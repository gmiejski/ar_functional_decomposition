package functionaldecomposition.domain

import functionaldecomposition.BaseTestSuite
import functionaldecomposition.domain.machine.{Machine, Machines}

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class BestCostHolderSpec extends BaseTestSuite {

  test("should choose solution with fever machine when same total cost") {
    val deadline: Deadline = Deadline(10)

    val machines: List[Machine] = List(Machine(Task.from(List(1, 1))))
    val firstSolution: Machines = Machines(machines, deadline)

    val betterMachines: List[Machine] = List(Machine(Task.from(List(1))), Machine(Task.from(List(1))))
    val secondSolution: Machines = Machines(betterMachines, deadline)


    BestCostHolder.tryUpdate(firstSolution)

    BestCostHolder.usesLessMachines(secondSolution) shouldBe true
  }
}
