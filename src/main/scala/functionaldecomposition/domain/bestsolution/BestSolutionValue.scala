package functionaldecomposition.domain.bestsolution

import functionaldecomposition.domain.Solution
import functionaldecomposition.domain.machine.Machines

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class BestSolutionValue extends Serializable {

  var currentBestCost = Int.MaxValue
  var currentBestMachines: Machines = _

  def clean() = {
    currentBestCost = Int.MaxValue
    currentBestMachines = null
  }

  def usesLessMachines(machines: Machines): Boolean = {
    machines.totalCost() == currentBestCost && this.currentBestMachines.longestMachineWorkingTime() > machines.longestMachineWorkingTime()
  }

  def shouldBeUpdated(machines: Machines): Boolean = {
    machines.totalCost() < currentBestCost || usesLessMachines(machines)
  }

  def tryUpdate(machines: Machines) = {
    if (shouldBeUpdated(machines)) {
      currentBestCost = machines.totalCost()
      this.currentBestMachines = machines
    }
  }

  def getSolution: Solution = {
    Solution(currentBestMachines)
  }

}

object BestSolutionValue {
  def apply(): BestSolutionValue = {
    new BestSolutionValue
  }

}
