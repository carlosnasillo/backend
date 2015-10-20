/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */

package com.lattice.lib.integration.lc.impl

import com.lattice.lib.integration.lc.LendingClubConnection
import com.lattice.lib.integration.lc.LendingClubDb
import com.lattice.lib.integration.lc.model.NoteWrapper
import com.lattice.lib.integration.lc.model.Order
import com.lattice.lib.investor.InvestorDb
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
class LendingClubPortfolioManager(investorDb: InvestorDb, db: LendingClubDb, lc: LendingClubConnection) extends MarketplacePortfolioManager {
  override val originator = Originator.LendingClub

  private val reconciler = new LendingClubReconciler(investorDb, lc, db)

  /**
   * submit an order to lending club
   * If the order was successful, get the owned note, and persist it.
   * TODO sanity checks
   */
  def submitOrder(investorId: String, loanId: String, amount: BigDecimal) = {
    // need to check that the investor id has sufficient funds in lattice 
    // load investor details from db and check if its current fund > amount
    if (investorDb.investorAccount(investorId, originator).availableCash < amount) {
      throw new Exception("insufficient funds")
    }

    val er = lc.submitOrder(Seq(Order(loanId.toInt, amount)))

    // analyse the order result
    val investedAmount = er.orderConfirmations.head.investedAmount
    if (investedAmount > 0) {
      val noteOpt = (lc.ownedNotes filter (x => (x.loanId == loanId) && (x.orderId == er.orderInstructId))).headOption

      // TODO sanity - check that the invested amount matches the note
      
      // persist the note to db
      noteOpt match {
        case None       => // should not happen TODO handle as error
        case Some(note) => db.persistNote(NoteWrapper(note, investorId, ""))
         // investmentPending
      }
    } else {
      // TODO return error result
    }
  }

  // reconcile available loans from the originator
  override def reconcileAvailableLoans {
    reconciler.reconcileAvailableLoans
  }

  // reconcile owned notes for investors in the originator
  override def reconcileOwnedNotes {
    reconciler.reconcileOwnedNotes
  }

  // send funds from Lattice to Lending Club
  override def transferFunds(amount: BigDecimal) {
    lc.transferFunds(amount)
  }

  // withdraw funds form Lending Club back to lattice
  override def withdrawFunds(amount: BigDecimal) = {
    lc.withdrawFunds(amount)
  }
}