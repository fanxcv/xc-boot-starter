package com.fan.xc.boot.plugins.coroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object KotlinCoroutines {
    /**
     * 替换多线程执行异步方法
     */
    @JvmStatic
    fun run(run: RunFunction) {
        GlobalScope.launch {
            run.run()
        }
    }
}