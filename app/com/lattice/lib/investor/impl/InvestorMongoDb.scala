/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.investor.impl

import com.lattice.lib.investor.InvestorAccount
import com.lattice.lib.investor.InvestorDb

import models.Originator
import reactivemongo.api.DefaultDB

/**
 * TODO implement
 *
 * @author ze97286
 */
class InvestorMongoDb(db: DefaultDB) extends InvestorDb {
  override def accountId(login: String, originator: Originator.Value) = ???

  override def newInvestor(investorId: String, originator: Originator.Value) = ???

  override def receivedPayment(investorId: String, originator: Originator.Value, payment: BigDecimal) = ???

  override def investmentPending(investorId: String, originator: Originator.Value, investedAmount: BigDecimal) = ???

  override def loanIssued(investorId: String, originator: Originator.Value, investmentAmount: BigDecimal) = ???

  override def loanCancelled(investorId: String, originator: Originator.Value, investmentAmount: BigDecimal) = ???

  override def addFunds(investorId: String, originator: Originator.Value, amount: BigDecimal) = ???

  override def withdrawFunds(investorId: String, originator: Originator.Value): BigDecimal = ???

  override def investorAccount(investorId: String, originator: Originator.Value): InvestorAccount = ???
}