/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.portfolio

import models.Grade
import models.Term
import java.time.LocalDate
import Portfolio._
import com.lattice.lib.integration.lc.model.LendingClubNote
import scala.math.BigDecimal.int2bigDecimal

/**
 * @author ze97286
 */

//TODO - need to think how to distinguish between 
object Portfolio {
  val InterestRateBuckets = Seq((6d, 8.99d), (9d, 11.99d), (12d, 14.99d), (15d, 17.99d), (18d, 22.99d), (23d, 99d))
}

case class Portfolio(
    principalOutstanding: BigDecimal,
    cashReceived: BigDecimal,
    interestReceived: BigDecimal,
    notesByGrade: Map[Grade.Value, Int],
    notesByState: Map[String, Int],
    notesCache: Map[Int, LendingClubNote],
    notesByStateByGrade: Map[String, Map[Grade.Value, Int]],
    principalOutstandingByGrade: Map[Grade.Value, BigDecimal],
    principalOutstandingByYield: Map[(Double, Double), BigDecimal],
    principalOutstandingByTerm: Map[Term.Value, BigDecimal],
    principalOutstandingByState: Map[String, BigDecimal],
    principalOutstandingByStateByGrade: Map[String, Map[Grade.Value, BigDecimal]],
    currentNotes: Int,
    notesByDate: Map[LocalDate, Seq[LendingClubNote]]) {

  def notesForRange(from: LocalDate, to: LocalDate): Map[LocalDate, Seq[LendingClubNote]] = notesByDate filter { case (date, _) => !date.isBefore(from) && !date.isAfter(to) }

  def notesAcquired(from: LocalDate, to: LocalDate): Map[LocalDate, Int] = notesForRange(from, to) map { case (k, v) => k -> v.size }
  def notesAcquiredByGrade(from: LocalDate, to: LocalDate): Map[LocalDate, Map[Grade.Value, Int]] = notesForRange(from, to) map { case (k, v) => k -> (v groupBy (_.gradeEnum) map { case (k1, v1) => k1 -> v1.size }) }
  def notesAcquiredByYield(from: LocalDate, to: LocalDate): Map[LocalDate, Map[(Double, Double), Int]] = {
    notesForRange(from, to) map { case (k, v) => k -> (InterestRateBuckets map { case (lower, upper) => (lower, upper) -> (v.collect { case x if x.interestRate >= lower && x.interestRate <= upper => x }).size }).toMap }
  }
  def notesAcquiredByPurpose(from: LocalDate, to: LocalDate): Map[LocalDate, Map[String, Int]] = notesForRange(from, to) map { case (k, v) => k -> (v groupBy (_.purpose) map { case (k1, v1) => k1 -> v1.size }) }
  def amountInvested(from: LocalDate, to: LocalDate): Map[LocalDate, BigDecimal] = notesForRange(from, to) map { case (k, v) => k -> (v map (_.principalPending) sum) }
  def amountInvestedByGrade(from: LocalDate, to: LocalDate): Map[LocalDate, Map[Grade.Value, BigDecimal]] = notesForRange(from, to) map { case (k, v) => k -> (v groupBy (_.gradeEnum) map { case (k1, v1) => k1 -> (v1 map (_.principalPending)).sum }) }
  def amountInvestedByYield(from: LocalDate, to: LocalDate): Map[LocalDate, Map[(Double, Double), BigDecimal]] = {
    notesForRange(from, to) map { case (k, v) => k -> (InterestRateBuckets map { case (lower, upper) => (lower, upper) -> (v.collect { case x if x.interestRate >= lower && x.interestRate <= upper => x.principalPending}).sum  }).toMap }
  }
  
  def amountInvestedByPurpose(from: LocalDate, to: LocalDate): Map[LocalDate, Map[String, BigDecimal]] = notesForRange(from, to) map { case (k, v) => k -> (v groupBy (_.purpose) map { case (k1, v1) => k1 -> (v1 map (_.principalPending)).sum }) }
}