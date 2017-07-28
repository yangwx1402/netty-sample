package com.young.netty.example.rpc.netty.handler

import com.young.netty.example.rpc.{RpcResponse, RpcRequest}
import com.young.netty.example.util.{MethodCall, ClassUtils}
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}

/**
  * Created by yangyong3 on 2017/7/28.
  */
class ProcessCallHandler extends SimpleChannelInboundHandler[RpcRequest] {

  private def findImpl(request: RpcRequest): AnyRef = {
    ClassUtils.getInstance(request.interfaceName)
  }

  private def call(request: RpcRequest): RpcResponse = {
    val obj: AnyRef = findImpl(request)
    val argsClass: Array[Class[_]] = request.anyRef.map(obj => obj.getClass).toArray
    val method = MethodCall.getMethod(obj.getClass, request.methodName, argsClass)
    println(method)
    request.anyRef.foreach(obj=>println(obj))
    println(obj)
    //scala中Seq或者Array转变成可变参数 采用:_*
    val result = method.invoke(obj, request.anyRef:_*)
    RpcResponse(result,"ok","ok")
  }

  override def channelRead0(ctx: ChannelHandlerContext, msg: RpcRequest): Unit = {
    val result = call(msg)
    ctx.fireChannelRead(result)
  }
}
