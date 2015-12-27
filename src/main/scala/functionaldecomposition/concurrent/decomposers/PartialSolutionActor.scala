package functionaldecomposition.concurrent.decomposers

import akka.actor.{ActorLogging, Actor}
import functionaldecomposition.domain.machine.Machines
import functionaldecomposition.domain.{PartialSolution, Task, Deadline}

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class PartialSolutionActor extends Actor with ActorLogging {

  def receive = {
    case SearchForSolution(numberOfMachines, deadline, tasks) => searchForPartialResult(numberOfMachines, deadline, tasks)
  }

  def searchForPartialResult(numberOfMachines: Int, deadline: Deadline, tasks: List[Task]) = {
    val machines = Machines(numberOfMachines, deadline)
    PartialSolution(machines, tasks).search()
  }

}


case class SearchForSolution(numberOfMachines: Int, deadline: Deadline, tasks: List[Task])