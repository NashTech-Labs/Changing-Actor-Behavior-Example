package stateless

import models.Models._
import akka.actor.{Actor, ActorLogging}

class SellerStateless extends Actor with ActorLogging {

  override def receive: Receive = startTransaction

  def startTransaction: Receive = {

    case RequestOrder(order: Order) =>
      log.info(s"Request for $order accepted")
      context.become(inProgress, discardOld = false)

    case GetStatus(order) => sender() ! Status(OrderAccept, order)
  }

  def inProgress: Receive = {

    case RequestOrder(order) =>
      log.info(s"Confirm the Payment for $order")
      sender() ! Status(OrderReject, order)

    case PaymentRequest(order, _, amount) =>
      log.info(s"Payment Received $amount for $order")
      processOrder(order)
      log.info("Seller is Ready For Next Order")
      context.unbecome()

    case GetStatus(order) =>
      sender() ! Status(InProgress, order)
  }

  private def processOrder(order: Order): Unit = {
    log.info(s"Order with orderId ${order.OrderId} is ready!")
  }
}