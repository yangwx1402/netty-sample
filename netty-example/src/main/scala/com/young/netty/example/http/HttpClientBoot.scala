package com.young.netty.example.http

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

/**
  * Created by yangyong3 on 2017/7/31.
  */
class HttpClientBoot extends ChannelInitializer[SocketChannel]{
  override def initChannel(ch: SocketChannel): Unit = {
    
  }
}
