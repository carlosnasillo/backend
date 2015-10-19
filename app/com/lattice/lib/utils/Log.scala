/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.utils

import org.slf4j.LoggerFactory

/**
 * TODO decide on log config
 * 
 * @author ze97286
 */
trait Log {
   def log = LoggerFactory.getLogger(this.getClass)
}