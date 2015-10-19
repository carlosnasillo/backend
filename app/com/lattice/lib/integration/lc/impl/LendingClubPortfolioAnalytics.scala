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
import com.lattice.lib.portfolio.MarketplacePortfolioAnalytics

import models.Grade
import models.Originator
import models.Term

/**
 * TODO implement all
 *
 * @author ze97286
 */
class LendingClubPortfolioAnalytics(db: LendingClubDb) extends MarketplacePortfolioAnalytics {
  override val originator = Originator.LendingClub

  // how much principal is invested in the market pending to be returned
  override def principalOutstanding(investorId: String): BigDecimal = ???

  // how much is invested in loans not yet originated
  override def pendingInvestment(investorId: String): BigDecimal = ???

  // how much cash has been received from loan repaid
  override def cashReceived(investorId: String): BigDecimal = ???

  // how much interest has been received on the investor account
  override def interestReceived(investorId: String): BigDecimal = ???

  // how many notes are owned by the investor by grade
  override def notesByGrade(investorId: String): Map[Grade.Value, Int] = ???

  // how many notes are owned by the investor by the note state
  override def notesByState(investorId: String): Map[String, Int] = ???

  // how many notes are owned by the intvestor by state, for each grouped by grade
  override def notesByStateByGrade(investorId: String): Map[String, Map[Grade.Value, Int]] = ???

  // how much principal is outstanding by grade
  override def principalOutstandingByGrade(investorId: String): Map[Grade.Value, BigDecimal] = ???

  // how much principal is outstanding by yield buckets
  override def principalOutstandingByYield(investorId: String): Map[(Double, Double), BigDecimal] = ???

  // how much principal is outstanding by term 
  override def principalOutstandingByTerm(investorId: String): Map[Term.Value, BigDecimal] = ???

  // how much principal is outstanding by note state
  override def principalOutstandingByState(investorId: String): Map[String, BigDecimal] = ???

  // how much principal is outstanding by state by grade
  override def principalOutstandingByStateByGrade(investorId: String): Map[String, Map[Grade.Value, BigDecimal]] = ???

  // how many active notes are owned by the investor
  override def currentNotes(investorId: String): Int = ???

  // how many notes were acquired today
  override def notesAcquiredToday(investorId: String): Int = ???

  // how many notes were acquired today by grade
  override def notesAcquiredTodayByGrade(investorId: String): Map[Grade.Value, Int] = ???

  // how many notes were acquired today by yield buckets
  override def notesAcquiredTodayByYield(investorId: String): Map[(Double, Double), Int] = ???

  // how many notes were acquired today by purpose 
  override def notesAcquiredTodayByPurpose(investorId: String): Map[String, Int] = ???

  // how many notes were acquired in the given period
  override def notesAcquired(investorId: String, from: LocalDate, to: LocalDate) = ???

  // how many notes were acquired by the given period by Grade
  override def notesAcquiredByGrade(investorId: String, from: LocalDate, to: LocalDate): Map[LocalDate, Map[Grade.Value, Int]] = ???

  // how many notes were acquired by the given period by Yield
  override def notesAcquiredByYield(investorId: String, from: LocalDate, to: LocalDate): Map[LocalDate, Map[(Double, Double), Int]] = ???

  // how many notes were acquired by the given period by purpose
  override def notesAcquiredByPurpose(investorId: String, from: LocalDate, to: LocalDate): Map[LocalDate, Map[String, Int]] = ???

  // how much was invested in notes today
  override def amountInvestedToday(investorId: String): BigDecimal = ???

  // how much was invested in notes today by grade
  override def amountInvestedTodayByGrade(investorId: String): Map[Grade.Value, Int] = ???

  // how much was invested in notes today by yield buckets
  override def amountInvestedTodayByYield(investorId: String): Map[(Double, Double), Int] = ???

  // how much was invested in notes today by purpose 
  override def amountInvestedTodayByPurpose(investorId: String): Map[String, Int] = ???

  // how much was invested in notes in the given period
  override def amountInvested(investorId: String, from: LocalDate, to: LocalDate): BigDecimal = ???

  // how much was invested in notes in the given period by grade 
  override def amountInvestedByGrade(investorId: String, from: LocalDate, to: LocalDate): Map[LocalDate, Map[Grade.Value, BigDecimal]] = ???

  // how much was invested in notes in the given period by yield buckets
  override def amountInvestedByYield(investorId: String, from: LocalDate, to: LocalDate): Map[LocalDate, Map[(Double, Double), Long]] = ???

  // how much was invested in notes in the given period by purpose
  override def amountInvestedByPurpose(investorId: String, from: LocalDate, to: LocalDate): Map[LocalDate, Map[String, Long]] = ???

}