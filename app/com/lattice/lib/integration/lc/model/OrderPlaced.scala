package com.lattice.lib.integration.lc.model

import java.time.ZonedDateTime

/**
 * @author ze97286
 */
case class OrderPlaced(
  investorId: String,
  orderId: Int,
  loanId: Int,
  noteId: Option[Int],
  investedAmount: BigDecimal,
  orderTime: ZonedDateTime,
  contractAddress: Option[String],
  paymentsReceived: BigDecimal,
  loanStatus: String)