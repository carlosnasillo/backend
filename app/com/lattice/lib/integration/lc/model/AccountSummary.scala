/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */

package com.lattice.lib.integration.lc.model

/**
 * @author ze97286
 */
case class AccountSummary(
  investorId: Int,
  availableCash: BigDecimal,
  accountTotal: BigDecimal,
  accruedInterest: BigDecimal,
  infundingBalance: BigDecimal,
  receivedInterest: BigDecimal,
  receivedPrincipal: BigDecimal,
  receivedLateFees: BigDecimal,
  outstandingPrincipal: BigDecimal,
  totalNotes: Int,
  totalPortfolios: Int)