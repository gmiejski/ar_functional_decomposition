package functionaldecomposition.standard

import functionaldecomposition.domain.{Solution, Deadline, Task}
import functionaldecomposition.standard.decomposers.StandardDecomposer

/**
 * Created by grzegorz.miejski on 26/12/15.
 */
object FunctionalDecomposition {

  def main(args: Array[String]) {

    val deadline: Deadline = Deadline(11)
    val tasks = List(4, 3, 2, 4, 3, 2, 1).map(Task(_))
    val start = System.currentTimeMillis()
    val solution: Solution = StandardDecomposer.solve(deadline, tasks)

    val totalTime = (System.currentTimeMillis() - start) / 1000
    println(s"Final solution after $totalTime seconds:")
    println(solution.machines.printableForm())
    println(s"Cost: ${solution.machines.totalCost()}")
  }
}
