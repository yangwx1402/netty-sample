package com.young.netty.example.simple

import java.util.Date

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.{Unpooled, ByteBuf}
import io.netty.channel.socket.SocketChannel
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel

/**
  * Created by yangyong3 on 2017/7/26.
  * 搞一个发送time和echo的小程序，熟悉netty工作模式
  */
class NettyServer {

  private class ServerTimeHandler extends SimpleChannelInboundHandler[ByteBuf] {

    override def channelActive(ctx: ChannelHandlerContext): Unit = {
      println("server listener a connection ")
    }

    override def channelReadComplete(ctx: ChannelHandlerContext): Unit = {
      val message = "server time is ["+new Date+"]"
      val byteBuf = Unpooled.copiedBuffer(message.getBytes("utf-8"))
      ctx.writeAndFlush(byteBuf)
    }

    override def channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf): Unit = {
      val bytes = new Array[Byte](msg.readableBytes())
      msg.readBytes(bytes)
      println("server read client message ["+(new String(bytes,"utf-8"))+"]")
    }
  }

  private class ServerChildHandler extends ChannelInitializer[SocketChannel] {
    override def initChannel(ch: SocketChannel): Unit = {
      ch.pipeline().addLast(new ServerTimeHandler)
    }
  }


  def bind(port: Int = 8080): Unit = {
    val connectionThreadPool = new NioEventLoopGroup(10)
    val processThreadPool = new NioEventLoopGroup(100)
    val serverboot = new ServerBootstrap()
    val SO_BACKLOG : ChannelOption[Int] = ChannelOption.valueOf("SO_BACKLOG")
    serverboot.group(connectionThreadPool, processThreadPool).channel(classOf[NioServerSocketChannel]).option(SO_BACKLOG, 1024).childHandler(new ServerChildHandler)
    println("server bind port "+port)
    val futureChannel = serverboot.bind(port).sync()
    futureChannel.channel().closeFuture().sync()
  }
}
object NettyServer{
  def main(args: Array[String]) {
    val server = new NettyServer
    server.bind()
  }
}
