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
case class OrderConfirmation(
    loanId: Int, // Unique LC assigned id for the loan. This is the same as the id loan attribute returned in the LoanListing result of the LoanBrowseLoans operation.
    requestedAmount: BigDecimal, // Amount that was requested for investment in this loan.
    investedAmount: Int, // Actual amount that was invested in this loan.
    executionStatus: Seq[String] //Indicates the status of the execution
    ) {
  val executionStatusEnum = executionStatus map (ExecutionStatus.withName(_))
}