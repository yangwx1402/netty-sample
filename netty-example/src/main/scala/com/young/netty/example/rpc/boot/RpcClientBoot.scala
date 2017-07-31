package com.young.netty.example.rpc.boot

import com.young.netty.example.rpc.netty.RemoteClient
import com.young.netty.example.rpc.netty.handler.{ClientProcessHandler, DecodeHandler}
import com.young.netty.example.rpc.serialization.DSerializable
import com.young.netty.example.rpc.serialization.support.{KyroDSerialization, JavaDSerialization}
import com.young.netty.example.rpc.{RpcRequest, RpcResponse}
import com.young.netty.example.test.{User, TestImpl}
import io.netty.buffer.Unpooled
import io.netty.channel.socket.SocketChannel
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter, ChannelInitializer}

/**
  * Created by yangyong3 on 2017/7/28.
  */


class RpcClientInitHandler(request: RpcRequest) extends ChannelInitializer[SocketChannel] {

  private class RpcClientCallHandler(dSerialization: DSerializable[RpcRequest, Array[Byte]], request: RpcRequest) extends ChannelInboundHandlerAdapter {
    override def channelActive(ctx: ChannelHandlerContext) {
      val bytes = dSerialization.serialization(request)
      val byteBuf = Unpooled.copiedBuffer(bytes)
      ctx.writeAndFlush(byteBuf)
      println("rpc client call ok " + bytes.length)
    }
  }

  override def initChannel(ch: SocketChannel): Unit = {
    val serializtion = new KyroDSerialization[RpcResponse](classOf[RpcResponse])
    val serializable1 = new KyroDSerialization[RpcRequest](classOf[RpcRequest])
    ch.pipeline().addLast(new DecodeHandler[RpcResponse](serializtion))
    ch.pipeline().addLast(new ClientProcessHandler)
    ch.pipeline().addLast(new RpcClientCallHandler(serializable1, request))
  }
}

object RpcClientBoot {

  def remoteCall(request: RpcRequest): Unit = {
    RemoteClient.connect(initHandler = new RpcClientInitHandler(request))
  }

  def main(args: Array[String]) {
    for (i <- 0 until 100)
      RpcClientBoot.remoteCall(RpcRequest(classOf[TestImpl].getName, "print", classOf[User].getName, User("beijing")))
  }
}
