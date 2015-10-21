/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.impl

import com.lattice.lib.integration.lc.AccountBalanceManager
import com.lattice.lib.integration.lc.model.AccountBalance
import com.lattice.lib.integration.lc.model.LendingClubNote
import com.lattice.lib.integration.lc.model.OrderPlaced
import com.lattice.lib.integration.lc.model.Transaction

/**
 * Keep tracks of account balance
 *
 * @author ze97286
 */
object AccountBalanceManagerImpl extends AccountBalanceManager {
  private var investorIdToAccountBalance: Map[String, AccountBalance] = Map()

  override def reconcileAccountBalance(transfers: Seq[Transaction], notes: Seq[LendingClubNote], pendingOrders: Seq[OrderPlaced]): Map[String, AccountBalance] = {
    val state = (transfers map (_.investorId)).distinct.map(x => (x, reconcileAccountBalance(x, transfers, notes, pendingOrders))).toMap
    investorIdToAccountBalance = state
    state
  }

  private[impl] def reconcileAccountBalance(investorId: String, transfers: Seq[Transaction], notes: Seq[LendingClubNote], pendingOrders: Seq[OrderPlaced]): AccountBalance = {
    val totalTransaction = (transfers map (_.amount)).sum // sigma(transferred - withdrawn)
    val totalPaymentReceived = (notes map (_.paymentsReceived)).sum // payment received from notes
    val totalInvested = (notes map (_.noteAmount)).sum // invested in notes
    val totalPendingInvestment = (pendingOrders map (_.investedAmount)).sum // invested in loans not yet initiated
    val totalPrincipalOutstanding = (notes map (_.principalPending)).sum
    val totalPrincipalReceived = (notes map (_.principalReceived)).sum
    val totalInterestReceived = (notes map (_.interestReceived)).sum

    val availableCash = totalTransaction + totalPaymentReceived - totalPendingInvestment - totalInvested

    AccountBalance(investorId, availableCash, totalInvested, totalPendingInvestment, totalPrincipalOutstanding, totalPrincipalReceived, totalInterestReceived, totalPaymentReceived)
  }

  override def newTransaction(transaction: Transaction): AccountBalance = {
    val currentBalance = investorIdToAccountBalance.getOrElse(transaction.investorId, AccountBalance.newAccount(transaction.investorId))
    val newAvailableCash = currentBalance.availableCash + transaction.amount
    val newBalance = currentBalance.copy(availableCash = newAvailableCash)
    investorIdToAccountBalance = investorIdToAccountBalance.updated(transaction.investorId, newBalance)
    newBalance
  }

  override def newPendingOrder(orderPlaced: OrderPlaced): AccountBalance = {
    val currentBalance = investorIdToAccountBalance(orderPlaced.investorId)
    val newAvailableCash = currentBalance.availableCash - orderPlaced.investedAmount
    val newPendingInvestment = currentBalance.pendingInvestment + orderPlaced.investedAmount
    val newBalance = currentBalance.copy(availableCash = newAvailableCash, pendingInvestment = newPendingInvestment)
    investorIdToAccountBalance = investorIdToAccountBalance.updated(orderPlaced.investorId, newBalance)
    newBalance
  }

  override def accountBalance(investorId: String): AccountBalance = investorIdToAccountBalance(investorId)
}