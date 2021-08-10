package com.fan.xc.boot.starter.handler.chain

import com.google.common.collect.Lists
import com.fan.xc.boot.starter.annotation.VerifyParam
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * 参数校验责任链模式
 * @author fan
 */
abstract class AbstractVerifyChain {
    protected val log: Logger = LoggerFactory.getLogger(this::class.java)
    private var next: AbstractVerifyChain? = null

    companion object {
        fun builder(head: AbstractVerifyChain): Builder {
            return Builder(head)
        }

        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        private val list: LinkedList<AbstractVerifyChain> = Lists.newLinkedList()

        constructor()
        constructor(head: AbstractVerifyChain) {
            list.addFirst(head)
        }

        fun head(chain: AbstractVerifyChain): Builder {
            list.addFirst(chain)
            return this
        }

        fun addChain(chain: AbstractVerifyChain): Builder {
            list.addLast(chain)
            return this
        }

        fun build(): AbstractVerifyChain {
            // head指向第一个元素
            val head = list.removeFirst()
            var next: AbstractVerifyChain? = head
            list.forEach { next = next?.next(it) }
            return head
        }
    }

    /**
     * 设置下一个节点
     */
    fun next(nextChain: AbstractVerifyChain?): AbstractVerifyChain? {
        next = nextChain
        return next
    }

    /**
     * 触发方法,先调用doCheck方法
     * 如果继续下一步标志位为false,返回该次结果
     * 如果继续下一步标志位为true,如果还有后续节点,则继续执行下个节点,否则返回该次结果
     */
    fun exec(key: String, value: Any?, verify: VerifyParam): Any? {
        val p = doExec(key, value, verify)
        return if (p.first) next?.exec(key, p.second, verify) ?: p.second else p.second
    }

    /**
     * 参数处理
     * 返回一个是否继续下一步的标志位和处理结果
     */
    abstract fun doExec(key: String, value: Any?, verify: VerifyParam): Pair<Boolean, Any?>
}