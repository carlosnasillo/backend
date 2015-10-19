/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.utils

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

import reactivemongo.api.MongoDriver

/**
 * Mongo DB util for accessing lattice mongo database
 *
 * @author ze97286
 */
object DbUtil {
  private val driver = new MongoDriver
  private val connection = driver.connection(List("localhost"))

  val timeout = 5 seconds
  val timeoutMillis = timeout.toMillis.toInt

  lazy val db = {
    val _db = connection("lattice")
    Await.ready(_db.drop, timeout)
    _db
  }

  def closeDriver(): Unit = try {
    driver.close()
  } catch { case _: Throwable => () }

}