/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.investor

import com.lattice.lib.utils.Log

import models.Originator

/**
 * Trait for investor persistence
 * 
 * @author ze97286
 */
trait InvestorDb extends Log {
  // get the account id corresponding to the login
  def accountId(login:String, originator:Originator.Value)
  
  // create a new investor
  def newInvestor(investorId:String, originator:Originator.Value):Int
  
  // update the investor account on a received payment
  def receivedPayment(investorId: String, originator:Originator.Value, payment:BigDecimal)
  
  // update the investor account on an investment in a loan that is pending to be issued
  def investmentPending(investorId: String, originator:Originator.Value, investedAmount:BigDecimal)
  
  // update the investor account on an issued loan for a note owned by the account 
  def loanIssued(investorId: String,originator:Originator.Value, investmentAmount:BigDecimal)
  
  // update the investor account on a loan that's gotten cancelled corresponding to a pending investment
  def loanCancelled(investorId: String, originator:Originator.Value, investmentAmount:BigDecimal)
  
  // add funds to the investor account (for a specific marketplace)
  def addFunds(investorId: String,originator:Originator.Value, amount:BigDecimal)
  
  // withdraw funds from the investor account (from a specific marketplace)
  def withdrawFunds(investorId: String,originator:Originator.Value):BigDecimal
  
  // get investor account status
  def investorAccount(investorId: String,originator:Originator.Value):InvestorAccount
}