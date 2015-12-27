package functionaldecomposition

import functionaldecomposition.domain.BestCostHolder
import org.scalatest.{Matchers, BeforeAndAfter, FunSuite}

/**
 * Created by grzegorz.miejski on 27/12/15.
 */
trait BaseTestSuite extends FunSuite
with BeforeAndAfter
with Matchers {

  before {
    BestCostHolder.clean()
  }

}
