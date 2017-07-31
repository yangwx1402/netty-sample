package com.young.netty.example.test

/**
  * Created by yangyong3 on 2017/7/28.
  */
trait Test {

  def test(name: String): String

  def test(name:String,age:Integer):String

  def print(user:User):User
}
