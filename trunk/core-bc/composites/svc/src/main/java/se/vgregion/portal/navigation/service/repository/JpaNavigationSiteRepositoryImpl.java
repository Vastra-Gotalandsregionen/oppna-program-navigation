package se.vgregion.portal.navigation.service.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.navigation.domain.jpa.NavigationSite;

/**
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class JpaNavigationSiteRepositoryImpl extends DefaultJpaRepository<NavigationSite, Long> implements JpaNavigationSiteRepository {

	@Override
    public List<NavigationSite> findNavigationSitesByCompanyId(long companyId) {
		String queryString = "SELECT n FROM NavigationSite n WHERE n.companyId = ?1 ORDER BY n.orderIndex ASC";
		Object[] queryObject = new Object[]{companyId};
		
		List<NavigationSite> navigationSites = findByQuery(queryString, queryObject);
		
		return navigationSites;
    }
	
	@Override
    public List<NavigationSite> findNavigationSitesSub(long companyId) {
		String queryString = "SELECT n FROM NavigationSite n WHERE n.companyId = ?1 AND n.orderIndex != ?2 ORDER BY n.orderIndex ASC";
		
		int orderIndex = 0;
		
		Object[] queryObject = new Object[]{companyId, orderIndex};
		
		List<NavigationSite> navigationSites = findByQuery(queryString, queryObject);
		
		return navigationSites;
    }

    @Override
    public List<NavigationSite> findNavigationSitesByGroupId(long companyId, long groupId) {
		String queryString = "SELECT n FROM NavigationSite n WHERE n.companyId = ?1 AND n.groupId = ?2 ORDER BY n.orderIndex ASC";
		Object[] queryObject = new Object[]{companyId, groupId};
		
		List<NavigationSite> navigationSites = findByQuery(queryString, queryObject);
		
		return navigationSites;
    }
    
    @Override
    public List<NavigationSite> findNavigationSitesByActiveState(long companyId, boolean isActive) {
		String queryString = "SELECT n FROM NavigationSite n WHERE n.companyId = ?1 AND n.isActive = ?2 ORDER BY n.orderIndex ASC";
		Object[] queryObject = new Object[]{companyId, isActive};
		
		List<NavigationSite> navigationSites = findByQuery(queryString, queryObject);
		
		return navigationSites;
    }
    
    @Override
    public NavigationSite findNavigationSiteByOrderIndex(long companyId, int orderIndex) {
		String queryString = "SELECT n FROM NavigationSite n WHERE n.companyId = ?1 AND n.orderIndex = ?2 ORDER BY n.orderIndex ASC";
		Object[] queryObject = new Object[]{companyId, orderIndex};
		
		List<NavigationSite> navigationSites = findByQuery(queryString, queryObject);
        
        NavigationSite navigationSite = null;
        
        if(navigationSites.size() > 0) {
        	navigationSite = navigationSites.get(0);
        }
        
        return navigationSite;
    }
    
}
