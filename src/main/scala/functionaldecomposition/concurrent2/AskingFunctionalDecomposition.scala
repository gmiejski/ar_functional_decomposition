package functionaldecomposition.concurrent2

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import functionaldecomposition.concurrent.bestsolution.BestSolutionActor
import functionaldecomposition.concurrent.decomposers.Solve
import functionaldecomposition.concurrent2.decomposers.AskingConcurrentDecomposer
import functionaldecomposition.domain.{Deadline, Task}

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
object AskingFunctionalDecomposition {

  def main(args: Array[String]) {

    val deadline: Deadline = Deadline(11)
    val tasks = List(4, 3, 2, 1, 4, 1, 5, 6, 10).map(Task(_))
//    val tasks = List(4, 3, 2, 4, 3, 2, 1, 5).map(Task(_))

    val system = ActorSystem("functional-decomposition", ConfigFactory.load("application"))

    val bestSolutionActor = system.actorOf(Props(classOf[BestSolutionActor], "arg"), "bestSolutionActor")
    val concurrentDecomposer = system.actorOf(Props(classOf[AskingConcurrentDecomposer]), "concurrentDecomposerActor")

    println(bestSolutionActor.path)
    println(concurrentDecomposer.path)

    concurrentDecomposer ! Solve(deadline, tasks)
  }
}
