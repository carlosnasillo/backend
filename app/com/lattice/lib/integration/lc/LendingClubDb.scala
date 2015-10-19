/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */

package com.lattice.lib.integration.lc

import com.lattice.lib.integration.lc.model.LoanListing
import com.lattice.lib.integration.lc.model.NoteWrapper
import com.lattice.lib.utils.Log

/**
 * Trait for lending club data persistence
 *
 * TODO add whatever is needed by the analytics API
 * 
 * @author ze97286
 */
trait LendingClubDb extends Log {
  // persist loan listing to lattice database
  def persistLoans(availableLoans: LoanListing): Unit

  // load currently available loans from lattice database
  def availableLoans: LoanListing

  // load a note by order id
  def loadNote(orderId: Int, loanId: Int): NoteWrapper

  // update a note
  def updateNote(note: NoteWrapper): Unit

  // insert a note
  def persistNote(note: NoteWrapper): Unit
  
}