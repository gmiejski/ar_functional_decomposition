package functionaldecomposition.standard.partial

import functionaldecomposition.domain.machine.Machines
import functionaldecomposition.domain.{BasicPartialSolution, Task}
import functionaldecomposition.standard.bestsolution.BestSolutionHolder

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class StandardPartialSolution(machines: Machines, tasksLeft: List[Task]) extends BasicPartialSolution(machines, tasksLeft) {
  
  override def reportSolution: (Machines) => Unit = {
    machines => BestSolutionHolder.tryUpdate(machines)
  }

  override def createChildPartialSolution: (Machines, BasicPartialSolution) => BasicPartialSolution = {
    (machines, basicPartialSolution) => {
      StandardPartialSolution(machines, basicPartialSolution.tasks.tail)
    }
  }
}

object StandardPartialSolution {

  def apply(machines: Machines, tasksLeft: List[Task]) = {
    new StandardPartialSolution(machines, tasksLeft)
  }
}



