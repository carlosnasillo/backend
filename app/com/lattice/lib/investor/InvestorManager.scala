/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.investor

import models.Originator

/**
 * @author ze97286
 */
trait InvestorManager {
  // transfer funds from investor account to lattice account
  def transferFunds(investorId: String, originator:Originator.Value, amount:BigDecimal)
  
  // transfer funds from lattice account to investor account
  def withdrawFunds(investorId: String, originator:Originator.Value, amount:BigDecimal)
  
  // returns the current status of the investor account
  def status(investorId: String, originator:Originator.Value):InvestorAccount
}