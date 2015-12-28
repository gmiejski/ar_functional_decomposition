package functionaldecomposition.concurrent.decomposers

import akka.actor.{ActorLogging, Actor}
import functionaldecomposition.concurrent.partial.ConcurrentPartialSolution
import functionaldecomposition.domain.machine.Machines
import functionaldecomposition.domain.{Task, Deadline}

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class PartialSolutionActor extends Actor with ActorLogging {

  def receive = {
    case SearchForSolution(numberOfMachines, deadline, tasks) => searchForPartialResult(numberOfMachines, deadline, tasks)
  }

  def searchForPartialResult(numberOfMachines: Int, deadline: Deadline, tasks: List[Task]) = {
    log.info(s"Searching for partial result : $numberOfMachines")
    val machines = Machines(numberOfMachines, deadline)
    val solutionReceiver = context.actorSelection("akka://functional-decomposition/user/bestSolutionActor")
    ConcurrentPartialSolution(machines, tasks, solutionReceiver).search()
  }
}

case class SearchForSolution(numberOfMachines: Int, deadline: Deadline, tasks: List[Task])