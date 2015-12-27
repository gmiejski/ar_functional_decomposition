package functionaldecomposition.standard.decomposers

import functionaldecomposition.domain._
import functionaldecomposition.domain.machine.Machines
import functionaldecomposition.standard.bestsolution.BestSolutionHolder

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class StandardDecomposer {

}

object StandardDecomposer {

  def solve(deadline: Deadline, tasks: List[Task]): Solution = {

    if (tasks.map(_.time).max > deadline.value) throw new RuntimeException("Max tasks time bigger than deadline!")

    val possibleProcessesCount = possibleProcessesCounts(deadline, tasks)

    findSolution(deadline, tasks, possibleProcessesCount)
  }

  def possibleProcessesCounts(deadline: Deadline, tasks: List[Task]): List[Int] = {

    val tasksSum = tasks.map(_.time).sum
    val minNumberOfProcessors = math.ceil(tasksSum / deadline.value.toDouble).toInt

    minNumberOfProcessors to tasks.length toList
  }


  def findSolution(deadline: Deadline, tasks: List[Task], possibleProcessesCount: List[Int]): Solution = {
    val sortedTasks = tasks.sortBy(_.time)(Ordering[Int].reverse)

    val startingPartialsSolutions: List[PartialSolution] = createStartingPartialSolutions(deadline, sortedTasks, possibleProcessesCount)

    startingPartialsSolutions.foreach(_.search())

    BestSolutionHolder.getSolution
  }

  def createStartingPartialSolutions(deadline: Deadline, tasks: List[Task], possibleProcessesCount: List[Int]): List[PartialSolution] = {
    possibleProcessesCount.map(Machines(_, deadline)).map(PartialSolution(_, tasks))
  }

}
