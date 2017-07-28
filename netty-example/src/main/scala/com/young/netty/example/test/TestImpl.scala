package com.young.netty.example.test

import java.util.Date

/**
  * Created by yangyong3 on 2017/7/28.
  */
class TestImpl extends Test{
  override def test(name: String): String = {
    println("test")
    name+" now time is :"+new Date()
  }

  override def test(name: String, age: Integer): String = {
    println("name="+name+",age="+age)
    name+age+" now time is :"+new Date()
  }
}
