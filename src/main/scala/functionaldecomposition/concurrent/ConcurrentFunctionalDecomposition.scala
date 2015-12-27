package functionaldecomposition.concurrent

import akka.actor.{ActorSystem, Props}
import functionaldecomposition.concurrent.bestsolution.BestSolutionActor
import functionaldecomposition.domain.{Deadline, Task}

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
object ConcurrentFunctionalDecomposition {

  def main(args: Array[String]) {

    val deadline: Deadline = Deadline(11)
    val tasks = List(4, 3, 2, 1, 4, 1, 5, 6, 10).map(Task(_))

    val system = ActorSystem("functional-decomposition")

    val bestSolutionActor = system.actorOf(Props(classOf[BestSolutionActor], "arg"),"bestSolutionActor")
    println(bestSolutionActor.path)

  }
}
