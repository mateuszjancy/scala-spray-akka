package com.mjs.spray.hello.factorial.service

import akka.actor.{Actor, ActorLogging, ActorRef}

import scala.annotation.tailrec

object FactorialService {

  def factorial (value:BigInt): BigInt = value match {
    case a: BigInt if a == 0 => 1
    case _ => value * factorial(value - 1)
  }

  @tailrec
  def calculate(x: BigInt, y: BigInt=1): BigInt = {
    if(x==1) y else calculate(x-1,x*y)
  }

  def apply() = new FactorialService()
}

class FactorialService() extends Actor with ActorLogging {
  import FactorialService._

  def receive = {
    case value: Int => sender ! calculate(value)
    case _ => log.error("Unsupported message")
  }
}
