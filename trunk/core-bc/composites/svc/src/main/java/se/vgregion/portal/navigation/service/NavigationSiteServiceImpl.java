package se.vgregion.portal.navigation.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.portal.navigation.domain.jpa.NavigationSite;
import se.vgregion.portal.navigation.service.repository.NavigationSiteRepository;

/**
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class NavigationSiteServiceImpl implements NavigationSiteService {

    private NavigationSiteRepository navigationSiteRepository;

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

    @Transactional
    public void addNavigationSite(long userId, long companyId, long groupId) {
        NavigationSite navigationSite = new NavigationSite(userId, companyId, groupId);
        navigationSiteRepository.merge(navigationSite);
    }
    
    @Override
    @Transactional
    public void addNavigationSite(long userId, long companyId, long groupId, boolean isPrivateLayout, boolean isActive, int orderIndex) {
        NavigationSite navigationSite = new NavigationSite(userId, companyId, groupId, isPrivateLayout, isActive, orderIndex);
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
