package se.vgregion.portal.navigation.service.repository;

import se.vgregion.dao.domain.patterns.repository.db.jpa.JpaRepository;
import se.vgregion.portal.navigation.domain.jpa.NavigationSite;

/**
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface JpaNavigationSiteRepository extends JpaRepository<NavigationSite, Long, Long>, NavigationSiteRepository {

}
