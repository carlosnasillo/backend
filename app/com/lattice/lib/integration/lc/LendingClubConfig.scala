/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */

package com.lattice.lib.integration.lc

/**
 * Lending club connectivity details
 * TODO: read authorisation details from config
 * 
 * @author ze97286
 */
object LendingClubConfig {
  val ApiKey = "0eeZrVo3/35tlFuRauP7IxwEiOA="
  val Account = 11479917
  val AuthorisationHeader = "Authorization"
  val ApiKeyHeader = "X-LC-Application-Key"
  val Authorisation = "dm+drWEIv3a2LQFPBcTwWO7eSMs="
  val BaseUrl = "https://api-sandbox.lendingclub.com/api/investor/v1/"
  val LoanListingUrl = BaseUrl + "loans/listing?showAll=true"
  val AccountSummaryUrl = s"${BaseUrl}accounts/${Account}/summary"
  val OwnedNotesUrl = s"${BaseUrl}accounts/${Account}/detailednotes"
  val TransferFundsUrl = s"${BaseUrl}accounts/${Account}/funds/add"
  val WithdrawFundsUrl = s"${BaseUrl}accounts/${Account}/funds/withdraw"
  val SubmitOrderUrl = s"${BaseUrl}accounts/${Account}/orders"
  val EmptyDoc = "{}"
}