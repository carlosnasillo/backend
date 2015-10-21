package com.lattice.lib.integration.lc.model

import java.time.ZonedDateTime

/**
 * @author ze97286
 */
case class Transaction(investorId:String, time:ZonedDateTime,amount:BigDecimal)