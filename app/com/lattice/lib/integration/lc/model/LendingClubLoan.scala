/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.model

import models.Term
import models.Grade
import java.time.ZonedDateTime

/**
 * @author ze97286
 */
case class LendingClubLoan(
    id:Int,
    memberId:Int,
    loanAmount:Double,
    fundedAmount:Double,
    term:Int,
    intRate:Double,
    expDefaultRate:Double,
    serviceFeeRate:Double,
    installment:Double,
    grade:String,
    subGrade:String,
    empLength:Int,
    homeOwnership:String,
    annualInc:Double,
//    isIncV:String,
//    acceptD:ZonedDateTime,
//    expD:ZonedDateTime,
//    listD:ZonedDateTime,
//    creditPullD:ZonedDateTime,
    reviewStatusD:ZonedDateTime,
    reviewStatus:String,
    desc:String,
    purpose:String,
    addrZip:String,
    addrState:String,
    investorCount:Int//,
//    ilsExpD:ZonedDateTime,
//    initialListStatus:String,
//    empTitle:String,
//    accNowDelinq:Int,
//    accOpenPast24Mths:Int,
//    bcOpenToBuy:Int,
//    percentBcGt75:Double,
//    bcUtil:Double,
//    dti:Double,
//    delinq2Yrs:Int,
//    delinqAmnt:Double,
//    earliestCrLine:ZonedDateTime,
//    ficoRangeLow:Int,
//    ficoRangeHigh:Int,
//    inqLast6Mths:Int,
//    mthsSinceLastDelinq:Int,
//    mthsSinceLastRecord:Int,
//    mthsSinceRecentInq:Int,
//    mthsSinceRecentRevolDelinq:Int,
//    mthsSinceRecentBc:Int,
//    mortAcc:Int,
//    openAcc:Int,
//    pubRec:Int,
//    totalBalExMort:Int,
//    revolBal:Double,
//    revolUtil:Double,
//    totalBcLimit:Int,
//    totalAcc:Int,
//    totalIlHighCreditLimit:Int,
//    numRevAccts:Int,
//    mthsSinceRecentBcDlq:Int,
//    pubRecBankruptcies:Int,
//    numAcctsEver120Ppd:Int,
//    chargeoffWithin12Mths:Int,
//    collections12MthsExMed:Int,
//    taxLiens:Int,
//    mthsSinceLastMajorDerog:Int,
//    numSats:Int,
//    numTlOpPast12m:Int,
//    moSinRcntTl:Int,
//    totHiCredLim:Int,
//    totCurBal:Int,
//    avgCurBal:Int,
//    numBcTl:Int,
//    numActvBcTl:Int,
//    numBcSats:Int,
//    pctTlNvrDlq:Int,
//    numTl90gDpd24m:Int,
//    numTl30dpd:Int,
//    numTl120dpd2m:Int,
//    numIlTl:Int,
//    moSinOldIlAcct:Int,
//    numActvRevTl:Int,
//    moSinOldRevTlOp:Int,
//    moSinRcntRevTlOp:Int,
//    totalRevHiLim:Int,
//    numRevTlBalGt0:Int,
//    numOpRevTl:Int,
//    totCollAmt:Int,
//    applicationType:ApplicationType.Value,
//    annualIncJoint:Double,
//    dtiJoint:Double,
//    isIncVJoint:JointIncomeVerified.Value
) {
  val termEnum=term match {
    case 24=> Term._24
    case 36=> Term._36
    case 60=> Term._60
    case _ => throw new IllegalArgumentException("unsupported term")
  }
  
  val gradeEnum = Grade.withName(grade)
  val homeOwnershipEnum = HomeOwnership.withName(homeOwnership)
  val reviewStatusEnum = ReviewStatus.withName(reviewStatus)
  val purposeEnum = Purpose.withName(purpose)
}
  
