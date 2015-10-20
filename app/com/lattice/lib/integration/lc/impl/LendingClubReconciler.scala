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
import com.lattice.lib.integration.lc.model.LendingClubNote
import com.lattice.lib.integration.lc.model.NoteWrapper
import com.lattice.lib.investor.InvestorDb
import com.lattice.lib.utils.Log
import models.Originator
import play.api.libs.json.JsValue

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
  investorDb: InvestorDb, // access to investor database
  lc: LendingClubConnection, // access to lending club api
  db: LendingClubDb) // access to lending club database
    extends Log {

  /**
   * persist current available loans from lending club
   */
  def reconcileAvailableLoans {
    log.info("reconciling available loans")
    val availableLoans = lc.availableLoans
//    db.persistLoans(availableLoans)
    calculateLoanAnalytics(availableLoans)
  }
  
  def calculateLoanAnalytics(loanListing:JsValue) {
    //TODO 
  }

  /**
   * read available notes from lending club, for each note, check if its status has changed to trigger any cash flows
   */
  def reconcileOwnedNotes {
    log.info("reconciling available notes")

    // load the owned notes from LC
    val ownedNotes = lc.ownedNotes
    log.info(s"owned notes:\n ${ownedNotes mkString "\n"}")

    // for each note analyse the state changes
    ownedNotes foreach (x => analyseNote(x))
  }

  /**
   *  analyse changes in note lifecycle
   */
  private[lc] def analyseNote(note: LendingClubNote) {
    // load the note from db
    val dbNoteWrapper = db.loadNote(note.orderId, note.loanId)
    val dbNote = dbNoteWrapper.note

    // check if the loan status has changed 
    val loanStatus = note.loanStatus
    var contractAddress = dbNoteWrapper.contract
    if (loanStatus != dbNote.loanStatus) {
      if (loanStatus == "Expired" || loanStatus == "Removed" || loanStatus == "Withdrawn by Applicant") {
        // update balance - refund pending principal to available cash
        investorDb.loanCancelled(dbNoteWrapper.investorId, Originator.LendingClub, dbNote.principalPending)
      } else if (loanStatus == "Issued") {
        // need to setup the smart contract
        // contractAddress="" //TODO get contract address

        // update balance - move cash from pending to outstanding
        investorDb.loanIssued(dbNoteWrapper.investorId, Originator.LendingClub, dbNote.principalPending)
      }
    }
    if (note.paymentsReceived > dbNote.paymentsReceived) {
      // need to send payment to the smart contract
      val paid = note.paymentsReceived - dbNote.paymentsReceived
      // send payment to contract
      // update account - decrease outstanding principal, increase cash
      investorDb.receivedPayment(dbNoteWrapper.investorId, Originator.LendingClub, paid)
    }

    db.updateNote(NoteWrapper(note, dbNoteWrapper.investorId, contractAddress))

  }

}

