package functionaldecomposition

import functionaldecomposition.decomposers.StandardDecomposer
import functionaldecomposition.domain.{Task, Deadline}

/**
 * Created by grzegorz.miejski on 26/12/15.
 */
object FunctionalDecomposition {


  def main(args: Array[String]) {

    val deadline: Deadline = Deadline(11)
    val tasks = List(4, 3, 2, 1, 4, 1, 5, 6, 10).map(Task(_))
    StandardDecomposer.solve(deadline, tasks)

  }
}
