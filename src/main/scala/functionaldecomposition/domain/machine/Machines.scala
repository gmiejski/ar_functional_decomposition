package functionaldecomposition.domain.machine

import functionaldecomposition.domain.{Task, Deadline}

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class Machines(machines: List[Machine], deadline: Deadline) extends Serializable{


  def printableForm(): String = {
    machines.map(_.toPrintableForm).mkString("--")
  }


  def createCopy(): List[Machine] = {
    machines.map(_.createCopy())
  }

  def createPossibleAlternatives(nextTask: Task): List[Machines] = {

    val listOfLists = List.fill(machines.length)(createCopy())

    val modifiedMachinesSets: List[List[Machine]] = List()
    for ((thisMachines, index) <- listOfLists.view.zipWithIndex) {
      thisMachines.apply(index).addTask(nextTask)
    }
    listOfLists.map(Machines(_, deadline))
  }

  def machinesUsed() = {
    machines.length
  }

  def processingTimeSum() = {
    machines.map(_.timeTaken).sum
  }

  def longestMachineWorkingTime() = {
    machines.map(_.timeTaken).max
  }

  def solutionWithinDeadline(): Boolean = {
    longestMachineWorkingTime() <= deadline.value
  }

  def totalCost() = {
    longestMachineWorkingTime() * machinesUsed()
  }

  def getMachines = {
    machines
  }
}

object Machines {

  def apply(machines: Int, deadline: Deadline): Machines = {
    val createdMachines: List[Machine] = List.fill(machines)(Machine())
    Machines(createdMachines, deadline)
  }

  def apply(machines: List[Machine], deadline: Deadline): Machines = {
    new Machines(machines, deadline)
  }
}


