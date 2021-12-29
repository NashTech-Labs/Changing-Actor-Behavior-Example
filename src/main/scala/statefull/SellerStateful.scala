package statefull

import akka.actor.{Actor, ActorLogging}
import models.Models._

class SellerStateful extends Actor with ActorLogging{

  var status: String = OrderAccept

  override def receive: Receive = {
    case RequestOrder(order) if (status == OrderAccept) =>
      log.info(s"Request for $order accepted")
      status = InProgress

    case RequestOrder(order) =>
      log.info(s"Confirm the Payment for $order")
      sender() ! Status(OrderReject, order)

    case PaymentRequest(order, _, amount) =>
      log.info(s"Payment Received $amount for $order")
      processOrder(order)
      log.info("Seller is Ready For Next Order")
      status = OrderAccept

    case GetStatus(order) =>
      if (status == OrderAccept) sender() ! Status(OrderAccept, order)
      else if (status == OrderReject) sender() ! Status(OrderReject, order)
      else sender() ! Status(InProgress, order)

  }

  private def processOrder(order: Order): Unit = {
    log.info(s"Order with orderId ${order.OrderId} is ready!")
  }
}