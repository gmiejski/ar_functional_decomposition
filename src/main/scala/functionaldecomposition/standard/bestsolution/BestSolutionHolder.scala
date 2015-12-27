package functionaldecomposition.standard.bestsolution

import functionaldecomposition.domain.Solution
import functionaldecomposition.domain.bestsolution.BestSolutionValue
import functionaldecomposition.domain.machine.Machines

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class BestSolutionHolder {

}

object BestSolutionHolder {

  var bestSolutionValue : BestSolutionValue = BestSolutionValue()
  
  def clean() = {
    bestSolutionValue = BestSolutionValue()
  }

  def tryUpdate(machines: Machines) = {
    bestSolutionValue.tryUpdate(machines)
  }

  def currentBestCost = {
    bestSolutionValue.currentBestCost
  }

  def getSolution: Solution = {
    bestSolutionValue.getSolution
  }
}
