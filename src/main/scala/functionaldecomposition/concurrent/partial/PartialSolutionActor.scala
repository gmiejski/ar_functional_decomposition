package functionaldecomposition.concurrent.partial

import akka.actor._
import functionaldecomposition.concurrent.bestsolution.{Refresh, TryUpdateMessage, RefreshResponse}
import functionaldecomposition.domain.bestsolution.BestSolutionValue
import functionaldecomposition.domain.machine.Machines
import functionaldecomposition.domain.{ChildActorsTracker, Task}

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class PartialSolutionActor extends Actor with ActorLogging {

  var partialSolution: ConcurrentPartialSolution = _
  var bestSolutionValue: BestSolutionValue = _
  var childActorsTracker: ChildActorsTracker = _

  def receive = {
    case SearchForSolution(machines, tasks) => saveActorConfiguration(machines, tasks)
    case RefreshResponse(bsv) => saveBestSolutionValue(bsv)
    case ChildSolutionCalculated() => registerChildSolutionFinished()
  }

  def saveActorConfiguration(machines: Machines, tasks: List[Task]) = {
    log.debug(s"Searching for partial result : ${machines.machinesUsed()}, tasks left: ${tasks.length}")
    val solutionReceiver = context.actorSelection("akka://functional-decomposition/user/bestSolutionActor")
    solutionReceiver ! Refresh()
    this.partialSolution = ConcurrentPartialSolution(machines, tasks, solutionReceiver)
  }

  def saveBestSolutionValue(bestSolutionValue: BestSolutionValue): Unit = {
    if (partialSolution.exceedsDeadline()) {
      finishComputingPath()
      return
    }
    this.bestSolutionValue = bestSolutionValue
    val childSolutions: List[ConcurrentPartialSolution] = this.partialSolution.getChildSolutions()
    // TODO change if for isLeaf and is not path
    if (childSolutions.isEmpty) {
      tryReportingBestSolution()
      finishComputingPath()
    } else {
      // create child actors
      val childSolutionsActors: List[ActorRef] = PartialSolutionActor.createActors(childSolutions.length, context)
      // save their number to wait for result
      childActorsTracker = new ChildActorsTracker(childSolutionsActors.length)

      // tell them to find solution
      childSolutionsActors zip childSolutions foreach (s => {
        s._1 ! SearchForSolution(s._2.getMachines, s._2.tasks)
      })
    }
  }

  def finishComputingPath() = {
    context.parent ! ChildSolutionCalculated()
    self ! PoisonPill
  }

  def tryReportingBestSolution(): Unit = {
    val solutionReceiver = context.actorSelection("akka://functional-decomposition/user/bestSolutionActor")
    solutionReceiver ! TryUpdateMessage(partialSolution.getMachines)
  }

  def registerChildSolutionFinished(): Unit = {
    this.childActorsTracker.registerChildProcessEnded()
    if ( childActorsTracker.isCompleted()) {
      context.parent ! ChildSolutionCalculated()
    }
  }
}

object PartialSolutionActor {
  def createActors(count: Int, context: ActorContext): List[ActorRef] = {
    List.fill(count)(context.actorOf(Props(classOf[PartialSolutionActor])))
  }
}

case class SearchForSolution(machines: Machines, tasks: List[Task])

case class ChildSolutionCalculated()