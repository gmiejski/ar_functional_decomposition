package functionaldecomposition.domain.machine

import functionaldecomposition.BaseTestSuite
import functionaldecomposition.domain.{Deadline, Task}

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class MachinesSpec extends BaseTestSuite {

  test("printable view of machines") {

    val machines = Machines.apply(3, Deadline(10))

    machines.getMachines.apply(0).addTask(Task(10))
    machines.getMachines.apply(0).addTask(Task(20))
    machines.getMachines.apply(1).addTask(Task(1))
    machines.getMachines.apply(1).addTask(Task(2))
    machines.getMachines.apply(1).addTask(Task(3))
    machines.getMachines.apply(2).addTask(Task(30))

    machines.printableForm() should be("[10,20]\n[1,2,3]\n[30]")
  }

  test("should create new machine usage paths") {

    val machines = Machines.apply(3, Deadline(10))
    val nextTask: Task = Task(10)

    val alternatives = machines.createPossibleAlternatives(nextTask)

    alternatives.length should be(3)
    alternatives.apply(0).getMachines.apply(0).tasks should be(List(nextTask))
    alternatives.apply(1).getMachines.apply(1).tasks should be(List(nextTask))
    alternatives.apply(2).getMachines.apply(2).tasks should be(List(nextTask))

  }
}
