package com.young.netty.example.rpc.boot

import com.young.netty.example.rpc.netty.RemoteServer
import com.young.netty.example.rpc.netty.handler.{DecodeHandler, EncodeHandler, ProcessCallHandler}
import com.young.netty.example.rpc.serialization.support.{KyroDSerialization, JavaDSerialization}
import com.young.netty.example.rpc.{RpcRequest, RpcResponse}
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

/**
  * Created by yangyong3 on 2017/7/28.
  */


class RpcServerInitHandler extends ChannelInitializer[SocketChannel] {
  override def initChannel(ch: SocketChannel): Unit = {
    val serialization = new KyroDSerialization[RpcRequest](classOf[RpcRequest])
    val serialization1 = new KyroDSerialization[RpcResponse](classOf[RpcResponse])
    ch.pipeline().addLast(new DecodeHandler[RpcRequest](serialization))
    ch.pipeline().addLast(new ProcessCallHandler)
    ch.pipeline().addLast(new EncodeHandler(serialization1))
  }
}
object RpcServerBoot {
  def main(args: Array[String]) {
    RemoteServer.start(new RpcServerInitHandler)
  }
}
