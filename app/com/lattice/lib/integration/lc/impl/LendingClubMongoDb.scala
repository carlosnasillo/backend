/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */

package com.lattice.lib.integration.lc.impl

import java.time.LocalDate

import com.lattice.lib.integration.lc.LendingClubDb
import com.lattice.lib.integration.lc.model.Formatters.{loanAnalyticsFormat, loanListingFormat, orderPlacedFormat, transactionFormat}
import com.lattice.lib.integration.lc.model.{LoanAnalytics, LoanListing, OrderPlaced, Transaction}
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.json.JsObjectDocumentWriter
import play.modules.reactivemongo.json.collection.JSONCollectionProducer
import reactivemongo.api.DefaultDB

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}
/**
 * TODO implement all
 * TODO add logging
 * TODO test
 * @author ze97286
 */
class LendingClubMongoDb(db: DefaultDB) extends LendingClubDb {
  implicit val ec = ExecutionContext.Implicits.global

  override def persistLoans(availableLoans: LoanListing) {
    log.info(s"persisting available loans: $availableLoans")
    val loans = db.collection("loans")
    val loansJs=Json.toJson(availableLoans)
    val future = loans.insert(loansJs.as[JsObject])
    future.onComplete {
      case Failure(e) => throw e
      case Success(lastError) => {
        log.info(s"successfully inserted document: $lastError")
      }
    }
  }

  override def availableLoans: Future[LoanListing] = {
    val loansTable = db.collection("loans")
    val query = Json.obj()
    val availableJsonFuture = loansTable.find(query).one[JsObject]
    availableJsonFuture.map(json => Json.fromJson[LoanListing](json.get).asOpt.get )
  }

  override def persistOrder(orderPlaced: OrderPlaced) = {
    val orders = db.collection("orders")
    val selector = Json.obj("investorId" -> orderPlaced.investorId, "orderId" -> orderPlaced.orderId)
    val modifier = Json.toJson(orderPlaced).as[JsObject]
    val future = orders.update(selector, modifier, upsert = true)
    Await.ready(future, Duration.Inf)
  }

  override def persistAnalytics(futureLoanAnalytics: Future[LoanAnalytics]): Unit = {
    val loanAnalyticsCol = db.collection("loanAnalytics")
    futureLoanAnalytics.onComplete {
      case Success(loanAnalytics) =>
        val future = loanAnalyticsCol.insert(Json.toJson(loanAnalytics).as[JsObject])
        future.onComplete {
          case Failure(e) => throw e
          case Success(lastError) => log.info(s"successfully inserted document: $lastError")
        }
      case Failure(e) => throw e
    }
  }

  override def loadOrders: Future[Seq[OrderPlaced]] = {
    val collection = db.collection("orders")
    val futureList = collection.find(Json.obj()).cursor[OrderPlaced].toList(Int.MaxValue)
    futureList
  }

  override def loadAnalyticsByDate(date: LocalDate): Future[LoanAnalytics] = {
    val loansAnalytics = db.collection("loanAnalytics")
    val query = Json.obj("created_on" -> Json.obj("$gte" -> date, "$lt" -> date.plusDays(1)))
    loansAnalytics.find(query).sort(Json.obj("created_on" -> -1)).cursor[LoanAnalytics].toList(Int.MaxValue).map(_.head)
  }

  override def loadTransactions: Future[Seq[Transaction]] = {
    val collection = db.collection("transactions")
    collection.find(Json.obj()).cursor[Transaction].toList(Int.MaxValue)
  }

  override def persistTransaction(transaction:Transaction) = {
    val orders = db.collection("transactions")
    val transactionJs = Json.toJson(transaction).as[JsObject]
    val future = orders.insert(transaction)
    Await.ready(future, Duration.Inf)
  }
}  