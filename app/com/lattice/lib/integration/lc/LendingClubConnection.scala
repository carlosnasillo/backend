/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc

import com.lattice.lib.integration.lc.model.AccountSummary
import com.lattice.lib.integration.lc.model.ExecutionReport
import com.lattice.lib.integration.lc.model.LendingClubNote
import com.lattice.lib.integration.lc.model.LoanListing
import com.lattice.lib.integration.lc.model.Order
import com.lattice.lib.utils.Log
import play.api.libs.json.JsValue

/**
 * @author ze97286
 */

trait LendingClubConnection extends Log {

  // submit an order to lending club
  def submitOrder(orders: Seq[Order]): ExecutionReport

  // get a sequence of owned loans details
  def ownedNotes: Seq[LendingClubNote]

  // get a sequence of available loans
  def availableLoans: LoanListing

  // transfer funds to lattice account with Lending Club
  def transferFunds(amount: BigDecimal)

  // withdraw funds from Lending Club to lattice account
  def withdrawFunds(amount: BigDecimal)

  // get lattice account summary in LC
  def accountSummary: AccountSummary
}