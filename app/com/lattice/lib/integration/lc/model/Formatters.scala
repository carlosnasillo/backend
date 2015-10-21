/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.model

import java.time.LocalDate

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
   * Defines the formatter for Map[LocalDate, Long]
   */
  implicit val mapLocalDateLongReads: Reads[Map[LocalDate, Long]] = new Reads[Map[LocalDate, Long]] {
    def reads(jv: JsValue): JsResult[Map[LocalDate, Long]] =
      JsSuccess(jv.as[Map[String, Long]].map{case (k, v) =>
        LocalDate.parse(k) -> v .asInstanceOf[Long]
      })
  }
  implicit val mapLocalDateLongWrites: Writes[Map[LocalDate, Long]] = new Writes[Map[LocalDate, Long]] {
    def writes(map: Map[LocalDate, Long]): JsValue =
      Json.obj(map.map{case (k, o) =>
        val ret: (String, JsValueWrapper) = k.toString -> o
        ret
      }.toSeq:_*)
  }
  implicit val mapLocalDateLongFormat: Format[Map[LocalDate, Long]] = Format(mapLocalDateLongReads, mapLocalDateLongWrites)

  /**
   * Defines the formatter for Map[LocalDate, Map[String, Long]]
   */
  implicit val mapLocalDateMapStringLongReads: Reads[Map[LocalDate, Map[String, Long]]] = new Reads[Map[LocalDate, Map[String, Long]]] {
    def reads(jv: JsValue): JsResult[Map[LocalDate, Map[String, Long]]] =
      JsSuccess(jv.as[Map[String, Map[String, Long]]].map{case (k, v) =>
        LocalDate.parse(k) -> v
      })
  }
  implicit val mapLocalDateMapStringLongWrites: Writes[Map[LocalDate, Map[String, Long]]] = new Writes[Map[LocalDate, Map[String, Long]]] {
    def writes(map: Map[LocalDate, Map[String, Long]]): JsValue =
      Json.obj(map.map{case (k, o) =>
        val ret: (String, JsValueWrapper) = k.toString -> o
        ret
      }.toSeq:_*)
  }
  implicit val mapLocalDateMapStringLongFormat: Format[Map[LocalDate, Map[String, Long]]] = Format(mapLocalDateMapStringLongReads, mapLocalDateMapStringLongWrites)

  /**
   * Defines the formatter for Map[Double, Long]
   */
  implicit val mapDoubleLongReads: Reads[Map[Double, Long]] = new Reads[Map[Double, Long]] {
    def reads(jv: JsValue): JsResult[Map[Double, Long]] =
      JsSuccess(jv.as[Map[Double, Long]].map{case (k, v) =>
        k -> v .asInstanceOf[Long]
      })
  }
  implicit val mapDoubleLongWrites: Writes[Map[Double, Long]] = new Writes[Map[Double, Long]] {
    def writes(map: Map[Double, Long]): JsValue =
      Json.obj(map.map{case (k, o) =>
        val ret: (String, JsValueWrapper) = k.toString -> o
        ret
      }.toSeq:_*)
  }
  implicit val mapDoubleLongFormat: Format[Map[Double, Long]] = Format(mapDoubleLongReads, mapDoubleLongWrites)

  /**
   * Defines the formatter for Map[LocalDate, Map[Double, Long]]
   */
  implicit val mapLocalDateMapDoubleLongReads: Reads[Map[LocalDate, Map[Double, Long]]] = new Reads[Map[LocalDate, Map[Double, Long]]] {
    def reads(jv: JsValue): JsResult[Map[LocalDate, Map[Double, Long]]] =
      JsSuccess(jv.as[Map[String, Map[Double, Long]]].map{case (k, v) =>
        LocalDate.parse(k) -> v
      })
  }
  implicit val mapLocalDateMapDoubleLongWrites: Writes[Map[LocalDate, Map[Double, Long]]] = new Writes[Map[LocalDate, Map[Double, Long]]] {
    def writes(map: Map[LocalDate, Map[Double, Long]]): JsValue =
      Json.obj(map.map{case (k, o) =>
        val ret: (String, JsValueWrapper) = k.toString -> o
        ret
      }.toSeq:_*)
  }
  implicit val mapLocalDateMapDoubleLongFormat: Format[Map[LocalDate, Map[Double, Long]]] = Format(mapLocalDateMapDoubleLongReads, mapLocalDateMapDoubleLongWrites)

  /**
   * Defines the formatter for LoanAnalytics
   * (better to be defined after the formatters needed)
   */
  implicit val loanAnalyticsFormat = Json.format[LoanAnalytics]
  implicit val transactionFormat = Json.format[Transaction]
}
