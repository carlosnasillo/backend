/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */

package com.lattice.lib.integration.lc

import com.lattice.lib.integration.lc.model.LoanListing
import com.lattice.lib.integration.lc.model.OrderPlaced
import com.lattice.lib.integration.lc.model.Transaction
import com.lattice.lib.utils.Log

import play.api.libs.json.JsValue

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

  // upsert an order
  def persistOrder(orderPlaced: OrderPlaced)

  // load an order
  def loadOrders: Seq[OrderPlaced]

  // persist a transfer or withdrawal transaction
  def persistTransaction(transaction: Transaction)

  // load all transactions 
  def loadTransactions: Seq[Transaction]
}