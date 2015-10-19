/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */

package com.lattice.lib.investor

import com.lattice.lib.investor.impl.InvestorMongoDb
import com.lattice.lib.utils.DbUtil

/**
 * Investor Db factory 
 * 
 * TODO replace with DI
 * 
 * @author ze97286
 */
object InvestorDbFactory {
  val db:InvestorDb=new InvestorMongoDb(DbUtil.db)
}