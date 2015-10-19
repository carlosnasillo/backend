/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.portfolio

import models.Originator
import com.lattice.lib.utils.Log

/**
 * manage interaction with marketplace
 * 
 * @author ze97286
 */
trait MarketplacePortfolioManager extends Log {
  def originator: Originator.Value

  // submit an order to the marketplace
  def submitOrder(investorId: String, loanId: String, amount: BigDecimal)

  // transfer funds from lattice to marketplace - this needs to be called when money is transferred by an investor to be invested in a marketplace
  def transferFunds(amount: BigDecimal)

  // withdraw funds from marketplace to lattice - this needs to be called when investor wants to withdraw money from a marketplace
  def withdrawFunds(amount: BigDecimal)

  // reconcile available loans from the originator
  def reconcileAvailableLoans: Unit

  // reconcile owned notes for investors in the originator
  def reconcileOwnedNotes: Unit
}