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
import com.lattice.lib.integration.lc.LendingClubFactory
import com.lattice.lib.integration.lc.model.Formatters.loanListingFormat
import com.lattice.lib.integration.lc.model.LendingClubLoan
import com.lattice.lib.integration.lc.model.LendingClubNote
import com.lattice.lib.integration.lc.model.LoanListing
import com.lattice.lib.integration.lc.model.OrderPlaced
import com.lattice.lib.utils.Log

import play.api.libs.json.JsValue
import play.api.libs.json.Json

/**
 * The reconciler is run periodically and reconciles the loans, notes, and accounts database with the state in lending club
 *
 * TODO add logging
 * TODO add error handling
 * TODO verify note analysis logic + add handling for various states - e.g late
 * TODO add contract interaction
 * @author ze97286
 */
class LendingClubReconciler(
  lc: LendingClubConnection, // access to lending club api
  db: LendingClubDb) // access to lending club database
    extends Log {

  def reconcileWithMarket {
    val availableLoans = lc.availableLoans
    val ownedNotes = lc.ownedNotes
    val placedOrders = db.loadOrders
    reconcileAvailableLoans(availableLoans.loans)
    reconcileOwnedNotes(ownedNotes, availableLoans.loans, placedOrders)
  }

  /**
   * persist current available loans from lending club
   */
  private[impl] def reconcileAvailableLoans(availableLoans: Seq[LendingClubLoan]) {
    log.info("reconciling available loans")
    val availableLoans = lc.availableLoans
    db.persistLoans(availableLoans)
    calculateLoanAnalytics(availableLoans)
  }

  //TODO Julien - use this to calculate the analytics and persist the result to db
  private[impl] def calculateLoanAnalytics(loanListing: LoanListing) {
    //TODO
  }

  /**
   * read available notes from lending club, for each note, check if its status has changed to trigger any cash flows
   */
  private[impl] def reconcileOwnedNotes(ownedNotes: Seq[LendingClubNote], loans: Seq[LendingClubLoan], placedOrders: Seq[OrderPlaced]) {
    log.info("reconciling available notes")

    log.info(s"placed orders:\n ${placedOrders mkString "\n"}")

    // load the owned notes from LC

    log.info(s"owned notes:\n ${ownedNotes mkString "\n"}")

    val orderId2Order = placedOrders map (x => (x.orderId -> x)) toMap
    val ordersId2InvestorId = placedOrders map (x => (x.orderId -> x.investorId)) toMap
    val ownedNotesByInvestor = ownedNotes.groupBy(x => ordersId2InvestorId(x.orderId))

    val portfolios = ownedNotesByInvestor map { case (k, v) => (k, PortfolioAnalyzer.analyse(v)) }

    LendingClubFactory.portfolio.resetPortfolios(portfolios)

    val notesToOrder = ownedNotes map (x => (x -> orderId2Order(x.orderId)))

    notesToOrder foreach (x => analyseNote(x._1, x._2))

    val orderIdToNote = (ownedNotes map (x => x.orderId -> x)).toMap

    val unusedOrders = placedOrders filter (x => !orderIdToNote.contains(x.orderId))
    val (pendingOrders, unissuedOrders) = analyseInactiveOrders(placedOrders, loans)

    unissuedOrders foreach (x => {
      db.persistOrder(x.copy(loanStatus = "Not Issued"))
    })

    val transfers = db.loadTransactions

    AccountBalanceManagerImpl.reconcileAccountBalance(transfers, ownedNotes, pendingOrders)
  }

  /**
   *  analyse changes in note lifecycle
   */
  private[lc] def analyseNote(note: LendingClubNote, order: OrderPlaced) {
    order.contractAddress match {
      case None =>
        val address = createLoanContract
        db.persistOrder(order.copy(loanStatus = note.loanStatus, noteId = Some(note.noteId), contractAddress = Some(address)))
      case Some(address) if (order.paymentsReceived != note.paymentsReceived) =>
        val paid = note.paymentsReceived - order.paymentsReceived
        db.persistOrder(order.copy(paymentsReceived = note.paymentsReceived))
      // send payment to the smart contract
      //       case Some(address) if (note.loanStatus=="pending")
    }
  }

  private[lc] def analyseInactiveOrders(order: Seq[OrderPlaced], loans: Seq[LendingClubLoan]) = {
    val loanIdToLoan = (loans.map(x => (x.id -> x))).toMap
    order.partition { x => loanIdToLoan.contains(x.loanId) }
  }

  //TODO create the contract on bc and return its address
  def createLoanContract: String = ""

}