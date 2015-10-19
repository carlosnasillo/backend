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

/**
 * Defines a set of analytics for marketplace lending
 * 
 * @author ze97286
 */
trait MarketplaceAnalytics extends Log {
  def originator:Originator.Value
  
  // the number of loans currently available in the marketplace
  def numLoans:Long
  
  // the total notional currently available for investment
  def liquidity:Long
  
  // number of loans currently available for investment in the market - breakdown by grade
  def numLoansByGrade:Map[Grade.Value,Long]
  
  // the total notional currently available for investment in the market - breakdown by grade 
  def liquidityByGrade:Map[Grade.Value,Long]
 
  // the change in percentage in the number of loans between days
  def dailyChangeInNumLoans:Double
  
  // the change in percentage in the available notional between days
  def dailyChangeInLiquidity:Double
  
  // the number of loans originated today
  def loanOrigination:Long=loanOrigination(LocalDate.now,LocalDate.now)(LocalDate.now)
  
  // the number of loans originated within the date range (inclusive on both ends)
  def loanOrigination(from:LocalDate, to:LocalDate):Map[LocalDate,Long]
  
  // the number of loans originated within the date range (inclusive on both ends) - breakdown by grade
  def loanOriginationByGrade(from:LocalDate, to:LocalDate):Map[LocalDate,Map[Grade.Value,Long]]
  
  // the number of loans originated today by yield 
  def loanOriginationByYield:Map[Double,Long]=loanOriginationByYield(LocalDate.now, LocalDate.now)(LocalDate.now)
  
  // the number of loans originated within the date range (inclusive on both ends) breakdown by yield
  def loanOriginationByYield(from:LocalDate, to:LocalDate):Map[LocalDate,Map[Double,Long]]
  
  // the total notional originated today
  def originatedNotional:Long = originatedNotional(LocalDate.now,LocalDate.now)(LocalDate.now)
  
  // the total notional originated within the date range (inclusive on both ends)
  def originatedNotional(from:LocalDate, to:LocalDate):Map[LocalDate,Long]
  
  // the total notional originated today - breakdown by grade
  def originatedNotionalByGrade:Map[Grade.Value,Long]=originatedNotionalByGrade(LocalDate.now,LocalDate.now)(LocalDate.now)
  
  // the total notional originated within the date range (inclusive on both ends) - breakdown by grade
  def originatedNotionalByGrade(from:LocalDate, to:LocalDate):Map[LocalDate,Map[Grade.Value,Long]]
  
  // the total notional originated today - breakdown by yield
  def originatedNotionalByYield:Map[Double,Long]=originatedNotionalByYield(LocalDate.now,LocalDate.now)(LocalDate.now)
  
  // the total notional originated within the date range (inclusive on both ends) - breakdown by yield
  def originatedNotionalByYield(from:LocalDate, to:LocalDate):Map[LocalDate,Map[Double,Long]]

}