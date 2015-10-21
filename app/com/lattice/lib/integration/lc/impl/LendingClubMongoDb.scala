/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */

package com.lattice.lib.integration.lc.impl

import java.time.{LocalDate, ZonedDateTime}

import com.lattice.lib.integration.lc.LendingClubDb
import com.lattice.lib.integration.lc.model.Formatters.{loanAnalyticsFormat, loanListingFormat, orderPlacedFormat}
import com.lattice.lib.integration.lc.model.LoanAnalytics

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}matters.transactionFormat

import com.lattice.lib.integration.lc.model.{LoanListing, OrderPlaced}
import play.api.libs.json.{JsObject, Json}
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

  override def persistAnalytics(loanAnalytics: LoanAnalytics): Unit = {
    val loanAnalyticsCol = db.collection("loanAnalytics")
    val future = loanAnalyticsCol.insert(Json.toJson(loanAnalytics).as[JsObject])
    future.onComplete {
      case Failure(e) => throw e
      case Success(lastError) => log.info(s"successfully inserted document: $lastError")
    }
  }

  override def loadOrders: Seq[OrderPlaced] = {
    val collection = db.collection("orders")
    val futureList = collection.find(Json.obj()).cursor[OrderPlaced].toList(Int.MaxValue)
    Await.ready(futureList, Duration.Inf).value.get.toOption.getOrElse(Seq.empty)
  }

  // TODO : there is probably a way to avoid created_on as _id is made from the date itself. See if we can request on it directly
  override def loadAnalyticsByDate(date: LocalDate): Future[LoanAnalytics] = {
    val loansAnalytics = db.collection("loanAnalytics")
    val query = Json.obj("created_on" -> Json.obj("$gte" -> date.minusDays(1), "$lt" -> date.plusDays(1)))
    loansAnalytics.find(query).one[JsObject].map { optAnalytics =>
      Json.fromJson[LoanAnalytics](optAnalytics.get).asOpt.get
    }

//    var analytics: Option[LoanAnalytics] = None
//    loansAnalyticsJsonFuture.onComplete {
//      case Success(optAnalytics) =>
//        analytics = Some(Json.fromJson[LoanAnalytics](optAnalytics.get).asOpt.get)
//      case _                   => log.error("failed to load loan listing from db")
//    }
//    analytics
  }
}  