/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc

import com.lattice.lib.integration.lc.model.AccountBalance
import com.lattice.lib.integration.lc.model.LendingClubNote
import com.lattice.lib.integration.lc.model.OrderPlaced
import com.lattice.lib.integration.lc.model.Transaction

/**
 * @author ze97286
 */
trait AccountBalanceManager {
  // recalculate the account balance based on previous money transfers, owned notes, and pending orders
  def reconcileAccountBalance(transfers: Seq[Transaction], notes: Seq[LendingClubNote], pendingOrders: Seq[OrderPlaced]): Map[String, AccountBalance]

  // new money transfer or withdrawal
  def newTransaction(transaction: Transaction): AccountBalance

  // new pending investment order 
  def newPendingOrder(orderPlaced: OrderPlaced): AccountBalance

  // get the account balance
  def accountBalance(investorId: String): AccountBalance
}