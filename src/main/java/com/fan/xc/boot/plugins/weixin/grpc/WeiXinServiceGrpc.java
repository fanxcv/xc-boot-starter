package com.fan.xc.boot.plugins.weixin.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.39.0)",
    comments = "Source: weixin.proto")
public final class WeiXinServiceGrpc {

  private WeiXinServiceGrpc() {}

  public static final String SERVICE_NAME = "WeiXinService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.StringValue> getGetAccessTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getAccessToken",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.google.protobuf.StringValue.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.StringValue> getGetAccessTokenMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.google.protobuf.StringValue> getGetAccessTokenMethod;
    if ((getGetAccessTokenMethod = WeiXinServiceGrpc.getGetAccessTokenMethod) == null) {
      synchronized (WeiXinServiceGrpc.class) {
        if ((getGetAccessTokenMethod = WeiXinServiceGrpc.getGetAccessTokenMethod) == null) {
          WeiXinServiceGrpc.getGetAccessTokenMethod = getGetAccessTokenMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.google.protobuf.StringValue>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getAccessToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.StringValue.getDefaultInstance()))
              .setSchemaDescriptor(new WeiXinServiceMethodDescriptorSupplier("getAccessToken"))
              .build();
        }
      }
    }
    return getGetAccessTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.StringValue> getGetJsTicketMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getJsTicket",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.google.protobuf.StringValue.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.StringValue> getGetJsTicketMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.google.protobuf.StringValue> getGetJsTicketMethod;
    if ((getGetJsTicketMethod = WeiXinServiceGrpc.getGetJsTicketMethod) == null) {
      synchronized (WeiXinServiceGrpc.class) {
        if ((getGetJsTicketMethod = WeiXinServiceGrpc.getGetJsTicketMethod) == null) {
          WeiXinServiceGrpc.getGetJsTicketMethod = getGetJsTicketMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.google.protobuf.StringValue>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getJsTicket"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.StringValue.getDefaultInstance()))
              .setSchemaDescriptor(new WeiXinServiceMethodDescriptorSupplier("getJsTicket"))
              .build();
        }
      }
    }
    return getGetJsTicketMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static WeiXinServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WeiXinServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WeiXinServiceStub>() {
        @java.lang.Override
        public WeiXinServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WeiXinServiceStub(channel, callOptions);
        }
      };
    return WeiXinServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static WeiXinServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WeiXinServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WeiXinServiceBlockingStub>() {
        @java.lang.Override
        public WeiXinServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WeiXinServiceBlockingStub(channel, callOptions);
        }
      };
    return WeiXinServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static WeiXinServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WeiXinServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WeiXinServiceFutureStub>() {
        @java.lang.Override
        public WeiXinServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WeiXinServiceFutureStub(channel, callOptions);
        }
      };
    return WeiXinServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class WeiXinServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * 获取accessToken
     * </pre>
     */
    public void getAccessToken(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.StringValue> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAccessTokenMethod(), responseObserver);
    }

    /**
     * <pre>
     * 获取accessToken
     * </pre>
     */
    public void getJsTicket(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.StringValue> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetJsTicketMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetAccessTokenMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                com.google.protobuf.StringValue>(
                  this, METHODID_GET_ACCESS_TOKEN)))
          .addMethod(
            getGetJsTicketMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                com.google.protobuf.StringValue>(
                  this, METHODID_GET_JS_TICKET)))
          .build();
    }
  }

  /**
   */
  public static final class WeiXinServiceStub extends io.grpc.stub.AbstractAsyncStub<WeiXinServiceStub> {
    private WeiXinServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WeiXinServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WeiXinServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * 获取accessToken
     * </pre>
     */
    public void getAccessToken(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.StringValue> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAccessTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 获取accessToken
     * </pre>
     */
    public void getJsTicket(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.StringValue> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetJsTicketMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class WeiXinServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<WeiXinServiceBlockingStub> {
    private WeiXinServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WeiXinServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WeiXinServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 获取accessToken
     * </pre>
     */
    public com.google.protobuf.StringValue getAccessToken(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAccessTokenMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 获取accessToken
     * </pre>
     */
    public com.google.protobuf.StringValue getJsTicket(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetJsTicketMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class WeiXinServiceFutureStub extends io.grpc.stub.AbstractFutureStub<WeiXinServiceFutureStub> {
    private WeiXinServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WeiXinServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WeiXinServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 获取accessToken
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.StringValue> getAccessToken(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAccessTokenMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 获取accessToken
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.StringValue> getJsTicket(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetJsTicketMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_ACCESS_TOKEN = 0;
  private static final int METHODID_GET_JS_TICKET = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final WeiXinServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(WeiXinServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ACCESS_TOKEN:
          serviceImpl.getAccessToken((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.StringValue>) responseObserver);
          break;
        case METHODID_GET_JS_TICKET:
          serviceImpl.getJsTicket((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.StringValue>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class WeiXinServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    WeiXinServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fan.xc.boot.plugins.weixin.grpc.WeiXinProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("WeiXinService");
    }
  }

  private static final class WeiXinServiceFileDescriptorSupplier
      extends WeiXinServiceBaseDescriptorSupplier {
    WeiXinServiceFileDescriptorSupplier() {}
  }

  private static final class WeiXinServiceMethodDescriptorSupplier
      extends WeiXinServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    WeiXinServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (WeiXinServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new WeiXinServiceFileDescriptorSupplier())
              .addMethod(getGetAccessTokenMethod())
              .addMethod(getGetJsTicketMethod())
              .build();
        }
      }
    }
    return result;
  }
}
