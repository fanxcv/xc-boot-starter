package com.fan.xc.boot.starter.handler

import com.fan.xc.boot.starter.enums.ReturnCode
import com.fan.xc.boot.starter.event.EventImpl
import com.fan.xc.boot.starter.exception.XcRunException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @Order(0)
    @ExceptionHandler(value = [XcRunException::class])
    fun xcRunExceptionHandler(e: XcRunException): Any {
        val error = EventImpl.getEvent()
        log.error("${error.getUrl()}\n\t${e.code}:${e.message}")
        error.setReturn(e.code, e.message)
        return error.error()
    }

    @Order(9)
    @ExceptionHandler(value = [Exception::class])
    fun exceptionHandler(e: Exception): Any {
        val error = EventImpl.getEvent()
        log.error("${error.getUrl()}\n\t${ReturnCode.SYSTEM_ERROR.code()}:${e.message}", e)
        error.setReturn(ReturnCode.SYSTEM_ERROR)
        return error.error()
    }
}