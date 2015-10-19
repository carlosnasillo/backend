package models

/**
 * @author ze97286
 */

object Originator extends Enumeration {
  type Originator = Value
  val Prosper, LendingClub = Value
}

case class Loan(id: String, originator: Originator.Value, requested:Double, outstanding:Double)