package com.young.netty.example.rpc.netty.handler

import com.young.netty.example.rpc.RpcResponse
import com.young.netty.example.rpc.serialization.DSerializable
import io.netty.buffer.Unpooled
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}

/**
  * Created by yangyong3 on 2017/7/28.
  */
class EncodeHandler(dSerializable: DSerializable[RpcResponse,Array[Byte]]) extends SimpleChannelInboundHandler[RpcResponse]{
  override def channelRead0(ctx: ChannelHandlerContext, msg: RpcResponse): Unit = {
    val bytes = dSerializable.serialization(msg)
    val byteBuf = Unpooled.copiedBuffer(bytes)
    ctx.writeAndFlush(byteBuf)
  }
}
