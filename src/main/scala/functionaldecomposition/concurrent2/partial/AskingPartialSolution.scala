package functionaldecomposition.concurrent2.partial

import functionaldecomposition.domain.Task
import functionaldecomposition.domain.machine.Machines

/**
 * Created by grzegorz.miejski on 28/12/15.
 */
class AskingPartialSolution(machines: Machines,
                            tasksLeft: List[Task]) {

  def getChildSolutions(): List[AskingPartialSolution] = {
    if (tasksLeft.isEmpty) List() else createPartialSolutions()
  }

  def createPartialSolutions(): List[AskingPartialSolution] = {
    val nextTask = tasksLeft.head

    val newMachinesStates = machines.createPossibleAlternatives(nextTask)

    val childPartialSolution: List[AskingPartialSolution] = newMachinesStates.map(createChildPartialSolution(_, this))
    childPartialSolution
  }

  def isLeaf: Boolean = {
    tasksLeft.isEmpty
  }

  def tasks: List[Task] = {
    tasksLeft
  }

  def getMachines: Machines = {
    machines
  }

  def exceedsDeadline(): Boolean = {
    !machines.solutionWithinDeadline()
  }

  def createChildPartialSolution: (Machines, AskingPartialSolution) => AskingPartialSolution = {
    (machines, basicPartialSolution) => {
      AskingPartialSolution(machines, basicPartialSolution.tasks.tail)
    }
  }
}

object AskingPartialSolution {
  def apply(machines: Machines,
            tasksLeft: List[Task]) = {
    new AskingPartialSolution(machines, tasksLeft)
  }
}

