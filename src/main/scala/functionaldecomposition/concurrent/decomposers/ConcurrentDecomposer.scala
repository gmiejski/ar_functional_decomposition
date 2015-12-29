package functionaldecomposition.concurrent.decomposers

import akka.actor.{Actor, ActorLogging, ActorRef}
import functionaldecomposition.concurrent.bestsolution.PrintSolution
import functionaldecomposition.concurrent.partial.{ChildSolutionCalculated, PartialSolutionActor, SearchForSolution}
import functionaldecomposition.domain.machine.Machines
import functionaldecomposition.domain.{ChildActorsTracker, Deadline, Task}
import functionaldecomposition.standard.decomposers.StandardDecomposer

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class ConcurrentDecomposer extends Actor with ActorLogging {

  var computing: Boolean = false
  var childActorsTracker: ChildActorsTracker = _
  
  
  def receive = {
    case Solve(deadline, tasks) => solve(deadline, tasks)
    case ChildSolutionCalculated() => logCompletedSubtask()
  }

  def logCompletedSubtask() = {
    childActorsTracker.registerChildProcessEnded()
    log.info(childActorsTracker.getMessage())
    if (childActorsTracker.isCompleted()) {
      // TODO get response from bestSolutionActor
      context.actorSelection("akka://functional-decomposition/user/bestSolutionActor") ! PrintSolution()
    }
  }

  def prepareForComputing(partialSolutions: Int) = {
    this.computing = true
    this.childActorsTracker = new ChildActorsTracker(partialSolutions)
  }

  def solve(deadline: Deadline, tasks: List[Task]): Unit = {
    if (computing) {
      log.error("Cannot start computing before finishing previous one!")
    } else {

      val possibleProcessesCount = StandardDecomposer.possibleProcessesCounts(deadline, tasks)
      prepareForComputing(possibleProcessesCount.length)
      val childSolutionsActors: List[ActorRef] = createChildActors(possibleProcessesCount)

      childSolutionsActors zip possibleProcessesCount foreach (s => {
        val machinesForActor: Machines = Machines.apply(s._2, deadline)
        s._1 ! SearchForSolution(machinesForActor, tasks)
      })
    }
  }

  def createChildActors(possibleProcessesCount: List[Int]): List[ActorRef] = {
    PartialSolutionActor.createActors(possibleProcessesCount.length, context)
  }
}

case class Solve(deadline: Deadline, tasks: List[Task])