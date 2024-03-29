package com.fan.xc.boot.plugins.weixin.grpc

import com.google.protobuf.Empty
import io.grpc.ManagedChannelBuilder
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class WeiXinRpcClientTest {
    private lateinit var stub: WeiXinServiceGrpc.WeiXinServiceBlockingStub

    @BeforeAll
    fun before() {
        val channel = ManagedChannelBuilder.forAddress("127.0.0.1", 20002)
                .usePlaintext().build()
        stub = WeiXinServiceGrpc.newBlockingStub(channel)
    }

    @Test
    fun test() {
        val token = stub.getAccessToken(Empty.getDefaultInstance())
        println(token.value)
    }
}
