/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.impl

import java.time.LocalDate

import com.lattice.lib.integration.lc.LendingClubDb
import com.lattice.lib.portfolio.MarketplaceAnalytics

import models.Grade
import models.Originator

/**
 * Implementation for LendingClub of the Market
 * Load marketplace analytics from db
 *
 * TODO implement
 * @author ze97286
 */
class LendingClubAnalytics(db: LendingClubDb) extends MarketplaceAnalytics {
  override val originator = Originator.LendingClub

  // read the latest doc from loans and return the count of loans
  override def numLoans: Long = ???

  // read the latest doc from loans and return the sum of available notional
  override def liquidity: Long = ???

  // read the latest doc from loans partition by grade, count
  override def numLoansByGrade: Map[Grade.Value, Long] = ???

  // read the latest doc from loans partition by grade, sum
  override def liquidityByGrade: Map[Grade.Value, Long] = ???

  // read the latest doc from loans for today and yesterday, diff in count
  override def dailyChangeInNumLoans: Double = ???

  // read the latest doc from loans for today and yesterday, diff in sum
  override def dailyChangeInLiquidity: Double = ???

  // read the latest doc from loans for each of the days in the range and for each return the number of loans *originated* on this day
  override def loanOrigination(from: LocalDate, to: LocalDate): Map[LocalDate, Long] = ???

  // read the latest doc from loans for each of the days in the range and for each return the number of loans  *originated* on this day partition by grade
  override def loanOriginationByGrade(from: LocalDate, to: LocalDate): Map[LocalDate, Map[Grade.Value, Long]] = ???

  // read the latest doc from loans for each of the days in the range and for each return the number of loans  *originated* on this day partition by yield
  override def loanOriginationByYield(from: LocalDate, to: LocalDate): Map[LocalDate, Map[Double, Long]] = ???

  // read the latest doc from loans for each of the days in the range and for each return the sum of requested cash  *originated* on this day
  override def originatedNotional(from: LocalDate, to: LocalDate): Map[LocalDate, Long] = ???

  // read the latest doc from loans for each of the days in the range and for each return the sum of requested cash  *originated* on this day partition by grade
  override def originatedNotionalByGrade(from: LocalDate, to: LocalDate): Map[LocalDate, Map[Grade.Value, Long]] = ???

  // read the latest doc from loans for each of the days in the range and for each return the sum of requested cash  *originated* on this day partition by yield
  override def originatedNotionalByYield(from: LocalDate, to: LocalDate): Map[LocalDate, Map[Double, Long]] = ???
}