package functionaldecomposition.concurrent2.partial

import akka.actor._
import functionaldecomposition.concurrent.bestsolution.{Refresh, RefreshResponse, TryUpdateMessage}
import functionaldecomposition.concurrent.partial.{ChildSolutionCalculated, SearchForSolution}
import functionaldecomposition.domain.bestsolution.BestSolutionValue
import functionaldecomposition.domain.machine.Machines
import functionaldecomposition.domain.{ChildActorsTracker, Task}

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class AskingPartialSolutionActor extends Actor with ActorLogging {

  var partialSolutionsQueue: List[AskingPartialSolution] = _
  var bestSolutionValue: BestSolutionValue = _
  var childActorsTracker: ChildActorsTracker = _
  var solutionReceiver: ActorSelection = _

  def receive = {
    case SearchForSolution(machines, tasks) => go(machines, tasks)
    case RefreshResponse(bsv) => continue(bsv)
    case ChildSolutionCalculated() => registerChildSolutionFinished()
  }

  def go(machines: Machines, tasks: List[Task]) = {
    log.debug(s"Searching for partial result : ${machines.machinesUsed()}, tasks left: ${tasks.length}")
    this.solutionReceiver = context.actorSelection("akka://functional-decomposition/user/bestSolutionActor")
    this.partialSolutionsQueue = List(AskingPartialSolution(machines, tasks))
    continue(new BestSolutionValue())
  }

  def continue(bsv: BestSolutionValue): Unit = {
    this.bestSolutionValue = bsv
    while (partialSolutionsQueue.nonEmpty) {

      val partialSolution = partialSolutionsQueue.head
      partialSolutionsQueue = partialSolutionsQueue.tail

      val childSolutions = proceedWithPartialSolutions(partialSolution, bestSolutionValue)
      if (childSolutions.isEmpty) {
        solutionReceiver ! new Refresh()
        return
      } else {
        partialSolutionsQueue = childSolutions ::: partialSolutionsQueue
      }
    }
    self ! PoisonPill
    context.parent ! new ChildSolutionCalculated()
  }

  def proceedWithPartialSolutions(partialSolution: AskingPartialSolution, bestSolutionValue: BestSolutionValue): List[AskingPartialSolution] = {

    if (partialSolution.exceedsDeadline()) {
      return List()
      //      proceedWithPartialSolutions(bestSolutionValue)
    }
    val childSolutions: List[AskingPartialSolution] = partialSolution.getChildSolutions()
    if (childSolutions.isEmpty) {
      tryReportingBestSolution(partialSolution)
      List()
    } else {
      childSolutions
    }
  }

  def tryReportingBestSolution(partialSolution: AskingPartialSolution): Unit = {
    if ( bestSolutionValue.shouldBeUpdated(partialSolution.getMachines)) {
      this.solutionReceiver ! TryUpdateMessage(partialSolution.getMachines)
    }
  }

  def registerChildSolutionFinished(): Unit = {
    this.childActorsTracker.registerChildProcessEnded()
    if (childActorsTracker.isCompleted()) {
      context.parent ! ChildSolutionCalculated()
    }
  }
}

object AskingPartialSolutionActor {
  def createActors(count: Int, context: ActorContext): List[ActorRef] = {
    List.fill(count)(context.actorOf(Props(classOf[AskingPartialSolutionActor])))
  }
}

