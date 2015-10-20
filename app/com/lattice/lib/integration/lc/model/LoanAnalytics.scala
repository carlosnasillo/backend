package com.lattice.lib.integration.lc.model

import java.time.LocalDate

import models.Grade

/**
 * Created by Julien Déray on 20/10/2015.
 */
case class LoanAnalytics(
    numLoans: Long,
    liquidity: Long,
    numLoansByGrade: Map[Grade.Value, Long],
    liquidityByGrade: Map[Grade.Value, Long],
    dailyChangeInNumLoans: Double,
    dailyChangeInLiquidity: Double,
    loanOrigination: Map[LocalDate, Long],
    loanOriginationByGrade: Map[LocalDate, Map[Grade.Value, Long]],
    loanOriginationByYield: Map[LocalDate, Map[Double, Long]],
    originatedNotional: Map[LocalDate, Long],
    originatedNotionalByGrade: Map[LocalDate, Map[Grade.Value, Long]],
    originatedNotionalByYield: Map[LocalDate, Map[Double, Long]]
)
