package com.fan.xc.boot.plugins.api.gateway.chain

import com.fan.xc.boot.starter.event.Event
import com.google.common.collect.Lists
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*


abstract class AbstractGatewayChain<V> {
    protected val log: Logger = LoggerFactory.getLogger(this::class.java)
    private var next: AbstractGatewayChain<V>? = null

    companion object {
        @JvmStatic
        fun <V> builder(head: AbstractGatewayChain<V>): Builder<V> {
            return Builder(head)
        }

        @JvmStatic
        fun <V> builder(): Builder<V> {
            return Builder()
        }
    }

    class Builder<V> {
        private val list: LinkedList<AbstractGatewayChain<V>> = Lists.newLinkedList()

        constructor()
        constructor(head: AbstractGatewayChain<V>) {
            list.addFirst(head)
        }

        fun head(chain: AbstractGatewayChain<V>): Builder<V> {
            list.addFirst(chain)
            return this
        }

        fun addChain(chain: AbstractGatewayChain<V>): Builder<V> {
            list.addLast(chain)
            return this
        }

        fun build(): AbstractGatewayChain<V> {
            // head指向第一个元素
            val head = list.removeFirst()
            var next: AbstractGatewayChain<V>? = head
            list.forEach { next = next?.next(it) }
            return head
        }
    }


    /**
     * 设置下一个节点
     */
    fun next(nextChain: AbstractGatewayChain<V>?): AbstractGatewayChain<V>? {
        next = nextChain
        return next
    }

    /**
     * 触发方法
     * 如果doExec执行无明确结果,即Null,继续执行后续流程,否则明确返回true,false
     */
    fun exec(event: Event, v: V): Boolean? {
        val res = doExec(event, v)
        return res ?: (next?.exec(event, v) ?: res)
    }

    /**
     * 逻辑执行
     * @return 校验结果,无明确结果返回Null, T为传入到下个doExec的参数,可为null
     */
    abstract fun doExec(event: Event, v: V): Boolean?

    fun chain(): String {
        val sb = StringBuilder()
        var next: AbstractGatewayChain<V>? = this
        while (next != null) {
            sb.append(next.javaClass.simpleName).append("->")
            next = next.next
        }
        val len = sb.length
        return sb.delete(len - 3, len - 1).toString();
    }

}
