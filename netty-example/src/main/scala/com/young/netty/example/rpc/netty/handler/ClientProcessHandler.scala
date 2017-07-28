package com.young.netty.example.rpc.netty.handler

import com.young.netty.example.rpc.RpcResponse
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}

/**
  * Created by yangyong3 on 2017/7/28.
  */
class ClientProcessHandler extends SimpleChannelInboundHandler[RpcResponse]{
  override def channelRead0(ctx: ChannelHandlerContext, msg: RpcResponse): Unit = {
    println("obj="+msg.result+",status="+msg.status+",message="+msg.message)
  }
}
