package com.fan.xc.boot.plugins.api.gateway.chain

import com.fan.xc.boot.plugins.adapter.redis.Redis
import com.fan.xc.boot.plugins.api.gateway.DefaultGatewayHandler
import com.fan.xc.boot.starter.event.Event
import com.fan.xc.boot.starter.exception.XcRunException
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.expression.EvaluationContext
import org.springframework.expression.ExpressionParser
import org.springframework.expression.ParserContext
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

/**
 * 基于redis的滑动窗口限流实现
 * @author fan
 */
@ConditionalOnBean(Redis::class)
class RollWindowRateLimitChain(private val redis: Redis) : AbstractGatewayChain<DefaultGatewayHandler.ApiCheckData>() {
    private val parser: ExpressionParser = SpelExpressionParser()

    // 采用redis zset数据结构实现滑动窗口
    // 先移除过期窗口的统计, 再统计现有的数据总量, 如果数据量小于当前limit, 插入新的数据, 成功执行返回OK, 否则返回FAIL
    private val luaScript = "local key = KEYS[1]\n" +
            "local invalidMill = '[' .. ARGV[1]\n" +
            "local limit = tonumber(ARGV[2])\n" +
            "local now = ARGV[3]\n" +
            "redis.call('ZREMRANGEBYLEX', key, '(0', invalidMill)\n" +
            "local count = redis.call('ZCARD', key) or 0\n" +
            "if (count < limit) then\n" +
            "    redis.call('ZADD', key, now, now)\n" +
            "    return '1'\n" +
            "else\n" +
            "    return '0'\n" +
            "end"
    private val success = 1

    override fun doExec(event: Event, v: DefaultGatewayHandler.ApiCheckData): Boolean? {
        val limits = v.check?.rollLimit
        if (limits == null || limits.isEmpty()) {
            // 如果没有配置滑动窗口限流
            return null
        }
        // 设置EL表达式上下文
        val context: EvaluationContext = StandardEvaluationContext()
        event.getParamMap().forEach { (k, v) -> context.setVariable(k, v) }

        // 只处理可用的配置
        limits.filter { it.value > 0 }
                .forEach {
                    // 获取key, 解析EL表达式
                    val key = parser.parseExpression(it.key, ParserContext.TEMPLATE_EXPRESSION).getValue(context, String::class.java)
                    // 计算窗口时间大小
                    val window = it.window
                    val totalTime = it.timeUnit.toMillis(it.time)
                    val windowTime = totalTime / window + if (totalTime % window == 0L) 0 else 1
                    // 计算当前时间所在的窗口
                    val current = System.currentTimeMillis()
                    val currentWindow = current / windowTime + if (current % windowTime == 0L) 0 else 1
                    // 获取失效时间
                    val invalidMill = windowTime * (currentWindow - window)
                    val result = redis.exec(luaScript, Int::class.java, listOf(key), invalidMill, it.value, current)
                    if (success != result) {
                        throw XcRunException(-103, "请求频率过高,请稍后再试")
                    }
                }
        return null
    }
}