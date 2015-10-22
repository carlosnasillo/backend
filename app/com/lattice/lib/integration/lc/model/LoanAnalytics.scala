/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.model

import java.time.ZonedDateTime

import models.Grade
import models.Grade.Grade

/**
*  Created by Julien Déray on 20/10/2015.
*/
case class LoanAnalytics(
    created_on: ZonedDateTime,
    numLoans: Int,
    liquidity: BigDecimal,
    numLoansByGrade: Map[String, Int],
    liquidityByGrade: Map[String, BigDecimal],
    dailyChangeInNumLoans: Int,
    dailyChangeInLiquidity: BigDecimal,
    loanOrigination: Long,
    loanOriginationByGrade: Map[String, Long],
    loanOriginationByYield: Map[Double, Long],
    originatedNotional: Long,
    originatedNotionalByGrade: Map[String, Long],
    originatedNotionalByYield: Map[Double, Long]
) {

  val numLoansByGradeEnum: Map[Grade, Int] = numLoansByGrade map {
    case (grade, num) => (Grade.withName(grade), num)
  }
  val liquidityByGradeEnum: Map[Grade, BigDecimal] = liquidityByGrade map {
    case (grade, num) => (Grade.withName(grade), num)
  }
  val loanOriginationByGradeEnum: Map[Grade, Long] = loanOriginationByGrade map {
    case (grade, num) => (Grade.withName(grade), num)
  }
  val originatedNotionalByGradeEnum: Map[Grade, Long] = loanOriginationByGrade map {
    case (grade, num) => (Grade.withName(grade), num)
  }
}