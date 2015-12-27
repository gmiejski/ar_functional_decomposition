package functionaldecomposition.domain.bestsolution

import functionaldecomposition.domain.machine.{Machine, Machines}
import functionaldecomposition.domain.{Deadline, Task}
import org.scalatest.{FunSuite, Matchers}

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class BestSolutionValueSpec extends FunSuite with Matchers {

  test("should choose solution with fever machine when same total cost") {
    val deadline: Deadline = Deadline(10)

    val machines: List[Machine] = List(Machine(Task.from(List(1, 1))))
    val firstSolution: Machines = Machines(machines, deadline)

    val betterMachines: List[Machine] = List(Machine(Task.from(List(1))), Machine(Task.from(List(1))))
    val secondSolution: Machines = Machines(betterMachines, deadline)

    val bestSolutionValue = BestSolutionValue()

    bestSolutionValue.tryUpdate(firstSolution)

    bestSolutionValue.usesLessMachines(secondSolution) shouldBe true
  }
}
