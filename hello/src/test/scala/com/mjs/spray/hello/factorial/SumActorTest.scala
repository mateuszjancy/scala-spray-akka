package com.mjs.spray.hello.factorial

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.testkit.{ TestActors, TestKit, ImplicitSender }
import com.mjs.spray.hello.factorial.service.{CoordinatorService, FactorialService, SumService}
import com.mjs.spray.hello.factorial.service.SumService._
import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll

class MySpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("MySpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An SumActor actor" must {
    "Add two values" in {
      val sum = system.actorOf(Props(SumService()))
      sum ! 1
      sum ! 1
      expectMsg(Result(2))
    }
  }

  "Factorial test" must {
    "Calculate factorial" in {
      val factorial = system.actorOf(Props(FactorialService()))
      factorial ! 4
      expectMsg(24)
    }
  }

  /*"Coordinator test" must {
    "Sum two factorials" in {
      val coordinator = system.actorOf(Props(CoordinatorService(Req)))
      coordinator ! (4, 4)
      expectMsg(48)
    }
  }*/
}