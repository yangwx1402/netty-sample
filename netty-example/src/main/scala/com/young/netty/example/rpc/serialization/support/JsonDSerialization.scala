package com.young.netty.example.rpc.serialization.support

import com.young.netty.example.rpc.serialization.{DSerializationException, DSerializable}
import org.codehaus.jackson.map.ObjectMapper

/**
  * Created by yangyong3 on 2017/7/26.
  */
class JsonDSerialization[T](clazz: Class[T]) extends DSerializable[T, String] {

  private val mapper = new ObjectMapper

  @throws[DSerializationException]
  override def serialization(from: T): String = {
    if (from.isInstanceOf[String])
      from.asInstanceOf[String]
    else
      mapper.writeValueAsString(from)
  }

  @throws[DSerializationException]
  override def unSerialization(to: String): T = {
    if (clazz == classOf[String])
      to.asInstanceOf[T]
    else
      mapper.readValue(to, clazz)
  }
}
