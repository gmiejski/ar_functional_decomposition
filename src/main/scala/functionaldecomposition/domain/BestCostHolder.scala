package functionaldecomposition.domain

import functionaldecomposition.domain.machine.Machines

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class BestCostHolder {

}

object BestCostHolder {

  def clean() = {
    currentBestCost = Int.MaxValue
    var currentBestMachines: Machines = null
  }

  var currentBestCost = Int.MaxValue
  var currentBestMachines: Machines = _

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
