package functionaldecomposition.concurrent.partial

import akka.actor.ActorSelection
import functionaldecomposition.concurrent.bestsolution.TryUpdateMessage
import functionaldecomposition.domain.Task
import functionaldecomposition.domain.machine.Machines

/**
 * Created by grzegorz.miejski on 28/12/15.
 */
class ConcurrentPartialSolution(machines: Machines,
                                tasksLeft: List[Task],
                                solutionReceiver: ActorSelection) {
  var partialSolutions: List[ConcurrentPartialSolution] = _


  def getChildSolutions(): List[ConcurrentPartialSolution] = {
    if (tasksLeft.isEmpty) List() else createPartialSolutions()
  }

  def createPartialSolutions(): List[ConcurrentPartialSolution] = {
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

  def getMachines: Machines = {
    machines
  }

  def exceedsDeadline(): Boolean = {
    !machines.solutionWithinDeadline()
  }

  //  def search(): Unit = {
  //    if (machines.solutionWithinDeadline()) {
  //      if (isLeaf) {
  //        reportSolution(machines)
  //      } else {
  //        partialSolutions = createPartialSolutions()
  //        partialSolutions.foreach(_.search())
  //      }
  //    } else {
  //      onExceedDeadline()
  //    }
  //  }

  def reportSolution: (Machines) => Unit = {
    println("Reportins solutions!")
    machines => solutionReceiver ! TryUpdateMessage(machines)
  }

  def createChildPartialSolution: (Machines, ConcurrentPartialSolution) => ConcurrentPartialSolution = {
    (machines, basicPartialSolution) => {
      ConcurrentPartialSolution(machines, basicPartialSolution.tasks.tail, solutionReceiver)
    }
  }
}

object ConcurrentPartialSolution {
  def apply(machines: Machines,
            tasksLeft: List[Task],
            solutionReceiver: ActorSelection) = {
    new ConcurrentPartialSolution(machines, tasksLeft, solutionReceiver)
  }
}

