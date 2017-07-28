package com.young.netty.example.rpc.netty.handler

import com.young.netty.example.rpc.serialization.DSerializable
import io.netty.buffer.ByteBuf
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}

/**
  * Created by yangyong3 on 2017/7/28.
  * 解决rpc反序列化的Handler
  */
class DecodeHandler[T](dSerializable: DSerializable[T,Array[Byte]]) extends SimpleChannelInboundHandler[ByteBuf]{
  override def channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf): Unit = {
    val bytes = new Array[Byte](msg.readableBytes())
    msg.readBytes(bytes)
    val t = dSerializable.unSerialization(bytes)
    println("decode handler receive a message ["+t+"]")
    ctx.fireChannelRead(t)
  }
}
