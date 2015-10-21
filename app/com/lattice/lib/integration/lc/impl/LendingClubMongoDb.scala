/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */

package com.lattice.lib.integration.lc.impl

import java.time.ZonedDateTime

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.TimeoutException
import scala.concurrent.duration.Duration
import scala.util.Failure
import scala.util.Success

import com.lattice.lib.integration.lc.LendingClubDb
import com.lattice.lib.integration.lc.model.Formatters.loanListingFormat
import com.lattice.lib.integration.lc.model.Formatters.orderPlacedFormat
import com.lattice.lib.integration.lc.model.Formatters.transactionFormat

import com.lattice.lib.integration.lc.model.LoanListing
import com.lattice.lib.integration.lc.model.OrderPlaced
import com.lattice.lib.integration.lc.model.Transaction

import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.modules.reactivemongo.json.JsObjectDocumentWriter
import play.modules.reactivemongo.json.collection.JSONCollectionProducer
import reactivemongo.api.DefaultDB

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

  override def availableLoans: LoanListing = {
    val loansTable = db.collection("loans")
    val query = Json.obj()
    val availbleJsonFuture = loansTable.find(query).one[JsObject]
    var loanListing: LoanListing = LoanListing(ZonedDateTime.now, null)
    availbleJsonFuture.onComplete {
      case Success(optListing) => loanListing = Json.fromJson[LoanListing](optListing.get).asOpt.get
      case _                   => log.error("failed to load loan listing from db")
    }
    loanListing
  }

  override def persistOrder(orderPlaced: OrderPlaced) = {
    val orders = db.collection("orders")
    val selector = Json.obj("investorId" -> orderPlaced.investorId, "orderId" -> orderPlaced.orderId)
    val modifier = Json.toJson(orderPlaced).as[JsObject]
    val future = orders.update(selector, modifier, upsert = true)
    Await.ready(future, Duration.Inf)
  }

  override def loadOrders: Seq[OrderPlaced] = {
    val collection = db.collection("orders")
    val futureList = collection.find(Json.obj()).cursor[OrderPlaced].toList(Int.MaxValue)
    Await.ready(futureList, Duration.Inf).value.get.toOption.getOrElse(Seq.empty)
  }
  
  override def loadTransactions: Seq[Transaction] = {
    val collection = db.collection("transactions")
    val futureList = collection.find(Json.obj()).cursor[Transaction].toList(Int.MaxValue)
    Await.ready(futureList, Duration.Inf).value.get.toOption.getOrElse(Seq.empty)
  }
  
  override def persistTransaction(transaction:Transaction) = {
    val orders = db.collection("transactions")
    val transactionJs = Json.toJson(transaction).as[JsObject]
    val future = orders.insert(transaction)
    Await.ready(future, Duration.Inf)
  }
}
