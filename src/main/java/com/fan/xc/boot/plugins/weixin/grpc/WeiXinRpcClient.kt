package com.fan.xc.boot.plugins.weixin.grpc

import com.fan.xc.boot.plugins.weixin.WeiXinConfig
import com.google.protobuf.Empty
import io.grpc.ManagedChannelBuilder

class WeiXinRpcClient(private val config: WeiXinConfig) {
    private val stub: WeiXinServiceGrpc.WeiXinServiceBlockingStub by lazy {
        val client = config.client
        val server = config.server
        // 如果打开了server, 就一定不允许配置client
        return@lazy if (server?.isEnable != true && client?.isEnable == true) {
            val host = client.host ?: throw IllegalArgumentException("weixin.client.host error")
            val port = client.port
            if (port <= 0 || port >= 65535) {
                throw IllegalArgumentException("weixin.client.port error")
            }
            val channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext().build()
            WeiXinServiceGrpc.newBlockingStub(channel)
        } else throw IllegalStateException("init weixin rpc client failed")
    }

    fun getAccessToken(): String {
        val token = stub.getAccessToken(Empty.getDefaultInstance())
        return token.value
    }

    fun getJsTicket(): String {
        val token = stub.getJsTicket(Empty.getDefaultInstance())
        return token.value
    }
}