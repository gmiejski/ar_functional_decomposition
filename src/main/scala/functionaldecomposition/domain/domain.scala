package functionaldecomposition.domain

import functionaldecomposition.domain.machine.Machines

/**
 * Created by grzegorz.miejski on 26/12/15.
 */
case class Deadline(value: Int) {

}

case class Task(time: Int) {

}

object Task {
  def from(values: List[Int]): List[Task] = {
    values.map(Task(_))
  }
}

case class Solution(machines : Machines) {
  def machinesCount() = {
    machines.machinesUsed()
  }

  def processingTimeSum() = {
    machines.processingTimeSum()
  }

  def longestMachineWorkingTime() = {
    machines.longestMachineWorkingTime()
  }


}
