package models

import akka.actor.ActorRef

object Models {

  case class startTransaction(sellerRef: ActorRef)

  case class Order(OrderId: String, ItemName: String, Quantity: Int)

  case class RequestOrder(order: Order)

  case class Status(message: String, order: Order)

  case class GetStatus(order: Order)

  case class PaymentRequest(order: Order, cardNumber: String, amount: Long)

  val OrderAccept = "Accept Order"
  val OrderReject = "Reject Order"
  val InProgress = "In Progress"

}