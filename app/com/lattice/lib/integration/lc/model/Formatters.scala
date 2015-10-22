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


  def writes[A, B] = new Writes[Map[A, B]] {
    def writes(map: Map[A, B]): JsValue =
      Json.obj(map.map {
        case (s, o) =>
          val ret: (String, JsValueWrapper) = s.toString -> o.toString
          ret
      }.toSeq: _*)
  }
  def reads[A, B](strToA: String => A, strToB: String => B) = new Reads[Map[A, B]] {
    def reads(jv: JsValue): JsResult[Map[A, B]] =
      JsSuccess(jv.as[Map[String, String]].map {
        case (k, v) =>
          strToA(k) -> strToB(v)
      })
  }

  implicit val mapDoubleBigDecimalFormat: Format[Map[Double, BigDecimal]] =
    Format(
    reads[Double, BigDecimal](_.toDouble, _.asInstanceOf[BigDecimal]),
    writes[Double, BigDecimal]
  )

  implicit val mapDoubleIntFormat: Format[Map[Double, Int]] =
    Format(
      reads[Double, Int](_.toDouble, _.toInt),
      writes[Double, Int]
    )

  /**
   * Defines the formatter for LoanAnalytics
   * (better to be defined after the formatters needed)
   */
  implicit val loanAnalyticsFormat = Json.format[LoanAnalytics]
  implicit val transactionFormat = Json.format[Transaction]
}
