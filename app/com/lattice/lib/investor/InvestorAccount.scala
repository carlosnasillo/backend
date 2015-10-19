/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */

package com.lattice.lib.investor

import models.Originator

/**
 * investor account per marketplace
 *
 * @author ze97286
 */
case class InvestorAccount(
  investorId: String,
  originator: Originator.Value,
  availableCash: BigDecimal,
  pendingInvestment: BigDecimal,
  outstandingPrincipal: BigDecimal,
  paidPrinicipal: BigDecimal,
  paidInterest: BigDecimal)