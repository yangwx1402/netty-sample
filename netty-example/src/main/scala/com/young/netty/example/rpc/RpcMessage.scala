package com.young.netty.example.rpc

/**
  * Created by yangyong3 on 2017/7/28.
  */
sealed trait RpcMessage

case class RpcRequest(interfaceName: String, methodName: String, retrunClassName: String, anyRef: Any*) extends RpcMessage

case class RpcResponse(result: AnyRef, status: String, message: String) extends RpcMessage
