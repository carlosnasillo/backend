/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.impl

import scala.math.BigDecimal.int2bigDecimal

import com.lattice.lib.integration.lc.model.LendingClubNote
import com.lattice.lib.portfolio.Portfolio
import com.lattice.lib.portfolio.Portfolio.InterestRateBuckets

/**
 * @author ze97286
 */
object PortfolioAnalyzer {
  def analyse(notes: Seq[LendingClubNote]): Portfolio = {
    val notesCache = (notes map (x => (x.noteId -> x))).toMap
    val notesByDate = notes groupBy (_.orderDate.toLocalDate)

    val principalOutstanding = notes.map(_.principalPending).sum
    val cashReceived = notes.map(_.principalReceived).sum
    val interestReceived = notes.map(_.interestReceived).sum
    val notesByGrade = notes.groupBy(_.gradeEnum) map { case (key, value) => (key, value.size) }
    val notesByState = notes.groupBy(_.loanStatus) map { case (key, value) => (key, value.size) }
    val notesByStateByGrade = notes.groupBy(_.loanStatus) map { case (key, value) => (key -> (value.groupBy(_.gradeEnum) map { case (k, v) => (k -> v.size) })) }
    val principalOutstandingByGrade = notes.groupBy(_.gradeEnum) map { case (key, value) => (key, value.map(_.principalPending).sum) }
    val principalOutstandingByYield = (InterestRateBuckets map { case (lower, upper) => (lower, upper) -> (notes.collect { case x if x.interestRate >= lower && x.interestRate <= upper => x.principalPending } reduceLeft (_ + _)) }).toMap
    val principalOutstandingByTerm = notes.groupBy(_.loanLengthEnum) map { case (key, value) => (key, (value.map(_.principalPending).sum)) }
    val principalOutstandingByState = notes.groupBy(_.loanStatus) map { case (key, value) => (key, (value.map(_.principalPending).sum)) }
    val principalOutstandingByStateByGrade = notes.groupBy(_.loanStatus) map { case (key, value) => (key, (value groupBy (_.gradeEnum) map { case (k, v) => (k -> (v.map(_.principalPending).sum)) })) }
    val currentNotes = notes.filter(x => x.principalPending > 0).size

    Portfolio(principalOutstanding,
      cashReceived,
      interestReceived,
      notesByGrade,
      notesByState,
      notesCache,
      notesByStateByGrade,
      principalOutstandingByGrade,
      principalOutstandingByYield,
      principalOutstandingByTerm,
      principalOutstandingByState,
      principalOutstandingByStateByGrade,
      currentNotes: Int,
      notesByDate)
  }
}