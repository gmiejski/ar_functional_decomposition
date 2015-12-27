package functionaldecomposition.concurrent

import akka.actor.Actor
import functionaldecomposition.domain.bestsolution.BestSolutionValue
import functionaldecomposition.domain.machine.Machines

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class BestSolutionActor extends Actor {

  var bestSolutionValue: BestSolutionValue = BestSolutionValue()

  def receive = {
    case TryUpdateMessage(m) =>

  }

}

case class TryUpdateMessage(machines: Machines)
