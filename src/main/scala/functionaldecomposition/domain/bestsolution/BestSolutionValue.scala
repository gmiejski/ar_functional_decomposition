package functionaldecomposition.domain.bestsolution

import functionaldecomposition.domain.Solution
import functionaldecomposition.domain.machine.Machines

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class BestSolutionValue {

  var currentBestCost = Int.MaxValue
  var currentBestMachines: Machines = _

  def clean() = {
    currentBestCost = Int.MaxValue
    currentBestMachines = null
  }

  def usesLessMachines(machines: Machines): Boolean = {
    machines.totalCost() == currentBestCost && this.currentBestMachines.longestMachineWorkingTime() > machines.longestMachineWorkingTime()
  }

  def tryUpdate(machines: Machines) = {
    if (machines.totalCost() < currentBestCost || usesLessMachines(machines)) {
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
