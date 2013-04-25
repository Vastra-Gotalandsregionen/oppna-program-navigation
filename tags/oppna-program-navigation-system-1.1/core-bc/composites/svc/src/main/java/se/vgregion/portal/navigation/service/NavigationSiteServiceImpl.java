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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.portal.navigation.domain.jpa.NavigationSite;
import se.vgregion.portal.navigation.service.repository.NavigationSiteRepository;

/**
 * Implementation of {@link NavigationSiteService}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class NavigationSiteServiceImpl implements NavigationSiteService {

    private NavigationSiteRepository navigationSiteRepository;

    /**
     * Constructor.
     *
     * @param navigationSiteRepository the {@link NavigationSiteRepository}
     */
    @Autowired
    public NavigationSiteServiceImpl(NavigationSiteRepository navigationSiteRepository) {
        this.navigationSiteRepository = navigationSiteRepository;
    }

    @Override
    public List<NavigationSite> findNavigationSitesByCompanyId(long companyId) {
        return navigationSiteRepository.findNavigationSitesByCompanyId(companyId);
    }

    @Override
    public List<NavigationSite> findNavigationSitesByActiveState(long companyId, boolean isActive) {
        return navigationSiteRepository.findNavigationSitesByActiveState(companyId, isActive);
    }

    @Override
    public List<NavigationSite> findNavigationSitesByGroupId(long companyId, long groupId) {
        return navigationSiteRepository.findNavigationSitesByGroupId(companyId, groupId);
    }

    @Override
    public NavigationSite findNavigationSiteByOrderIndex(long companyId, int orderIndex) {
        return navigationSiteRepository.findNavigationSiteByOrderIndex(companyId, orderIndex);
    }

    @Override
    public List<NavigationSite> findNavigationSitesSub(long companyId) {
        return navigationSiteRepository.findNavigationSitesSub(companyId);
    }

    @Override
    @Transactional
    public void addNavigationSite(long userId, long companyId, long groupId) {
        NavigationSite navigationSite = new NavigationSite(userId, companyId, groupId);
        navigationSiteRepository.merge(navigationSite);
    }

    @Override
    @Transactional
    public void addNavigationSite(long userId, long companyId, long groupId, boolean isPrivateLayout, boolean isActive,
                                  int orderIndex) {
        NavigationSite navigationSite = new NavigationSite(userId, companyId, groupId, isPrivateLayout, isActive,
                orderIndex);
        navigationSiteRepository.merge(navigationSite);
    }

    @Override
    public NavigationSite findNavigationSitesMain(long companyId) {
        return findNavigationSiteByOrderIndex(companyId, 0);
    }


    @Override
    public Collection<NavigationSite> findAll() {
        return navigationSiteRepository.findAll();
    }

    @Override
    @Transactional
    public void remove(NavigationSite navigationSite) {
        navigationSiteRepository.remove(navigationSite);
    }

    @Override
    @Transactional
    public void removeAll() {
        Collection<NavigationSite> all = navigationSiteRepository.findAll();
        for (NavigationSite navigationSite : all) {
            navigationSiteRepository.remove(navigationSite);
        }
    }

}
