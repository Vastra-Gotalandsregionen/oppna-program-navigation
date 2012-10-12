package se.vgregion.portal.navigation.service.repository;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.navigation.domain.jpa.NavigationSite;

/**
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface NavigationSiteRepository extends Repository<NavigationSite, Long> {
	
	List<NavigationSite> findNavigationSitesByCompanyId(long companyId);
	
	List<NavigationSite> findNavigationSitesByGroupId(long companyId, long groupId);
    
    List<NavigationSite> findNavigationSitesByActiveState(long companyId, boolean isActive);

    List<NavigationSite> findNavigationSitesSub(long companyId);
    
    NavigationSite findNavigationSiteByOrderIndex(long companyId, int orderIndex);
}
