package bootstrap

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import models.Models._
import statefull.SellerStateful
import stateless.SellerStateless

object Application extends App {

  val actorSystem = ActorSystem("ActorBehaviour")
  val statelessActor = actorSystem.actorOf(Props[SellerStateless])
  val statefulActor = actorSystem.actorOf(Props[SellerStateful])
  val buyer = actorSystem.actorOf(Props[Buyer])

  class Buyer extends Actor with ActorLogging {

    val OrderForScala: Order = Order("123", "Scala", 2)
    val OrderForJava: Order = Order("1234", "Java", 1)

    override def receive: Receive = {
      case startTransaction(sellerRef) =>
        log.info("Starting Transaction")
        sellerRef ! RequestOrder(OrderForScala)
        sellerRef ! GetStatus(OrderForScala)
        sellerRef ! RequestOrder(OrderForJava)
        sellerRef ! PaymentRequest(OrderForScala, "cardNumber", 20L)
        sellerRef ! GetStatus(OrderForScala)
        sellerRef ! RequestOrder(OrderForJava)

      case Status(InProgress, order) =>
        log.info(s"$order is in progress")

      case Status(OrderAccept, order) =>
        log.info(s"$order is accepted")

      case Status(OrderReject, order) =>
        log.info(s"$order is rejected")

      case _ =>
        log.info("Oops! System Failure")
    }
  }

  buyer ! startTransaction(statefulActor)

  actorSystem.terminate()
}