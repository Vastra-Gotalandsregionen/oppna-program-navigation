package se.vgregion.portal.navigation.service.repository;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.navigation.domain.jpa.NavigationSite;

/**
 * Repository interface for managing {@code NavigationSite}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface NavigationSiteRepository extends Repository<NavigationSite, Long> {

    /**
     * Find all {@link NavigationSite}s for a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link NavigationSite}s
     */
    List<NavigationSite> findNavigationSitesByCompanyId(long companyId);

    /**
     * Find all {@link NavigationSite}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return a {@link List} of {@link NavigationSite}s
     */
    List<NavigationSite> findNavigationSitesByGroupId(long companyId, long groupId);

    /**
     * Find all {@link NavigationSite}s with the correct status for a company.
     *
     * @param companyId the companyid
     * @param isActive  whether the {@link NavigationSite} is active
     * @return a {@link List} of {@link NavigationSite}s
     */
    List<NavigationSite> findNavigationSitesByActiveState(long companyId, boolean isActive);

    /**
     * Find all {@link NavigationSite}s except for the main site within a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link NavigationSite}s
     */
    List<NavigationSite> findNavigationSitesSub(long companyId);

    /**
     * Find a {@link NavigationSite} with the specific orderIndex within a company.
     *
     * @param companyId  the companyId
     * @param orderIndex the orderIndex
     * @return the {@link NavigationSite} or <code>null</code> if none found
     */
    NavigationSite findNavigationSiteByOrderIndex(long companyId, int orderIndex);
}
