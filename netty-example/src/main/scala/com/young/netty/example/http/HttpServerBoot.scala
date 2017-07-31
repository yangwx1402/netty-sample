package com.young.netty.example.http

import com.young.netty.example.rpc.netty.RemoteServer
import io.netty.buffer.Unpooled
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter, ChannelInitializer}
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http._

/**
  * Created by yangyong3 on 2017/7/31.
  */
class HttpServerBoot extends ChannelInitializer[SocketChannel] {

  private class HttpServerHandler extends ChannelInboundHandlerAdapter {
    override def channelRead(ctx: ChannelHandlerContext, msg: AnyRef): Unit = {
      if (msg.isInstanceOf[HttpRequest]) {
        val request = msg.asInstanceOf[HttpRequest]
        println(request.uri())
      }
      if (msg.isInstanceOf[HttpContent]) {
        val content = msg.asInstanceOf[HttpContent]
        val byteBuf = content.content()
        val bytes = new Array[Byte](byteBuf.readableBytes())
        byteBuf.readBytes(bytes)
        println(new String(bytes, "utf-8"))
        val httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(("i am ok").getBytes("utf-8")))
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain")
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes())
        ctx.writeAndFlush(httpResponse)
      }
    }
  }

  override def initChannel(ch: SocketChannel): Unit = {
    //HttpResponseEncoder是一个Output Bounder Handler
    ch.pipeline().addLast(new HttpResponseEncoder)
    //HttpRequestDecoder是一个Input Bounder handler
    ch.pipeline().addLast(new HttpRequestDecoder)
    //处理业务逻辑
    ch.pipeline().addLast(new HttpServerHandler)
  }
}

object HttpServerBoot{
  def main(args: Array[String]) {
    RemoteServer.start(new HttpServerBoot)
  }
}
