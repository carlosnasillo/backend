/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.impl

import java.time.{ZonedDateTime, LocalDate}

import com.lattice.lib.integration.lc.model.{LendingClubLoan, LendingClubNote, LoanListing, OrderPlaced, _}
import com.lattice.lib.integration.lc.{LendingClubConnection, LendingClubDb, LendingClubFactory}
import com.lattice.lib.utils.{DbUtil, Log}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

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

  def reconcileWithMarket() {
    val availableLoans = lc.availableLoans
    val ownedNotes = lc.ownedNotes
    val placedOrders = db.loadOrders
    reconcileAvailableLoans(availableLoans.loans)
    reconcileOwnedNotes(ownedNotes, availableLoans.loans, placedOrders)
  }

  implicit val ec = ExecutionContext.Implicits.global

  /**
   * persist current available loans from lending club
   */
  private[impl] def reconcileAvailableLoans(availableLoans: Seq[LendingClubLoan]) {
    log.info("reconciling available loans")
    val availableLoans = lc.availableLoans
    db.persistLoans(availableLoans)
    calculateLoanAnalytics(availableLoans)
  }

  private[impl] def calculateLoanAnalytics(loanListing: LoanListing) {
    val numLoans: Int = loanListing.loans.size
    val liquidity: BigDecimal = loanListing.loans.map(lcl => lcl.loanAmount - lcl.fundedAmount).sum.toLong
    val numLoansByGrade: Map[String, Int] = loanListing.loans.groupBy(_.grade).mapValues(_.size)
    val liquidityByGrade: Map[String, BigDecimal] = loanListing.loans.groupBy(_.grade).mapValues(_.map(lcl => lcl.loanAmount - lcl.fundedAmount).sum.toLong)

    val loanOrigination: Int = loanListing.loans.count(loan => loan.listD.toLocalDate == LocalDate.now())
    val loanOriginationByGrade: Map[String, Int] = loanListing.loans.filter(loan => loan.listD.toLocalDate == LocalDate.now()).groupBy(_.grade).mapValues(_.size)
    val loanOriginationByYield: Map[Double, Int] = loanListing.loans.filter(loan => loan.listD.toLocalDate == LocalDate.now()).groupBy(_.intRate).mapValues(_.size)

    val originatedNotional: BigDecimal =
      (loanListing.loans collect {
        case x if x.listD.toLocalDate == LocalDate.now() => x.loanAmount
      }).sum.toLong

    val originatedNotionalByGrade: Map[String, BigDecimal] =
      loanListing.loans.groupBy(_.grade).mapValues(_.collect {
        case x if x.listD.toLocalDate == LocalDate.now() => x.loanAmount
      }.sum.toLong)

    val originatedNotionalByYield: Map[Double, BigDecimal] =
      loanListing.loans.groupBy(_.intRate).mapValues(_.collect {
        case x if x.listD.toLocalDate == LocalDate.now() => x.loanAmount
      }.sum.toLong)

    val lendingClubMongoDb: LendingClubMongoDb = new LendingClubMongoDb(DbUtil.db)

    val yesterdayAnalytics: Future[LoanAnalytics] = lendingClubMongoDb.loadAnalyticsByDate(LocalDate.now().minusDays(1))

    yesterdayAnalytics.onComplete {
      case Success(analytics) =>
        val dailyChangeInNumLoans: Int = numLoans - analytics.numLoans
        val dailyChangeInLiquidity: BigDecimal = liquidity - analytics.liquidity

        val todaysAnalytics = LoanAnalytics(
          ZonedDateTime.now(),
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
        )

        lendingClubMongoDb.persistAnalytics(todaysAnalytics)
      case _ => log.error("failed to load analytics listing from db")
    }
  }

  /**
   * read available notes from lending club, for each note, check if its status has changed to trigger any cash flows
   */
  private[impl] def reconcileOwnedNotes(ownedNotes: Seq[LendingClubNote], loans: Seq[LendingClubLoan], placedOrdersFuture: Future[Seq[OrderPlaced]]) {
    log.info("reconciling available notes")

    placedOrdersFuture.onComplete {
      case Success(placedOrders) =>
        log.info(s"placed orders:\n ${placedOrders mkString "\n"}")
        val orderId2Order = placedOrders map (x => (x.orderId -> x)) toMap
        val ordersId2InvestorId = placedOrders map (x => (x.orderId -> x.investorId)) toMap
        val ownedNotesByInvestor = ownedNotes.groupBy(x => ordersId2InvestorId(x.orderId))
        // load the owned notes from LC

        log.info(s"owned notes:\n ${ownedNotes mkString "\n"}")

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

        db.loadTransactions.onComplete {
          case Success(transfers) =>
            AccountBalanceManagerImpl.reconcileAccountBalance(transfers, ownedNotes, pendingOrders)
          case _ => log.error("failed to load transfers from db")
        }
      case _ => log.error("failed to load orders placed from db")
    }
  }

  /**
   * analyse changes in note lifecycle
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
}