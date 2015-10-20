/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.model

import java.time.ZonedDateTime

import models.Grade
import models.Term

/**
 * @author ze97286
 */

case class LendingClubLoan(
    id: Int,
    memberId: Int,
    loanAmount: Double,
    fundedAmount: Double,
    term: Int,
    intRate: Double,
    expDefaultRate: Double,
    serviceFeeRate: Double,
    installment: Double,
    grade: String,
    subGrade: String,
    empLength: Int,
    homeOwnership: String,
    annualInc: Double,
    reviewStatusD: Option[ZonedDateTime],
    reviewStatus: String,
    desc: Option[String],
    purpose: String,
    addrZip: String,
    addrState: String,
    investorCount: Option[Int]) {
  val termEnum = term match {
    case 24 => Term._24
    case 36 => Term._36
    case 60 => Term._60
    case _  => throw new IllegalArgumentException("unsupported term")
  }

  val gradeEnum = Grade.withName(grade)
  val homeOwnershipEnum = HomeOwnership.withName(homeOwnership)
  val reviewStatusEnum = ReviewStatus.withName(reviewStatus)
  val purposeEnum = Purpose.withName(purpose)
}
  
