package com.fan.xc.boot.plugins.api.cache;

import com.fan.xc.boot.plugins.adapter.redis.Redis;
import com.fan.xc.boot.starter.Dict;
import com.fan.xc.boot.starter.enums.ReturnCode;
import com.fan.xc.boot.starter.event.Event;
import com.fan.xc.boot.starter.event.EventImpl;
import com.fan.xc.boot.starter.utils.Tools;
import com.fan.xc.boot.plugins.coroutines.KotlinCoroutines;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Supplier;

/**
 * 提供各种情况下的缓存包装
 * @author fan
 */
@Slf4j
public class CacheUtils implements EnvironmentAware {
    private final ExpressionParser parser = new SpelExpressionParser();
    private static final String VALUE_KEY = "__SerializableValue__";
    private final Redis redis;
    private boolean isDev;

    public CacheUtils(Redis redis) {
        this.redis = redis;
    }

    @Builder
    @ToString
    protected static class Config {
        /**
         * 缓存Key
         */
        private final String key;
        /**
         * 判断是否返回新数据的key，用于前端自带缓存的业务
         */
        private final String checkKey;
        /**
         * 前端传过来的检查值，用于前端自带缓存的业务
         */
        private final Object checkValue;
        /**
         * 数据最大缓存时间
         */
        private final int expireTime;
        /**
         * 提前异步刷新时间
         */
        private final int refreshBefore;
        /**
         * 是否续期过期时间
         */
        private final boolean renewal;
        /**
         * 需要缓存的数据列表，会从Event对象中获取
         */
        private final List<String> cacheKeys;
    }

    /**
     * 缓存执行方法
     */
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        try {
            Object[] args = point.getArgs();
            // 获取Event对象
            Event e = EventImpl.getEvent();
            // 获取方法对定义
            Class<?> clazz = point.getTarget().getClass();
            MethodSignature ms = (MethodSignature) point.getSignature();
            Method method = clazz.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
            // 获取配置
            Config config = getConfig(method, args);
            log.debug("缓存配置：{}", config);
            withCheckCache(e, config, point::proceed);
            return e;
        } catch (Exception ex) {
            log.error("获取方法定义失败！！！直接调用方法", ex);
            return point.proceed();
        }
    }

    private Config getConfig(Method method, Object[] args) {
        // 获取参数列表
        Parameter[] parameters = method.getParameters();
        Assert.notNull(parameters, "方法参数不匹配，不支持缓存进行");
        Assert.isTrue(parameters.length == args.length, "方法定义参数值个数与入参数量不符");
        // 获取注解对象
        XCCache annotation = method.getAnnotation(XCCache.class);
        // 设置EL表达式上下文
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameters.length; i++) {
            context.setVariable(parameters[i].getName(), args[i]);
        }
        // 获取注解相关参数
        String checkKey = annotation.checkKey();
        String checkValue = annotation.checkValue();
        String[] cacheKeys = annotation.cacheKeys();

        return Config.builder()
                .key(parser.parseExpression(annotation.value(), ParserContext.TEMPLATE_EXPRESSION).getValue(context, String.class))
                .checkKey(Dict.DEFAULT_VALUE.equals(checkKey) ? null :
                        parser.parseExpression(checkKey, ParserContext.TEMPLATE_EXPRESSION).getValue(context, String.class))
                .checkValue(Dict.DEFAULT_VALUE.equals(checkValue) ? null :
                        parser.parseExpression(checkValue, ParserContext.TEMPLATE_EXPRESSION).getValue(context))
                .cacheKeys(cacheKeys.length == 0 ? null : Arrays.asList(cacheKeys))
                .refreshBefore(annotation.refreshBefore())
                .expireTime(annotation.expire())
                .renewal(annotation.renewal())
                .build();
    }

    /**
     * 简单的数据缓存，如果redis中有数据，那么数据会从redis中读取，
     * 如果redis中没有数据，则会执行实际的数据获取方法，并将执行结果返回
     */
    public void simpleCache(Event e, Config config, RunFunction func, Supplier<Object> callBack) throws Throwable {
        Assert.notNull(e, "Event对象不能为null");
        // 先判断config里的key是否存在，不存在直接从数据库获取数据
        // 再判断redis里有这个key相关的数据没有
        boolean hasKey = config.key != null && redis.exists(config.key);
        if (!hasKey) {
            refresh(e, config, func, callBack);
        }

        // 如果存在缓存，直接从缓存中获取
        // 避免直接使用hGetAll
        Map<String, Object> data = redis.hGet(config.key, VALUE_KEY);
        // 如果异步刷新时间设置大于0, 且小于到期时间, 且大于redis过期时间, 则触发刷新
        // 在本次获取完数据口就开始刷新数据
        if (config.refreshBefore > 0
                && config.refreshBefore < config.expireTime
                && config.refreshBefore >= redis.ttl(config.key)) {
            KotlinCoroutines.run(() -> {
                String lockKey = "lock:" + config.key;
                // 使用分布式锁避免并发刷新
                if (redis.tryGetDistributedLock(lockKey, lockKey, config.refreshBefore * 1000)) {
                    refresh(e, config, func, callBack);
                    redis.releaseDistributedLock(lockKey, lockKey);
                }
            });
        }
        // 按配置续期过期时间
        if (config.renewal) {
            redis.expire(config.key, config.expireTime);
        }
        e.setMap(data);
        if (isDev) {
            // 添加一个标志，判断数据从哪里获取到的
            String from = "fromRedis";
            e.set(from, true);
        }
    }

    /**
     * 刷新数据
     */
    private void refresh(Event e, Config config, RunFunction func, Supplier<Object> callBack) throws Throwable {
        // 如果缓存中没有数据,先执行正常的方法，获取到Event对象
        Object o = func.run();
        // 如果返回不是event对象,需要对返回数据做处理
        if (!(o instanceof Event)) {
            e.setReturn(ReturnCode.SUCCESS)
                    .setBody(o);
        }
        Map<String, Object> data;
        if (config.cacheKeys == null) {
            // 如果没有设置需要缓存的字段，那就缓存整个Event对象
            data = e.toMap();
        } else {
            // 从Event对象中获取需要暂存的字段
            data = new HashMap<>(Tools.nextPowerOf2(config.cacheKeys.size()));
            config.cacheKeys.forEach(it -> data.put(it, e.getParam(it)));
        }

        Optional.of(data).ifPresent(it -> {
            Map<String, Object> saveMap = new HashMap<>(Tools.nextPowerOf2(it.size()));
            // 保存前需要清理掉没有值的字段，避免存储过多数据
            it.forEach((k, v) -> {
                if (v != null) {
                    saveMap.put(k, v);
                }
            });
            // redis.hmSet(config.key, saveMap);
            // 此处的序列化是为了保证数据获取时的效率
            redis.hSet(config.key, VALUE_KEY, saveMap);
            // 添加下到期时间
            redis.expire(config.key, config.expireTime);
        });

        // 执行回调, 返回了数据的话，需要更新下redis里检查字段的值，用于下次的判断
        Optional.ofNullable(callBack).ifPresent(Supplier::get);
    }

    /**
     * 带检查字段的缓存，
     * 用于前端有自己的缓存实现，后端根据参数字段判断是否需要返回新的数据
     */
    public void withCheckCache(Event e, Config config, RunFunction func) throws Throwable {
        Assert.notNull(e, "Event对象不能为null");
        // 先判断是否包含需要检查的key
        // 然后判断是否包含需要检查的key
        boolean isExists = config.checkKey != null && redis.hExists(config.key, config.checkKey);
        if (isExists) {
            // 如果存在检查字段，判断下前台传过来的字段值与redis里面的值是否一致
            if (config.checkValue == null) {
                // 如果检查值为null，必须返回数据
                simpleCache(e, config, func, () -> redis.hSet(config.key, config.checkKey, e.getParam(config.checkKey)));
            } else if (!config.checkValue.equals(redis.hGet(config.key, config.checkKey))) {
                // 如果检查值存在，并且与redis里面的值不同，需要返回数据
                simpleCache(e, config, func, () -> redis.hSet(config.key, config.checkKey, e.getParam(config.checkKey)));
            } else {
                // 检查值存在，并且与redis里面的值一致，那就不用返回任何数据了
                e.success("数据未变化");
            }
        } else {
            simpleCache(e, config, func, null);
            // 如果从Event中获取到的待检查值不为null，那就需要存到redis里面去，方便后面使用
            Optional.ofNullable(config.checkKey)
                    .map(e::getParam)
                    .ifPresent(value -> redis.hSet(config.key, config.checkKey, value));
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        List<String> profiles = Arrays.asList(environment.getActiveProfiles());
        this.isDev = profiles.contains("dev") || profiles.contains("test");
    }
}
