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
  var startTime: Long = _

  def receive = {
    case Solve(deadline, tasks) => solve(deadline, tasks)
    case ChildSolutionCalculated() => logCompletedSubtask()
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

  def prepareForComputing(partialSolutions: Int) = {
    this.computing = true
    this.childActorsTracker = new ChildActorsTracker(partialSolutions)
    this.startTime = System.currentTimeMillis()
  }

  def logCompletedSubtask() = {
    this.childActorsTracker.registerChildProcessEnded()
    log.debug(this.childActorsTracker.getMessage())
    if (this.childActorsTracker.isCompleted()) {
      // TODO get response from bestSolutionActor
      this.computing = false
      this.childActorsTracker = null
      val time = (System.currentTimeMillis() - startTime) / 1000
      context.actorSelection("akka://functional-decomposition/user/bestSolutionActor") ! PrintSolution(time)
    }
  }

  def createChildActors(possibleProcessesCount: List[Int]): List[ActorRef] = {
    PartialSolutionActor.createActors(possibleProcessesCount.length, context)
  }
}

case class Solve(deadline: Deadline, tasks: List[Task])