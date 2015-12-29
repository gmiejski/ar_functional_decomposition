package functionaldecomposition.concurrent.bestsolution

import akka.actor.{ActorLogging, Actor}
import functionaldecomposition.domain.bestsolution.BestSolutionValue
import functionaldecomposition.domain.machine.Machines

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class BestSolutionActor(aaa: String) extends Actor with ActorLogging {

  var bestSolutionValue: BestSolutionValue = BestSolutionValue()

  def receive = {
    case TryUpdateMessage(machines) =>
      log.info(s"updating value in bestSolutionActor : ${machines.processingTimeSum()}")
      log.info(machines.printableForm())
      bestSolutionValue.tryUpdate(machines)
    case Refresh() => sender ! RefreshResponse(bestSolutionValue)
    case PrintSolution() => printSolution()
  }

  def printSolution() = {
    log.info("Final result:")
    log.info(bestSolutionValue.currentBestMachines.printableForm())
    context.system.terminate()
  }
}

case class TryUpdateMessage(machines: Machines)

case class Refresh()

case class RefreshResponse(bestSolutionValue: BestSolutionValue)

case class PrintSolution()