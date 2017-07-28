package com.young.netty.example.simple

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.{Unpooled, ByteBuf}
import io.netty.channel.socket.SocketChannel
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel

/**
  * Created by yangyong3 on 2017/7/26.
  */
class NettyClient {

  private class ClientSendThread(ctx: ChannelHandlerContext) extends Runnable {
    override def run(): Unit = {
      var index = 1
      while (true) {
        val message = "client send message [" + index + "]"
        val buffer = Unpooled.copiedBuffer(message.getBytes("utf-8"))
        ctx.write(buffer)
        ctx.flush()
        println("write data")
        index += 1
        Thread.sleep(1000)
      }
    }
  }


  private class ClientTimeHandler extends SimpleChannelInboundHandler[ByteBuf] {

    override def channelActive(ctx: ChannelHandlerContext) {
      new Thread(new ClientSendThread(ctx)).start()
      println("client连接成功，启动一个线程")
    }

    override def channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf): Unit = {
      val bytes = new Array[Byte](msg.readableBytes())
      msg.readBytes(bytes)
      println("client read server message ["+(new String(bytes,"utf-8"))+"]")
    }
  }

  private class ClientChildHandler extends ChannelInitializer[SocketChannel] {
    override def initChannel(ch: SocketChannel): Unit = {
      ch.pipeline().addLast(new ClientTimeHandler)
    }
  }

  def connect(ip: String = "localhost", port: Int = 8080): Unit = {
    val bootStrap = new Bootstrap
    val eventGroup = new NioEventLoopGroup()
    val tcp_nodelay: ChannelOption[Boolean] = ChannelOption.valueOf("TCP_NODELAY")
    bootStrap.group(eventGroup).channel(classOf[NioSocketChannel]).option(tcp_nodelay, true).handler(new ClientChildHandler)
    val futureChannel = bootStrap.connect(ip, port).sync()
    futureChannel.channel().closeFuture().sync()
  }
}

object NettyClient {
  def main(args: Array[String]) {
    val client = new NettyClient
    client.connect()
  }
}
