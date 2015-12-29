package functionaldecomposition.domain.machine

import functionaldecomposition.domain.Task

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
class Machine(var tasks: List[Task]) extends Serializable {
  def addTask(nextTask: Task) = {
    tasks = tasks ++ List(nextTask)
  }

  def withNewTask(nextTask: Task): Machine = {
    Machine(tasks ++ List(nextTask))
  }

  def createCopy(): Machine = {
    Machine(tasks)
  }

  def timeTaken: Int = {
    tasks.map(_.time).sum
  }

  def toPrintableForm: String = {
    "[" + tasks.map(_.time).mkString(",") + "]"
  }
}

object Machine {

  def apply() = {
    new Machine(List())
  }

  def apply(tasks: List[Task]) = {
    new Machine(tasks)
  }

}


