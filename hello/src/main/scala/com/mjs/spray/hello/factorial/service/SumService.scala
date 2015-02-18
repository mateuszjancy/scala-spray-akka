package com.mjs.spray.hello.factorial.service

import akka.actor.{Actor, ActorLogging, FSM}
import com.mjs.spray.hello.factorial.service.SumService.{State, Data}

object SumService {
  sealed trait Data
  sealed trait State

  case object Idle extends State
  case object Waiting extends State

  case object Uninitialized extends Data
  case class Value(value:BigInt) extends Data
  case class Result(value:BigInt)

  def apply() = new SumService()
}

class SumService() extends Actor with ActorLogging with FSM[State, Data] {
  import com.mjs.spray.hello.factorial.service.SumService._
  import scala.concurrent.duration._

  startWith(Idle, Uninitialized);

  when(Idle){
    case Event(a: BigInt, Uninitialized) => {
      log.debug("{}", a)
      goto(Waiting) using(Value(a))
    }
  }

  when(Waiting, stateTimeout = 120 seconds) {
    case StateTimeout => goto(Idle) using(Uninitialized)
    case Event(b: BigInt, t: Value) => {
      log.debug("{}, {}", b, t)

      val result = t.value + b
      sender ! Result(result)
      goto(Idle) using(Uninitialized)
    }
  }

  onTransition {
    case Idle -> Waiting => stateData match {
      case t: Value => {
        log.debug("Idle -> Waiting with {}", t)
      }
      case Uninitialized => {
        log.debug("Idle -> Waiting with Uninitialized")
      }
    }
    case Waiting -> Idle => stateData match  {
      case t: Value => {
        log.debug("Waiting -> Processing with {}", t)
      }
      case Uninitialized => {
        log.debug("Waiting -> Processing with Uninitialized")
      }
    }
  }

  whenUnhandled {
    case Event(e, s) => {
      log.warning("received unhandled request {} in state {}/{}", e, stateName, s)
      stay
    }
  }

  initialize()
}
