/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */

package com.lattice.lib.integration.lc.impl

import com.lattice.lib.integration.lc.LendingClubConfig
import com.lattice.lib.integration.lc.LendingClubConfig.ApiKey
import com.lattice.lib.integration.lc.LendingClubConfig.ApiKeyHeader
import com.lattice.lib.integration.lc.LendingClubConfig.Authorisation
import com.lattice.lib.integration.lc.LendingClubConfig.AuthorisationHeader
import com.lattice.lib.integration.lc.LendingClubConfig.SubmitOrderUrl
import com.lattice.lib.integration.lc.LendingClubConnection
import com.lattice.lib.integration.lc.model.AccountSummary
import com.lattice.lib.integration.lc.model.ExecutionReport
import com.lattice.lib.integration.lc.model.Formatters.accountSummaryFormat
import com.lattice.lib.integration.lc.model.Formatters.executionReportFormat
import com.lattice.lib.integration.lc.model.Formatters.loanListingFormat
import com.lattice.lib.integration.lc.model.Formatters.notesFormat
import com.lattice.lib.integration.lc.model.Formatters.ordersFormat
import com.lattice.lib.integration.lc.model.LendingClubNote
import com.lattice.lib.integration.lc.model.LoanListing
import com.lattice.lib.integration.lc.model.Order
import com.lattice.lib.integration.lc.model.Orders
import com.lattice.lib.integration.lc.model.OwnedNotes

import play.api.libs.json.Json
import scalaj.http.Http

/**
 * Implementation for lending club connection api
 *
 * TODO need to look at why available loans is taking so much time to serialise - maybe need to change to use csv format or xml
 * TODO fix ownedNotes
 * TODO add full logging
 * TODO test all
 *
 * @author ze97286
 */

object LendingClubConnectionImpl extends LendingClubConnection {

  /**
   * Submit an order to buy loans based on the given sequence of orders
   */
  override def submitOrder(orders: Seq[Order]): ExecutionReport = {
    val lcOrders = Orders(LendingClubConfig.Account, orders)
    val jsonOrders = Json.toJson(lcOrders).toString
    val erString = Http(SubmitOrderUrl).headers(
      ("Content-Type", "application/json"),
      (AuthorisationHeader, Authorisation),
      (ApiKeyHeader, ApiKey)).postData(jsonOrders).asString.body
    val erJson = Json.parse(erString)
    Json.fromJson[ExecutionReport](erJson).asOpt.get
  }

  /**
   * Get notes owned through lattice
   */
  override def ownedNotes: Seq[LendingClubNote] = {
    val notesString = Http(LendingClubConfig.OwnedNotesUrl)
      .headers((AuthorisationHeader, Authorisation),
        (ApiKeyHeader, ApiKey)).asString.body

    if (notesString == LendingClubConfig.EmptyDoc) {
      Seq()
    } else {
      val notesJson = Json.parse(notesString)
      println(s"ownedNotes=$notesJson")
      Json.fromJson[OwnedNotes](notesJson).asOpt.get.myNotes
    }
  }

  /**
   * Get the currently available loans listing
   */
  override def availableLoans: LoanListing = {
    val loansString = Http(LendingClubConfig.LoanListingUrl)
      .headers((AuthorisationHeader, Authorisation),
        (ApiKeyHeader, ApiKey)).asString.body

    println(s"availableLoans string=$loansString")

    val loansJson = Json.parse(loansString)

    println(s"availableLoans json=$loansJson")

    Json.fromJson[LoanListing](loansJson).asOpt.get

  }

  /**
   * Get lattice account summary from LC
   */
  override def accountSummary: AccountSummary = {
    val summaryString = Http(LendingClubConfig.AccountSummaryUrl)
      .headers((AuthorisationHeader, Authorisation),
        (ApiKeyHeader, ApiKey)).asString.body

    val summaryJson = Json.parse(summaryString)

    Json.fromJson[AccountSummary](summaryJson).asOpt.get
  }

  /**
   *  Transfer funds to lattice account with Lending Club
   */
  override def transferFunds(amount: BigDecimal) = {
    val transferJson = s"""{
      "transferFrequency" : "LOAD_NOW",
      "amount" : $amount
      }"""

    val transferResult = Http(LendingClubConfig.TransferFundsUrl).headers(
      ("Content-Type", "application/json"),
      (AuthorisationHeader, Authorisation),
      (ApiKeyHeader, ApiKey)).postData(transferJson).asString.body

  }

  /**
   *  Withdraw funds from Lending Club to lattice account
   */
  override def withdrawFunds(amount: BigDecimal) = {
    val withdrawJson = s"""{"amount" : $amount}"""

    println(LendingClubConfig.WithdrawFundsUrl)

    val withdrawResult = Http(LendingClubConfig.WithdrawFundsUrl).headers(
      ("Content-Type", "application/json"),
      (AuthorisationHeader, Authorisation),
      (ApiKeyHeader, ApiKey)).postData(withdrawJson).asString.body

    println(withdrawResult)
  }
}