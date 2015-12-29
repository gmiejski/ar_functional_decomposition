package functionaldecomposition.domain

/**
 * Created by grzegorz.miejski on 28/12/15.
 */
class ChildActorsTracker(childProcessesCount: Int) {
  var finishedChildProcesses = 0


  def registerChildProcessEnded() = {
    finishedChildProcesses += 1
    if (finishedChildProcesses > childProcessesCount) {
      throw new RuntimeException("Received more child finished messages than should!")
    }
  }

  def isCompleted(): Boolean = {
    finishedChildProcesses == childProcessesCount
  }

  def getMessage(): String = {
    s"Completed ($finishedChildProcesses / $childProcessesCount) partial solutions"
  }
}

object ChildActorsTracker {
  def apply(childProcessesCount: Int) = {
    new ChildActorsTracker(childProcessesCount)
  }
}
