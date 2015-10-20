/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.portfolio

import java.time.LocalDate

import com.lattice.lib.utils.Log

import models.Grade
import models.Originator
import models.Term

/**
 * Defines marketplace analytics for an investor portfolio
 *
 * @author ze97286
 */
trait MarketplacePortfolioAnalytics extends Log {
  def originator: Originator.Value

  // how much principal is invested in the market pending to be returned
  def principalOutstanding(investorId: String): BigDecimal

  // how much is invested in loans not yet originated
  def pendingInvestment(investorId: String): BigDecimal

  // how much cash has been received from loan repaid
  def cashReceived(investorId: String): BigDecimal

  // how much interest has been received on the investor account
  def interestReceived(investorId: String): BigDecimal

  // how many notes are owned by the investor by grade
  def notesByGrade(investorId: String): Map[Grade.Value, Int]

  // how many notes are owned by the investor by the note state
  def notesByState(investorId: String): Map[String, Int]

  // how many notes are owned by the intvestor by state, for each grouped by grade
  def notesByStateByGrade(investorId: String): Map[String, Map[Grade.Value, Int]]

  // how much principal is outstanding by grade
  def principalOutstandingByGrade(investorId: String): Map[Grade.Value, BigDecimal]

  // how much principal is outstanding by yield buckets
  def principalOutstandingByYield(investorId: String): Map[(Double, Double), BigDecimal]

  // how much principal is outstanding by term 
  def principalOutstandingByTerm(investorId: String): Map[Term.Value, BigDecimal]

  // how much principal is outstanding by note state
  def principalOutstandingByState(investorId: String): Map[String, BigDecimal]

  // how much principal is outstanding by state by grade
  def principalOutstandingByStateByGrade(investorId: String): Map[String, Map[Grade.Value, BigDecimal]]

  // how many active notes are owned by the investor
  def currentNotes(investorId: String): Int

  // how many notes were acquired today
  def notesAcquiredToday(investorId: String): Int

  // how many notes were acquired today by grade
  def notesAcquiredTodayByGrade(investorId: String): Map[Grade.Value, Int]

  // how many notes were acquired today by yield buckets
  def notesAcquiredTodayByYield(investorId: String): Map[(Double, Double), Int]

  // how many notes were acquired today by purpose 
  def notesAcquiredTodayByPurpose(investorId: String): Map[String, Int]

  // how many notes were acquired in the given period
  def notesAcquired(investorId: String, from: LocalDate, to: LocalDate):Map[LocalDate,Int] 

  // how many notes were acquired by the given period by Grade
  def notesAcquiredByGrade(investorId: String, from: LocalDate, to: LocalDate): Map[LocalDate, Map[Grade.Value, Int]]

  // how many notes were acquired by the given period by Yield
  def notesAcquiredByYield(investorId: String, from: LocalDate, to: LocalDate): Map[LocalDate, Map[(Double, Double), Int]]

  // how many notes were acquired by the given period by purpose
  def notesAcquiredByPurpose(investorId: String, from: LocalDate, to: LocalDate): Map[LocalDate, Map[String, Int]]

  // how much was invested in notes today
  def amountInvestedToday(investorId: String): BigDecimal

  // how much was invested in notes today by grade
  def amountInvestedTodayByGrade(investorId: String): Map[Grade.Value, BigDecimal]

  // how much was invested in notes today by yield buckets
  def amountInvestedTodayByYield(investorId: String): Map[(Double, Double), BigDecimal]

  // how much was invested in notes today by purpose 
  def amountInvestedTodayByPurpose(investorId: String): Map[String, BigDecimal]

  // how much was invested in notes in the given period
  def amountInvested(investorId: String, from: LocalDate, to: LocalDate):  Map[LocalDate,BigDecimal]

  // how much was invested in notes in the given period by grade 
  def amountInvestedByGrade(investorId: String, from: LocalDate, to: LocalDate): Map[LocalDate, Map[Grade.Value, BigDecimal]]

  // how much was invested in notes in the given period by yield buckets
  def amountInvestedByYield(investorId: String, from: LocalDate, to: LocalDate): Map[LocalDate, Map[(Double, Double), BigDecimal]]

  // how much was invested in notes in the given period by purpose
  def amountInvestedByPurpose(investorId: String, from: LocalDate, to: LocalDate): Map[LocalDate, Map[String, BigDecimal]]

}