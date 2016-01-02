package functionaldecomposition.concurrent2.decomposers

import akka.actor.ActorRef
import functionaldecomposition.concurrent.decomposers.ConcurrentDecomposer
import functionaldecomposition.concurrent2.partial.AskingPartialSolutionActor

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class AskingConcurrentDecomposer extends ConcurrentDecomposer {

  override def createChildActors(possibleProcessesCount: List[Int]): List[ActorRef] = {
    AskingPartialSolutionActor.createActors(possibleProcessesCount.length, context)
  }
}
