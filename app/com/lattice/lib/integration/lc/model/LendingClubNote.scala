/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.model

import models.Term
import java.time.ZonedDateTime
import models.Grade

/**
 * @author ze97286
 */

case class LendingClubNote(
  loanId: Int,
  noteId: Int,
  orderId: Int,
  interestRate: Double,
  loanLength: Int,
  loanStatus: String,
  grade: String,
  loanAmount: BigDecimal,
  noteAmount: BigDecimal,
  currentPaymentStatus: Option[String],
  creditTrend: String,
  paymentsReceived: BigDecimal,
  accruedInterest: BigDecimal,
  principalPending: BigDecimal,
  interestPending: BigDecimal,
  principalReceived: BigDecimal,
  interestReceived: BigDecimal,
  nextPaymentDate: Option[ZonedDateTime],
  issueDate: Option[ZonedDateTime],
  orderDate: ZonedDateTime,
  purpose: String,
  loanStatusDate: ZonedDateTime
  ) {
 
  val gradeEnum=Grade.withName(grade)
  
  val loanLengthEnum=loanLength match {
    case 24=> Term._24
    case 36=> Term._36
    case 60=> Term._60
    case _ => throw new IllegalArgumentException("unsupported term")
  }
}
