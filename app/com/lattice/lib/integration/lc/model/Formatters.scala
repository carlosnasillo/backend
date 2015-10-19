/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.model

import play.api.libs.json._

/**
 * @author ze97286
 */
object Formatters {
  implicit val orderFormat = Json.format[Order]
  implicit val ordersFormat = Json.format[Orders]
  implicit val orderConfirmationFormat = Json.format[OrderConfirmation]
  implicit val executionReportFormat = Json.format[ExecutionReport]
  implicit val noteFormat = Json.format[LendingClubNote]
  implicit val notesFormat = Json.format[OwnedNotes]
  implicit val loanFormat = Json.format[LendingClubLoan]
  implicit val loanListingFormat = Json.format[LoanListing]
  implicit val accountSummaryFormat = Json.format[AccountSummary]
  implicit val noteAndContrctFormat = Json.format[NoteWrapper]
}
