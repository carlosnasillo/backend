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
   * Defines the formatter for Map[Double, BigDecimal]
   */
  private val mapDoubleBigDecimalReads: Reads[Map[Double, BigDecimal]] = new Reads[Map[Double, BigDecimal]] {
    def reads(jv: JsValue): JsResult[Map[Double, BigDecimal]] =
      JsSuccess(jv.as[Map[String, BigDecimal]].map{case (k, v) =>
        k.toDouble -> v
      })
  }

  private val mapDoubleBigDecimalWrites: Writes[Map[Double, BigDecimal]] = new Writes[Map[Double, BigDecimal]] {
    def writes(map: Map[Double, BigDecimal]): JsValue =
      Json.obj(map.map{case (s, o) =>
        val ret: (String, JsValueWrapper) = s.toString -> o
        ret
      }.toSeq:_*)
  }

  implicit val mapDoubleBigDecimalFormat: Format[Map[Double, BigDecimal]] = Format(mapDoubleBigDecimalReads, mapDoubleBigDecimalWrites)

  /**
   * Defines the formatter for Map[Double, Int]
   */
  private val mapDoubleIntReads: Reads[Map[Double, Int]] = new Reads[Map[Double, Int]] {
    def reads(jv: JsValue): JsResult[Map[Double, Int]] =
      JsSuccess(jv.as[Map[String, Int]].map{case (k, v) =>
        k.toDouble -> v
      })
  }

  private val mapDoubleIntWrites: Writes[Map[Double, Int]] = new Writes[Map[Double, Int]] {
    def writes(map: Map[Double, Int]): JsValue =
      Json.obj(map.map{case (s, o) =>
        val ret: (String, JsValueWrapper) = s.toString -> o
        ret
      }.toSeq:_*)
  }

  implicit val mapDoubleIntFormat: Format[Map[Double, Int]] = Format(mapDoubleIntReads, mapDoubleIntWrites)

  /**
   * Defines the formatter for LoanAnalytics
   * (better to be defined after the formatters needed)
   */
  implicit val loanAnalyticsFormat = Json.format[LoanAnalytics]
  implicit val transactionFormat = Json.format[Transaction]
}
