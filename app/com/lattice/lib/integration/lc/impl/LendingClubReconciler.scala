/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.impl


import com.fasterxml.jackson.databind.JsonNode
import com.lattice.lib.integration.lc.{LendingClubConnection, LendingClubDb, LendingClubFactory}
import com.lattice.lib.integration.lc.model.{LendingClubLoan, LendingClubNote, LoanListing, OrderPlaced}
import com.lattice.lib.utils.Log
import models.Grade
import play.api.libs.json.JsValue
import play.libs.Json

/**
 * The reconciler is run periodically and reconciles the loans, notes, and accounts database with the state in lending club
 *
 * TODO add logging
 * TODO add error handling
 * TODO verify note analysis logic + add handling for various states - e.g late 
 * TODO add contract interaction
 * @author ze97286
 */

class LendingClubReconciler(
  lc: LendingClubConnection, // access to lending club api
  db: LendingClubDb) // access to lending club database
    extends Log {

  def reconcileWithMarket {
    val availableLoans = lc.availableLoans
    val ownedNotes = lc.ownedNotes
    val placedOrders = db.loadOrders
    reconcileAvailableLoans(availableLoans.loans)
    reconcileOwnedNotes(ownedNotes, availableLoans.loans, placedOrders)
  }

  /**
   * persist current available loans from lending club
   */
  private[impl] def reconcileAvailableLoans(availableLoans: Seq[LendingClubLoan]) {
    log.info("reconciling available loans")
    val availableLoans = lc.availableLoans
    db.persistLoans(availableLoans)
    calculateLoanAnalytics(availableLoans)
  }
  
  //TODO Julien - use this to calculate the analytics and persist the result to db
  private[impl] def calculateLoanAnalytics(loanListing: LoanListing) {
    val model: JsValue = Json.parse(
      """
        {
          "asOfDate":"2015-10-20T05:58:49.506-07:00",
          "loans":[
              {
                                "id" : 9712698,
                                "memberId" : 11514201,
                                "loanAmount" : 15000,
                                "fundedAmount" : 1000,
                                "term" : 36,
                                "intRate" : 21.99,
                                "expDefaultRate" : 10.55,
                                "serviceFeeRate" : 1.11,
                                "installment" : 572.78,
                                "grade" : "E",
                                "subGrade" : "E5",
                                "empLength" : null,
                                "homeOwnership" : "OWN",
                                "annualInc" : 120000,
                                "isIncV" : "NOT_VERIFIED",
                                "acceptD" : "2015-10-15T20:03:48.000-07:00",
                                "expD" : "2015-10-29T20:04:27.000-07:00",
                                "listD" : "2015-10-15T20:04:27.000-07:00",
                                "creditPullD" : "2015-10-15T20:03:45.000-07:00",
                                "reviewStatusD" : null,
                                "reviewStatus" : "NOT_APPROVED",
                                "desc" : null,
                                "purpose" : "house",
                                "addrZip" : "110xx",
                                "addrState" : "CA",
                                "investorCount" : null,
                                "ilsExpD" : "2015-11-01T11:04:51.000-08:00",
                                "initialListStatus" : "F",
                                "empTitle" : null,
                                "accNowDelinq" : 0,
                                "accOpenPast24Mths" : 23,
                                "bcOpenToBuy" : 30000,
                                "percentBcGt75" : 23,
                                "bcUtil" : 23,
                                "dti" : 0,
                                "delinq2Yrs" : 1,
                                "delinqAmnt" : 0,
                                "earliestCrLine" : "1984-09-15T00:00:00.000-07:00",
                                "ficoRangeLow" : 750,
                                "ficoRangeHigh" : 754,
                                "inqLast6Mths" : 0,
                                "mthsSinceLastDelinq" : 90,
                                "mthsSinceLastRecord" : 0,
                                "mthsSinceRecentInq" : 14,
                                "mthsSinceRecentRevolDelinq" : 23,
                                "mthsSinceRecentBc" : 23,
                                "mortAcc" : 23,
                                "openAcc" : 3,
                                "pubRec" : 0,
                                "totalBalExMort" : 13944,
                                "revolBal" : 1,
                                "revolUtil" : 0,
                                "totalBcLimit" : 23,
                                "totalAcc" : 4,
                                "totalIlHighCreditLimit" : 12,
                                "numRevAccts" : 28,
                                "mthsSinceRecentBcDlq" : 52,
                                "pubRecBankruptcies" : 0,
                                "numAcctsEver120Ppd" : 12,
                                "chargeoffWithin12Mths" : 0,
                                "collections12MthsExMed" : 0,
                                "taxLiens" : 0,
                                "mthsSinceLastMajorDerog" : 12,
                                "numSats" : 8,
                                "numTlOpPast12m" : 0,
                                "moSinRcntTl" : 12,
                                "totHiCredLim" : 12,
                                "totCurBal" : 12,
                                "avgCurBal" : 12,
                                "numBcTl" : 12,
                                "numActvBcTl" : 12,
                                "numBcSats" : 7,
                                "pctTlNvrDlq" : 12,
                                "numTl90gDpd24m" : 12,
                                "numTl30dpd" : 12,
                                "numTl120dpd2m" : 12,
                                "numIlTl" : 12,
                                "moSinOldIlAcct" : 12,
                                "numActvRevTl" : 12,
                                "moSinOldRevTlOp" : 12,
                                "moSinRcntRevTlOp" : 11,
                                "totalRevHiLim" : 12,
                                "numRevTlBalGt0" : 12,
                                "numOpRevTl" : 12,
                                "totCollAmt" : 12,
                                "applicationType" : "INDIVIDUAL",
                                "annualIncJoint" : null,
                                "dtiJoint" : null,
                                "isIncVJoint" : null
                        },
                        {
                                "id" : 9712699,
                                "memberId" : 11514202,
                                "loanAmount" : 15000,
                                "fundedAmount" : 0,
                                "term" : 36,
                                "intRate" : 21.99,
                                "expDefaultRate" : 10.55,
                                "serviceFeeRate" : 1.11,
                                "installment" : 572.78,
                                "grade" : "E",
                                "subGrade" : "E5",
                                "empLength" : null,
                                "homeOwnership" : "OWN",
                                "annualInc" : 120000,
                                "isIncV" : "NOT_VERIFIED",
                                "acceptD" : "2015-10-15T20:05:21.000-07:00",
                                "expD" : "2015-10-29T20:06:04.000-07:00",
                                "listD" : "2015-10-15T20:06:04.000-07:00",
                                "creditPullD" : "2015-10-15T20:05:18.000-07:00",
                                "reviewStatusD" : null,
                                "reviewStatus" : "NOT_APPROVED",
                                "desc" : null,
                                "purpose" : "house",
                                "addrZip" : "110xx",
                                "addrState" : "CA",
                                "investorCount" : null,
                                "ilsExpD" : "2015-11-01T11:06:25.000-08:00",
                                "initialListStatus" : "F",
                                "empTitle" : null,
                                "accNowDelinq" : 0,
                                "accOpenPast24Mths" : 23,
                                "bcOpenToBuy" : 30000,
                                "percentBcGt75" : 23,
                                "bcUtil" : 23,
                                "dti" : 0,
                                "delinq2Yrs" : 1,
                                "delinqAmnt" : 0,
                                "earliestCrLine" : "1984-09-15T00:00:00.000-07:00",
                                "ficoRangeLow" : 750,
                                "ficoRangeHigh" : 754,
                                "inqLast6Mths" : 0,
                                "mthsSinceLastDelinq" : 90,
                                "mthsSinceLastRecord" : 0,
                                "mthsSinceRecentInq" : 14,
                                "mthsSinceRecentRevolDelinq" : 23,
                                "mthsSinceRecentBc" : 23,
                                "mortAcc" : 23,
                                "openAcc" : 3,
                                "pubRec" : 0,
                                "totalBalExMort" : 13944,
                                "revolBal" : 1,
                                "revolUtil" : 0,
                                "totalBcLimit" : 23,
                                "totalAcc" : 4,
                                "totalIlHighCreditLimit" : 12,
                                "numRevAccts" : 28,
                                "mthsSinceRecentBcDlq" : 52,
                                "pubRecBankruptcies" : 0,
                                "numAcctsEver120Ppd" : 12,
                                "chargeoffWithin12Mths" : 0,
                                "collections12MthsExMed" : 0,
                                "taxLiens" : 0,
                                "mthsSinceLastMajorDerog" : 12,
                                "numSats" : 8,
                                "numTlOpPast12m" : 0,
                                "moSinRcntTl" : 12,
                                "totHiCredLim" : 12,
                                "totCurBal" : 12,
                                "avgCurBal" : 12,
                                "numBcTl" : 12,
                                "numActvBcTl" : 12,
                                "numBcSats" : 7,
                                "pctTlNvrDlq" : 12,
                                "numTl90gDpd24m" : 12,
                                "numTl30dpd" : 12,
                                "numTl120dpd2m" : 12,
                                "numIlTl" : 12,
                                "moSinOldIlAcct" : 12,
                                "numActvRevTl" : 12,
                                "moSinOldRevTlOp" : 12,
                                "moSinRcntRevTlOp" : 11,
                                "totalRevHiLim" : 12,
                                "numRevTlBalGt0" : 12,
                                "numOpRevTl" : 12,
                                "totCollAmt" : 12,
                                "applicationType" : "INDIVIDUAL",
                                "annualIncJoint" : null,
                                "dtiJoint" : null,
                                "isIncVJoint" : null
                        }
                ]
            }
      """)

    val loanListingM = Json.fromJson[LoanListing](model).asOpt.get

    val numLoans: Long = loanListingM.loans.size
    val liquidity: Long = loanListingM.loans.map(lcl => lcl.loanAmount - lcl.fundedAmount).sum.toLong
    val numLoansByGrade: Map[Grade.Value, Long] = loanListingM.loans.groupBy(_.gradeEnum).mapValues(_.size)
    val liquidityByGrade: Map[Grade.Value, Long] = loanListingM.loans.groupBy(_.gradeEnum).mapValues(_.map(lcl => lcl.loanAmount - lcl.fundedAmount).sum.toLong)
//    val dailyChangeInNumLoans: Double = ???
//    val dailyChangeInLiquidity: Double = ???
//    val loanOrigination: Map[LocalDate, Long] = ???
//    val loanOriginationByGrade: Map[LocalDate, Map[Grade.Value, Long]] = ???
//    val loanOriginationByYield: Map[LocalDate, Map[Double, Long]] = ???
//    val originatedNotional: Map[LocalDate, Long] = ???
//    val originatedNotionalByGrade: Map[LocalDate, Map[Grade.Value, Long]] = ???
//    val originatedNotionalByYield: Map[LocalDate, Map[Double, Long]] = ???

    println(liquidityByGrade)

    /*LoanAnalytics(
      numLoans,
      liquidity,
      numLoansByGrade,
      liquidityByGrade,
      dailyChangeInNumLoans,
      dailyChangeInLiquidity,
      loanOrigination,
      loanOriginationByGrade,
      loanOriginationByYield,
      originatedNotional,
      originatedNotionalByGrade,
      originatedNotionalByYield
    )*/
  }

  /**
   * read available notes from lending club, for each note, check if its status has changed to trigger any cash flows
   */
  private[impl] def reconcileOwnedNotes(ownedNotes: Seq[LendingClubNote], loans: Seq[LendingClubLoan], placedOrders: Seq[OrderPlaced]) {
    log.info("reconciling available notes")

    log.info(s"placed orders:\n ${placedOrders mkString "\n"}")

    // load the owned notes from LC

    log.info(s"owned notes:\n ${ownedNotes mkString "\n"}")

    val orderId2Order = placedOrders map (x => (x.orderId -> x)) toMap
    val ordersId2InvestorId = placedOrders map (x => (x.orderId -> x.investorId)) toMap
    val ownedNotesByInvestor = ownedNotes.groupBy(x => ordersId2InvestorId(x.orderId))

    val portfolios = ownedNotesByInvestor map { case (k, v) => (k, PortfolioAnalyzer.analyse(v)) }

    LendingClubFactory.portfolio.resetPortfolios(portfolios)

    val notesToOrder = ownedNotes map (x => (x -> orderId2Order(x.orderId)))

    notesToOrder foreach (x => analyseNote(x._1, x._2))

    val orderIdToNote = (ownedNotes map (x => x.orderId -> x)).toMap

    val unusedOrders = placedOrders filter (x => !orderIdToNote.contains(x.orderId))
    val (pendingOrders, unissuedOrders) = analyseInactiveOrders(placedOrders, loans)

    unissuedOrders foreach (x => {
      db.persistOrder(x.copy(loanStatus = "Not Issued"))
    })

    val transfers = db.loadTransactions

    AccountBalanceManagerImpl.reconcileAccountBalance(transfers, ownedNotes, pendingOrders)
  }

  /**
   *  analyse changes in note lifecycle
   */
  private[lc] def analyseNote(note: LendingClubNote, order: OrderPlaced) {
    order.contractAddress match {
      case None =>
        val address = createLoanContract
        db.persistOrder(order.copy(loanStatus = note.loanStatus, noteId = Some(note.noteId), contractAddress = Some(address)))
      case Some(address) if (order.paymentsReceived != note.paymentsReceived) =>
        val paid = note.paymentsReceived - order.paymentsReceived
        db.persistOrder(order.copy(paymentsReceived = note.paymentsReceived))
      // send payment to the smart contract
      //       case Some(address) if (note.loanStatus=="pending")
    }
  }

  private[lc] def analyseInactiveOrders(order: Seq[OrderPlaced], loans: Seq[LendingClubLoan]) = {
    val loanIdToLoan = (loans.map(x => (x.id -> x))).toMap
    order.partition { x => loanIdToLoan.contains(x.loanId) }
  }

  //TODO create the contract on bc and return its address
  def createLoanContract: String = ""

