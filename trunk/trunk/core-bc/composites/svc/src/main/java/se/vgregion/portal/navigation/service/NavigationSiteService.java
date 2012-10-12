package se.vgregion.portal.navigation.service;

import java.util.Collection;
import java.util.List;

import se.vgregion.portal.navigation.domain.jpa.NavigationSite;

/**
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface NavigationSiteService {

    void addNavigationSite(long userId, long companyId, long groupId);
    
    void addNavigationSite(long userId, long companyId, long groupId, boolean isPrivateLayout, boolean isActive, int orderIndex);
	
	Collection<NavigationSite> findAll();
	
	List<NavigationSite> findNavigationSitesByCompanyId(long companyId);
    
	List<NavigationSite> findNavigationSitesByActiveState(long companyId, boolean isActive);
	
	List<NavigationSite> findNavigationSitesByGroupId(long companyId, long groupId);
	
	List<NavigationSite> findNavigationSitesSub(long companyId);
	
	NavigationSite findNavigationSitesMain(long companyId);
	
	NavigationSite findNavigationSiteByOrderIndex(long companyId, int orderIndex);
	
    

    void remove(NavigationSite navigationSite);

    void removeAll();
}
