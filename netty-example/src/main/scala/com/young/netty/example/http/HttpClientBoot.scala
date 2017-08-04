package com.young.netty.example.http

import java.net.URI

import com.young.netty.example.rpc.netty.RemoteClient
import io.netty.buffer.Unpooled
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter, ChannelInitializer}
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http._

/**
  * Created by yangyong3 on 2017/7/31.
  */
class HttpClientBoot extends ChannelInitializer[SocketChannel] {

  private class HttpClientHandler extends ChannelInboundHandlerAdapter {
    override def channelRead(ctx: ChannelHandlerContext, msg: AnyRef): Unit = {
      if (msg.isInstanceOf[HttpResponse]) {
        val response = msg.asInstanceOf[HttpResponse]
        println(response.status().code())
      }
      if (msg.isInstanceOf[HttpContent]) {
        val content = msg.asInstanceOf[HttpContent]
        val byteBuf = content.content
        val bytes = new Array[Byte](byteBuf.readableBytes())
        byteBuf.readBytes(bytes)
        println(new String(bytes, "utf-8"))
      }
    }
  }

  override def initChannel(ch: SocketChannel): Unit = {
    ch.pipeline().addLast(new HttpResponseDecoder())
    ch.pipeline().addLast(new HttpRequestEncoder)
    ch.pipeline().addLast(new HttpClientHandler)
  }
}

object HttpClientBoot {
  def main(args: Array[String]) {
    val channel = RemoteClient.connect(initHandler = new HttpClientBoot, close = false)
    val uri = new URI("http://127.0.0.1:8080")
    val request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString, Unpooled.wrappedBuffer("Are you ok?".getBytes("UTF-8")))
    request.headers().set(HttpHeaderNames.HOST, "localhost")
    request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
    request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes())
    channel.channel().writeAndFlush(request)
    channel.channel().closeFuture().sync()
  }
}