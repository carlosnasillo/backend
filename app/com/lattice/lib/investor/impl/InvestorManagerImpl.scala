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
import com.lattice.lib.investor.InvestorManager
import com.lattice.lib.portfolio.MarketPlaceFactory

import models.Originator

/**
 * TODO implement
 * TODO error handling - check amount, check balance allows withdraw, etc.
 * @author ze97286
 */
class InvestorManagerImpl(db: InvestorDb) extends InvestorManager {
  // transfer funds from investor account to lattice account
  def transferFunds(investorId: String, originator: Originator.Value, amount: BigDecimal) {
    db.addFunds(investorId, originator, amount)
    MarketPlaceFactory.manager(originator).transferFunds(amount)
  }

  // transfer funds from lattice account to investor account
  def withdrawFunds(investorId: String, originator: Originator.Value, amount: BigDecimal) {
    db.withdrawFunds(investorId, originator)
    MarketPlaceFactory.manager(originator).withdrawFunds(amount)
  }

  // returns the current status of the investor account
  def status(investorId: String, originator: Originator.Value): InvestorAccount = db.investorAccount(investorId, originator)
}