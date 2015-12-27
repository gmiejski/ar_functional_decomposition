package functionaldecomposition.standard.decomposers

import functionaldecomposition.BaseTestSuite
import functionaldecomposition.domain.{Deadline, Solution, Task}

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class StandardDecomposerSpec extends BaseTestSuite {

  test("return correct number of possible machines count ") {

    val tasks = Task.from(List(4, 3, 2, 1, 4, 1, 5, 6, 10))

    val possibleProcessesCounts = StandardDecomposer.possibleProcessesCounts(Deadline(11), tasks)

    possibleProcessesCounts should be(List(4, 5, 6, 7, 8, 9))
  }

  test("return correct result when needs all machines to be used") {

    val tasks = Task.from(List(10, 10, 10, 10))

    val solution: Solution = StandardDecomposer.solve(Deadline(11), tasks)

    solution.processingTimeSum() should be(40)
    solution.longestMachineWorkingTime() should be(10)
    solution.machinesCount() should be(4)
  }

  test("should more machines if it doesn't increase general cost") {
    val tasks = Task.from(List(1, 1, 1, 1, 1))

    val solution: Solution = StandardDecomposer.solve(Deadline(11), tasks)

    solution.processingTimeSum() should be(5)
    solution.longestMachineWorkingTime() should be(1)
    solution.machinesCount() should be(5)
  }

  test("should not select solution which exceeds deadline") {
    val tasks = Task.from(List(5, 3))

    val solution: Solution = StandardDecomposer.solve(Deadline(6), tasks)

    solution.longestMachineWorkingTime() should be(5)
    solution.processingTimeSum() should be(8)
    solution.machinesCount() should be(2)
  }

  test("should solve problem by fully occupying machines") {
    val tasks = Task.from(List(4, 3, 2, 1, 4, 1, 5, 6, 10, 4, 2, 2))

    val solution: Solution = StandardDecomposer.solve(Deadline(11), tasks)

    solution.longestMachineWorkingTime() should be(11)
    solution.processingTimeSum() should be(44)
    solution.machinesCount() should be(4)
  }
}
