/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.portfolio

import models.Grade
import java.time.LocalDate
import models.Originator
import com.lattice.lib.utils.Log

import scala.concurrent.{ExecutionContext, Future}

/**
 * Defines a set of analytics for marketplace lending
 * 
 * @author ze97286
 */
trait MarketplaceAnalytics extends Log {
  implicit val ec = ExecutionContext.Implicits.global

  def originator:Originator.Value
  
  // the number of loans currently available in the marketplace
  def numLoans: Future[Int]
  
  // the total notional currently available for investment
  def liquidity: Future[BigDecimal]
  
  // number of loans currently available for investment in the market - breakdown by grade
  def numLoansByGrade: Future[Map[Grade.Value,Int]]
  
  // the total notional currently available for investment in the market - breakdown by grade 
  def liquidityByGrade: Future[Map[Grade.Value,BigDecimal]]
 
  // the change in percentage in the number of loans between days
  def dailyChangeInNumLoans: Future[Int]
  
  // the change in percentage in the available notional between days
  def dailyChangeInLiquidity: Future[BigDecimal]
  
  // the number of loans originated today
  def loanOrigination: Future[Long] = loanOrigination(LocalDate.now, LocalDate.now).map(_.head._2)
  
  // the number of loans originated within the date range (inclusive on both ends)
  def loanOrigination(from:LocalDate, to:LocalDate): Future[Map[LocalDate,Long]]
  
  // the number of loans originated within the date range (inclusive on both ends) - breakdown by grade
  def loanOriginationByGrade(from:LocalDate, to:LocalDate): Future[Map[LocalDate,Map[Grade.Value,Long]]]
  
  // the number of loans originated today by yield 
  def loanOriginationByYield: Future[Map[Double,Long]] = loanOriginationByYield(LocalDate.now, LocalDate.now).map(_.head._2)
  
  // the number of loans originated within the date range (inclusive on both ends) breakdown by yield
  def loanOriginationByYield(from:LocalDate, to:LocalDate): Future[Map[LocalDate,Map[Double,Long]]]
  
  // the total notional originated today
  def originatedNotional: Future[Long] = originatedNotional(LocalDate.now,LocalDate.now).map(_.head._2)
  
  // the total notional originated within the date range (inclusive on both ends)
  def originatedNotional(from:LocalDate, to:LocalDate): Future[Map[LocalDate,Long]]
  
  // the total notional originated today - breakdown by grade
  def originatedNotionalByGrade: Future[Map[Grade.Value,Long]] = originatedNotionalByGrade(LocalDate.now,LocalDate.now).map(_.head._2)
  
  // the total notional originated within the date range (inclusive on both ends) - breakdown by grade
  def originatedNotionalByGrade(from:LocalDate, to:LocalDate): Future[Map[LocalDate,Map[Grade.Value,Long]]]
  
  // the total notional originated today - breakdown by yield
  def originatedNotionalByYield: Future[Map[Double,Long]] = originatedNotionalByYield(LocalDate.now,LocalDate.now).map(_.head._2)
  
  // the total notional originated within the date range (inclusive on both ends) - breakdown by yield
  def originatedNotionalByYield(from:LocalDate, to:LocalDate): Future[Map[LocalDate,Map[Double,Long]]]

}