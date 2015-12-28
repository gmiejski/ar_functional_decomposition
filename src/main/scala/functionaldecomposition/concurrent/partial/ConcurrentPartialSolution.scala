package functionaldecomposition.concurrent.partial

import akka.actor.ActorSelection
import functionaldecomposition.concurrent.bestsolution.TryUpdateMessage
import functionaldecomposition.domain.{BasicPartialSolution, Task}
import functionaldecomposition.domain.machine.Machines

/**
 * Created by grzegorz.miejski on 28/12/15.
 */
class ConcurrentPartialSolution(machines: Machines, tasks: List[Task], solutionReceiver: ActorSelection)
  extends BasicPartialSolution(machines, tasks) {

  override def reportSolution: (Machines) => Unit = {
    println("Reportins solutions!")
    machines => solutionReceiver ! TryUpdateMessage(machines)
  }

  override def createChildPartialSolution: (Machines, BasicPartialSolution) => BasicPartialSolution = {
    (machines, basicPartialSolution) => {
      ConcurrentPartialSolution(machines, basicPartialSolution.tasks.tail, solutionReceiver)
    }
  }
}

object ConcurrentPartialSolution {
  def apply(machines: Machines, tasks: List[Task], solutionReceiver: ActorSelection) = {
    new ConcurrentPartialSolution(machines, tasks, solutionReceiver)
  }
}

