/**
 * Copyright (c) 2015 Lattice Markets, All rights reserved.
 *
 * Unpublished copyright. All rights reserved. This material contains
 * proprietary information that shall be used or copied only with
 * Lattice Markets, except with written permission of Lattice Markets.
 */
package com.lattice.lib.portfolio

import com.lattice.lib.integration.lc.LendingClubFactory

import models.Originator

/**
 * A factory to originator's interfaces
 * 
 * @author ze97286
 */
trait MarketPlaceFactory {
  def analytics: MarketplaceAnalytics
  def portfolio: MarketplacePortfolioAnalytics
  def manager: MarketplacePortfolioManager
}

object MarketPlaceFactory {
  def analytics(originator: Originator.Value) = originator match {
    case Originator.LendingClub => LendingClubFactory.analytics
    case _                      => throw new IllegalArgumentException(s"unsupported originator $originator")
  }

  def portfolio(originator: Originator.Value) = originator match {
    case Originator.LendingClub => LendingClubFactory.portfolio
    case _                      => throw new IllegalArgumentException(s"unsupported originator $originator")
  }

  def manager(originator: Originator.Value) = originator match {
    case Originator.LendingClub => LendingClubFactory.manager
    case _                      => throw new IllegalArgumentException(s"unsupported originator $originator")
  }
}