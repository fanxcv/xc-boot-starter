package com.fan.xc.boot.starter.event

import com.fan.xc.boot.starter.enums.ReturnCode

object Return {
    @JvmStatic
    fun success(data: Any?): Event = EventImpl.getEvent().setReturn(ReturnCode.SUCCESS).setBody(data)

    @JvmStatic
    fun success(): Event = EventImpl.getEvent().setReturn(ReturnCode.SUCCESS)

    @JvmStatic
    fun fail(): Event = EventImpl.getEvent().setReturn(ReturnCode.FAIL)

    @JvmStatic
    fun fail(data: Any?): Event = EventImpl.getEvent().fail(data)

    @JvmStatic
    fun fail(code: Int, data: Any?): Event = EventImpl.getEvent().setReturn(code, data)

    @JvmStatic
    fun data(): Event = EventImpl.getEvent()
}