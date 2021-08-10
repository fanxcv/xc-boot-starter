package com.fan.xc.boot.starter.extension

data class Tuple2<out A, out B>(val a: A?, val b: B?)
data class Tuple3<out A, out B, out C>(val a: A?, val b: B?, val c: C?)
data class Tuple4<out A, out B, out C, out D>(val a: A?, val b: B?, val c: C?, val d: D?)
data class Tuple5<out A, out B, out C, out D, out E>(val a: A?, val b: B?, val c: C?, val d: D?, val e: E?)

typealias Pair<A, B> = Tuple2<A, B>
typealias Triple<A, B, C> = Tuple3<A, B, C>

object Tuple {
    operator fun <A, B> invoke(a: A?, b: B?) = Tuple2(a, b)
    operator fun <A, B, C> invoke(a: A?, b: B?, c: C?) = Tuple3(a, b, c)
    operator fun <A, B, C, D> invoke(a: A?, b: B?, c: C?, d: D?) = Tuple4(a, b, c, d)
    operator fun <A, B, C, D, E> invoke(a: A?, b: B?, c: C?, d: D?, e: E?) = Tuple5(a, b, c, d, e)
}