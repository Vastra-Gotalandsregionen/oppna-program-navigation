/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 */

package se.vgregion.portal.navigation.service;

import java.util.Collection;
import java.util.List;

import se.vgregion.portal.navigation.domain.jpa.NavigationSite;

/**
 * Service interface for managing {@link NavigationSite}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface NavigationSiteService {

    /**
     * Add a {@link NavigationSite}.
     *
     * @param userId    the userId of the user who creates the {@link NavigationSite}
     * @param companyId the companyId
     * @param groupId   the groupId
     */
    void addNavigationSite(long userId, long companyId, long groupId);

    /**
     * Add a {@link NavigationSite}.
     *
     * @param userId          the userId of the user who creates the {@link NavigationSite}
     * @param companyId       the companyId
     * @param groupId         the groupId
     * @param isPrivateLayout whether private layout, i.e. whether public or private (requiring authorization to it)
     * @param isActive        whether active, i.e. whether it is browsable
     * @param orderIndex      the orderIndex, used for ordering
     */
    void addNavigationSite(long userId, long companyId, long groupId, boolean isPrivateLayout, boolean isActive,
                           int orderIndex);

    /**
     * Find all {@link NavigationSite}s.
     *
     * @return all {@link NavigationSite}s.
     */
    Collection<NavigationSite> findAll();

    /**
     * Find all {@link NavigationSite} for a given company.
     *
     * @param companyId the companyId
     * @return all {@link NavigationSite} for a given company
     */
    List<NavigationSite> findNavigationSitesByCompanyId(long companyId);

    /**
     * Find all {@link NavigationSite}s with a given status for a given company.
     *
     * @param companyId the companyId
     * @param isActive  whether active
     * @return a {@link List} of {@link NavigationSite}s
     */
    List<NavigationSite> findNavigationSitesByActiveState(long companyId, boolean isActive);

    /**
     * Find {@link NavigationSite}s by company and group.
     *
     * @param companyId the companyId
     * @param groupId   the groupId
     * @return a {@link List} of {@link NavigationSite}s
     */
    List<NavigationSite> findNavigationSitesByGroupId(long companyId, long groupId);

    /**
     * Find all {@link NavigationSite}s for a given company except for the main site.
     *
     * @param companyId the companyId
     * @return a {@link List} of {@link NavigationSite}s
     */
    List<NavigationSite> findNavigationSitesSub(long companyId);

    /**
     * Find the main {@link NavigationSite} for a company.
     *
     * @param companyId the companyId
     * @return the main {@link NavigationSite} for a company
     */
    NavigationSite findNavigationSitesMain(long companyId);

    /**
     * Find a {@link NavigationSite} by companyId and orderIndex.
     *
     * @param companyId  the companyId
     * @param orderIndex the orderIndex
     * @return the {@link NavigationSite} or <code>null</code> if none found
     */
    NavigationSite findNavigationSiteByOrderIndex(long companyId, int orderIndex);

    /**
     * Remove a {@link NavigationSite}.
     *
     * @param navigationSite the {@link NavigationSite} to remove
     */
    void remove(NavigationSite navigationSite);

    /**
     * Remove all {@link NavigationSite}s.
     */
    void removeAll();
}
