/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */

package com.lattice.lib.integration.lc.impl

import java.time.ZonedDateTime

import scala.math.BigDecimal.int2bigDecimal

import com.lattice.lib.integration.lc.LendingClubConnection
import com.lattice.lib.integration.lc.LendingClubDb
import com.lattice.lib.integration.lc.model.Order
import com.lattice.lib.integration.lc.model.OrderPlaced
import com.lattice.lib.integration.lc.model.Transaction
import com.lattice.lib.portfolio.MarketplacePortfolioManager

import models.Originator

/**
 * TODO add logging
 * TODO error handling
 * TODO interaction with the smart contract (create/pay, etc)
 *
 *
 * @author ze97286
 */
class LendingClubPortfolioManager(db: LendingClubDb, lc: LendingClubConnection) extends MarketplacePortfolioManager {
  override val originator = Originator.LendingClub

  private val reconciler = new LendingClubReconciler(lc, db)

  /**
   * submit an order to lending club
   * If the order was successful, get the owned note, and persist it.
   * TODO sanity checks
   */
  def submitOrder(investorId: String, loanId: String, amount: BigDecimal) = {
    //TODO this needs to be transactional in the sense that a crash anywhere in the flow should be recoverable
    if (AccountBalanceManagerImpl.accountBalance(investorId).availableCash < amount) {
      throw new IllegalArgumentException("Insufficient funds")
    }

    val er = lc.submitOrder(Seq(Order(loanId.toInt, amount)))

    // analyse the order result
    val investedAmount = er.orderConfirmations.head.investedAmount
    if (investedAmount > 0) {
      val order = OrderPlaced(investorId, er.orderInstructId, loanId.toInt, None, investedAmount, ZonedDateTime.now, None, 0, "pending")
      db.persistOrder(order)
      AccountBalanceManagerImpl.newPendingOrder(order)
    } else {
      // TODO return error result
    }
  }

  override def reconcileWithMarket {
    reconciler.reconcileWithMarket
  }

  // send funds from Lattice to Lending Club
  override def transferFunds(investorId: String, amount: BigDecimal) {
    db.persistTransaction(Transaction(investorId, ZonedDateTime.now, amount))
    lc.transferFunds(amount)
  }

  // withdraw funds form Lending Club back to lattice
  override def withdrawFunds(investorId: String, amount: BigDecimal) = {
    db.persistTransaction(Transaction(investorId, ZonedDateTime.now, -amount))
    lc.withdrawFunds(amount)
  }
}