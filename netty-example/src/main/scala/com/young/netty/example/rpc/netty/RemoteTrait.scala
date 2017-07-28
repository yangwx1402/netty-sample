package com.young.netty.example.rpc.netty

import io.netty.channel.ChannelOption

/**
  * Created by yangyong3 on 2017/7/27.
  */
trait RemoteTrait {

  val SO_BACKLOG : ChannelOption[Int] = ChannelOption.valueOf("SO_BACKLOG")

  val tcp_nodelay: ChannelOption[Boolean] = ChannelOption.valueOf("TCP_NODELAY")
}
