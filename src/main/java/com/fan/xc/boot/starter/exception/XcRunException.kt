package com.fan.xc.boot.starter.exception

import com.fan.xc.boot.starter.enums.ReturnCode

/**
 * @author fan
 */
open class XcRunException : RuntimeException {
    var code = -1
        private set

    constructor(codeEnum: ReturnCode) : super(codeEnum.message()) {
        this.code = codeEnum.code()
    }

    constructor(code: Int, msg: String?) : super(msg) {
        this.code = code
    }

    constructor(msg: String?) : super(msg)
    constructor(cause: Throwable?) : super(cause)
    constructor(code: Int, cause: Throwable?) : super(cause) {
        this.code = code
    }

    constructor(msg: String?, cause: Throwable?) : super(msg, cause)
    constructor(code: Int, msg: String?, cause: Throwable?) : super(msg, cause) {
        this.code = code
    }
}