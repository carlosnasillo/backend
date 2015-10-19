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
object ExecutionStatus extends Enumeration {
  type ExecutionStatus = Value
  val ORDER_FULFILLED, // Order fulfilled as submitted. Invested amount is equal the requested amount.
  LOAN_AMNT_EXCEEDED, // Order fulfilled up to the remaining loan funding amount.
  NOT_AN_INFUNDING_LOAN, // Order ignored, loan does not exist or is not in funding.
  REQUESTED_AMNT_LOW, // Order ignored. For normal loans: Requested amount is smaller than $25 and is or becomes $0 when rounded. For whole loans: Requested amount is less that the entire loan amount.
  REQUESTED_AMNT_ROUNDED, // Order fulfilled up to the highest $25 multiple smaller than requested amount. This happens when the requested amount is not a multiple of $25.
  AUGMENTED_BY_MERGE, // Order augmented by merging other orders in the instruct that were targeting the same loan. Since other restrictions may apply after merging the presence of other status should be checked and the invested amount will not necessarily be the total requested for the loan.
  ELIM_BY_MERGE, // Order ignored and merged into a previous order in the same instruct. This happens when there are more than one orders for the same loan.
  INSUFFICIENT_CASH, // Order was ignored since there was not enough cash available.
  NOT_AN_INVESTOR, // Order was ignored since user credentials do not match an investor.
  NOT_A_VALID_INVESTMENT, // Order ignored. User does not have the required permissions to invest or because his previous order was still being processed.
  NOTE_ADDED_TO_PORTFOLIO, // This indicates that the successful order was assigned to the portfolioId passed in the request. This status does not indicate the amount invested.
  NOT_A_VALID_PORTFOLIO, // This indicates that the order was not assigned to the indicated portfolioId from the request.
  ERROR_ADDING_NOTE_TO_PORTFOLIO, // This indicates that there was an error adding the note to requested portfolio.
  SYSTEM_BUSY, // Order was ignored due to other system activity.
  UNKNOWN_ERROR = Value //Order was ignored due to an unknown error.
}