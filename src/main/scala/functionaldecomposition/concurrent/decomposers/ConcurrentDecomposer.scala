package functionaldecomposition.concurrent.decomposers

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import functionaldecomposition.domain.{Deadline, Task}
import functionaldecomposition.standard.decomposers.StandardDecomposer

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class ConcurrentDecomposer extends Actor with ActorLogging {

  var computing: Boolean = false
  var finishedSubtasks = 0
  var partialSolutions = 0

  def receive = {
    case Solve(deadline, tasks) => solve(deadline, tasks)
    case FinishedPartialSolution() => logCompletedSubtask()
  }

  def logCompletedSubtask() = {
    finishedSubtasks += 1
    log.info(s"Completed ($finishedSubtasks / $partialSolutions) subtasks")
    if (finishedSubtasks == partialSolutions) {
      // TODO get response from bestSolutionActor
    }
  }

  def prepareForComputing(partialSolutions: Int) = {
    this.computing = true
    this.finishedSubtasks = 0
    this.partialSolutions = partialSolutions
  }

  def solve(deadline: Deadline, tasks: List[Task]) = {

    if (computing) {
      log.error("Cannot start computing before finishing previous one!")
      null
    } else {

      val possibleProcessesCount = StandardDecomposer.possibleProcessesCounts(deadline, tasks)
      prepareForComputing(possibleProcessesCount.length)
      val partialSolutionsManagers: List[ActorRef] = createChildActors(possibleProcessesCount)

      partialSolutionsManagers zip possibleProcessesCount foreach (s => {
        s._1 ! SearchForSolution(s._2, deadline, tasks)
      })
    }
  }

  def createChildActors(possibleProcessesCount: List[Int]): List[ActorRef] = {
    List.fill(possibleProcessesCount.length)(context.actorOf(Props(classOf[PartialSolutionActor])))
  }
}

case class Solve(deadline: Deadline, tasks: List[Task])

case class FinishedPartialSolution()