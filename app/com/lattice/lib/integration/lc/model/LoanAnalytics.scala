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
    loanOrigination: Long,
    loanOriginationByGrade: Map[String, Long],
    loanOriginationByYield: Map[Double, Long],
    originatedNotional: Long,
    originatedNotionalByGrade: Map[String, Long],
    originatedNotionalByYield: Map[Double, Long]
) {

  private def enumifyMap(aMap: Map[String, Long]): Map[Grade, Long] =
    aMap map {
      case (grade, num) => (Grade.withName(grade), num)
    }

  val numLoansByGradeEnum: Map[Grade, Long] = enumifyMap(numLoansByGrade)
  val liquidityByGradeEnum: Map[Grade, Long] = enumifyMap(numLoansByGrade)
  val loanOriginationByGradeEnum: Map[Grade, Long] = enumifyMap(loanOriginationByGrade)
  val originatedNotionalByGradeEnum: Map[Grade, Long] = enumifyMap(loanOriginationByGrade)
}