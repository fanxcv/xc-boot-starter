package com.fan.xc.boot.starter.exception

import com.fan.xc.boot.starter.enums.ReturnCode

/**
 * @author fan
 */
class InjectParamException : XcRunException {
    constructor(msg: String?) : super(ReturnCode.PARAM_ERROR.code(), msg)
    constructor(msg: String?, cause: Throwable?) : super(ReturnCode.PARAM_ERROR.code(), msg, cause)
}