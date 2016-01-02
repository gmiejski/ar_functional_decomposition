package functionaldecomposition.domain

import functionaldecomposition.domain.machine.Machines

/**
 * Created by grzegorz.miejski on 28/12/15.
 */
abstract class BasicPartialSolution(machines: Machines, tasksLeft: List[Task]) extends Serializable {

  var partialSolutions: List[BasicPartialSolution] = _

  def createPartialSolutions(): List[BasicPartialSolution] = {

    val nextTask = tasksLeft.head
    val tasksLeftForChildSolutions = tasksLeft.tail

    val newMachinesStates = machines.createPossibleAlternatives(nextTask)

    newMachinesStates.map(createChildPartialSolution(_, this))
  }

  def isLeaf: Boolean = {
    tasksLeft.isEmpty
  }

  def tasks: List[Task] = {
    tasksLeft
  }

  def search(): Unit = {
    if (machines.solutionWithinDeadline()) {
      if (isLeaf) {
        reportSolution(machines)
      } else {
        partialSolutions = createPartialSolutions()
        partialSolutions.foreach(_.search())
      }
    } else {
      onExceedDeadline()
    }
  }

  def reportSolution: (Machines) => Unit

  def createChildPartialSolution: (Machines, BasicPartialSolution) => BasicPartialSolution

  def onExceedDeadline: () => Unit = {
    () => {}
  }
}
