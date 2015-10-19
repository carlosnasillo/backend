/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.integration.lc.model

import java.time.ZonedDateTime

/**
 * @author ze97286
 */
case class LoanListing(asOfDate:ZonedDateTime,loans:Array[LendingClubLoan])