/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */

package com.lattice.lib.integration.lc.impl

import java.time.ZonedDateTime
import scala.concurrent.ExecutionContext
import scala.util.Failure
import scala.util.Success
import com.lattice.lib.integration.lc.LendingClubDb
import com.lattice.lib.integration.lc.model.Formatters.loanListingFormat
import com.lattice.lib.integration.lc.model.LoanListing
import com.lattice.lib.integration.lc.model.NoteWrapper
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.modules.reactivemongo.json.JsObjectDocumentWriter
import play.modules.reactivemongo.json.collection.JSONCollectionProducer
import reactivemongo.api.DefaultDB
import play.api.libs.json.JsValue

/**
 * TODO implement all
 * TODO add logging
 * TODO test
 * @author ze97286
 */
class LendingClubMongoDb(db: DefaultDB) extends LendingClubDb {
  implicit val ec = ExecutionContext.Implicits.global

  override def persistLoans(availableLoans: JsValue) {
    log.info(s"persisting available loans: $availableLoans")
    val loans = db.collection("loans")
    val future = loans.insert(availableLoans.as[JsObject])
    future.onComplete {
      case Failure(e) => throw e
      case Success(lastError) => {
        log.info(s"successfully inserted document: $lastError")
      }
    }
  }

  override def availableLoans: LoanListing = {
    val loansTable = db.collection("loans")
    val query = Json.obj()
    val availbleJsonFuture = loansTable.find(query).one[JsObject]
    var loanListing: LoanListing = LoanListing(ZonedDateTime.now, null)
    availbleJsonFuture.onComplete {
      case Success(optListing) => loanListing = Json.fromJson[LoanListing](optListing.get).asOpt.get
      case _                   => log.error("failed to load loan listing from db")
    }
    loanListing
  }

  override def loadNote(orderId: Int, loanId: Int): NoteWrapper = ???

  override def updateNote(note: NoteWrapper): Unit = ???

  override def persistNote(note: NoteWrapper): Unit = ???

}