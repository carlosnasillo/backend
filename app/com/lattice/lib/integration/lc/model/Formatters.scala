/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.model

import play.api.libs.json.Json.JsValueWrapper
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
  implicit val orderPlacedFormat = Json.format[OrderPlaced]
  implicit val loanFormat = Json.format[LendingClubLoan]
  implicit val loanListingFormat = Json.format[LoanListing]
  implicit val accountSummaryFormat = Json.format[AccountSummary]

  /**
   * Defines the formatter for Map[Double, Long]
   */
  private val mapDoubleLongReads: Reads[Map[Double, Long]] = new Reads[Map[Double, Long]] {
    def reads(jv: JsValue): JsResult[Map[Double, Long]] =
      JsSuccess(jv.as[Map[String, Long]].map{case (k, v) =>
        k.toDouble -> v.toLong
      })
  }

  private val mapDoubleLongWrites: Writes[Map[Double, Long]] = new Writes[Map[Double, Long]] {
    def writes(map: Map[Double, Long]): JsValue =
      Json.obj(map.map{case (s, o) =>
        val ret: (String, JsValueWrapper) = s.toString -> o
        ret
      }.toSeq:_*)
  }

  implicit val mapDoubleLongFormat: Format[Map[Double, Long]] = Format(mapDoubleLongReads, mapDoubleLongWrites)

  /**
   * Defines the formatter for LoanAnalytics
   * (better to be defined after the formatters needed)
   */
  implicit val loanAnalyticsFormat = Json.format[LoanAnalytics]
  implicit val transactionFormat = Json.format[Transaction]
}
