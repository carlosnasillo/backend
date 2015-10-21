/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.model

import java.time.LocalDate

import models.Grade
import models.Grade
import models.Grade.Grade
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._

/**
*  Created by Julien Déray on 20/10/2015.
*/
case class LoanAnalytics(
    created_on: LocalDate,
    numLoans: Long,
    liquidity: Long,
    numLoansByGrade: Map[String, Long],
    liquidityByGrade: Map[String, Long],
    dailyChangeInNumLoans: Double,
    dailyChangeInLiquidity: Double,
    loanOrigination: Map[LocalDate, Long],
    loanOriginationByGrade: Map[LocalDate, Map[String, Long]],
    loanOriginationByYield: Map[LocalDate, Map[Double, Long]],
    originatedNotional: Map[LocalDate, Long],
    originatedNotionalByGrade: Map[LocalDate, Map[String, Long]],
    originatedNotionalByYield: Map[LocalDate, Map[Double, Long]]
) {

  implicit val mapLocalDateReads: Reads[Map[LocalDate, Long]] = new Reads[Map[LocalDate, Long]] {
    def reads(jv: JsValue): JsResult[Map[LocalDate, Long]] =
      JsSuccess(jv.as[Map[String, Long]].map{case (k, v) =>
        LocalDate.parse(k) -> v .asInstanceOf[Long]
      })
  }

  implicit val mapLocalDateWrites: Writes[Map[LocalDate, Long]] = new Writes[Map[LocalDate, Long]] {
    def writes(map: Map[LocalDate, Long]): JsValue =
      Json.obj(map.map{case (k, o) =>
        val ret: (String, JsValueWrapper) = k.toString -> o
        ret
      }.toSeq:_*)
  }

  implicit val mapLocalDateFormat: Format[Map[LocalDate, Long]] = Format(mapLocalDateReads, mapLocalDateWrites)

  private def enumifyMap(aMap: Map[String, Long]): Map[Grade, Long] =
    aMap map {
      case (grade, num) => (Grade.withName(grade), num)
    }

  val numLoansByGradeEnum: Map[Grade, Long] = enumifyMap(numLoansByGrade)

  val liquidityByGradeEnum: Map[Grade, Long] = enumifyMap(numLoansByGrade)

  val loanOriginationByGradeEnum: Map[LocalDate, Map[Grade, Long]] = loanOriginationByGrade map {
    case (date, byGrade) => (date, enumifyMap(byGrade))
  }

  val originatedNotionalByGradeEnum: Map[LocalDate, Map[Grade, Long]] = originatedNotionalByGrade map {
    case (date, byGrade) => (date, enumifyMap(byGrade))
  }
}