package com.young.netty.example.rpc.netty

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

/**
  * Created by yangyong3 on 2017/7/27.
  */
class RemoteServer(initHandler: ChannelInitializer[SocketChannel], port: Int = 8080) extends RemoteTrait {

  @throws[Exception]
  def start(): Unit = {
    val serverBootStrap = new ServerBootstrap()
    val listenerPool = new NioEventLoopGroup(5)
    val processPool = new NioEventLoopGroup(10)
    serverBootStrap.group(listenerPool, processPool).channel(classOf[NioServerSocketChannel]).option(SO_BACKLOG, 1024).childHandler(initHandler)
    val futureChannel = serverBootStrap.bind(port).sync()
    futureChannel.channel().closeFuture().sync()
  }
}

object RemoteServer {
  @throws[Exception]
  def start(initHandler: ChannelInitializer[SocketChannel], port: Int = 8080): Unit = {
    val server = new RemoteServer(initHandler, port)
    println("start server port is "+port)
    server.start()
  }
}
