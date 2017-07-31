package com.young.netty.example.rpc.netty

import java.io.IOException

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel

/**
  * Created by yangyong3 on 2017/7/27.
  */
class RemoteClient(initHandler: ChannelInitializer[SocketChannel]) extends RemoteTrait {

  def connect(ip: String = "localhost", port: Int = 8080): Unit = {
    val bootStrap = new Bootstrap
    val pool = new NioEventLoopGroup(10)
    try {
      bootStrap.group(pool).channel(classOf[NioSocketChannel]).option(tcp_nodelay, true).handler(initHandler)
      val futureChannel = bootStrap.connect(ip, port).sync()
      futureChannel.channel().closeFuture().sync()
    } catch {
      case e: IOException => println("断开连接")
        System.exit(-1)
    }finally{
        pool.shutdownGracefully()
    }
  }
}

object RemoteClient {
  def connect(ip: String = "localhost", port: Int = 8080, initHandler: ChannelInitializer[SocketChannel]): Unit = {
    val client = new RemoteClient(initHandler)
    client.connect(ip, port)
  }
}
