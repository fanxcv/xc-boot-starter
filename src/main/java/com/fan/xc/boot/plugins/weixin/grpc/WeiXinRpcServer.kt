package com.fan.xc.boot.plugins.weixin.grpc

import com.alibaba.fastjson.JSON
import com.fan.xc.boot.plugins.weixin.AccessTokenManager
import com.fan.xc.boot.plugins.weixin.JsApiTicketManager
import com.fan.xc.boot.plugins.weixin.WeiXinConfig
import com.fan.xc.boot.plugins.weixin.grpc.WeiXinServiceGrpc.WeiXinServiceImplBase
import com.google.protobuf.Empty
import com.google.protobuf.StringValue
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import org.springframework.beans.factory.InitializingBean

/**
 * 实现grpc Server
 * @author yangfan323
 */
class WeiXinRpcServer(private val config: WeiXinConfig,
                      private val accessTokenManager: AccessTokenManager,
                      private val jsApiTicketManager: JsApiTicketManager)
    : WeiXinServiceImplBase(), InitializingBean {
    /**
     * 获取access token
     */
    override fun getAccessToken(request: Empty, responseObserver: StreamObserver<StringValue>) {
        val json = JSON.toJSONString(mapOf("code" to 0, "access_token" to accessTokenManager.token(), "expires" to accessTokenManager.expires()))
        val stringValue = StringValue.newBuilder().setValue(json).build()
        responseObserver.onNext(stringValue)
        responseObserver.onCompleted()
    }

    /**
     * 获取js ticket
     */
    override fun getJsTicket(request: Empty, responseObserver: StreamObserver<StringValue>) {
        val json = JSON.toJSONString(mapOf("code" to 0, "ticket" to jsApiTicketManager.token(), "expires" to jsApiTicketManager.expires()))
        val stringValue = StringValue.newBuilder().setValue(json).build()
        responseObserver.onNext(stringValue)
        responseObserver.onCompleted()
    }

    override fun afterPropertiesSet() {
        val server = config.server
        if (server?.isEnable == true) {
            // 开启server
            ServerBuilder.forPort(server.port).addService(this)
                    .build().start()
        }
    }
}