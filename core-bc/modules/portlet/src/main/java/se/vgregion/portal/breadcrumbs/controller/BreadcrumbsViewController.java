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

package se.vgregion.portal.breadcrumbs.controller;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.portal.breadcrumbs.domain.BreadcrumbsItem;
import se.vgregion.portal.navigation.domain.jpa.NavigationSite;
import se.vgregion.portal.navigation.service.NavigationSiteService;
import se.vgregion.portal.navigation.util.NavigationUtil;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.List;
import java.util.Locale;

/**
 * Controller for the breadcrumbs portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class BreadcrumbsViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BreadcrumbsViewController.class.getName());
    private NavigationSiteService navigationSiteService;

    /**
     * Constructor.
     *
     * @param navigationSiteService the {@link NavigationSiteService}
     */
    @Autowired
    public BreadcrumbsViewController(NavigationSiteService navigationSiteService) {
        this.navigationSiteService = navigationSiteService;
    }

    /**
     * Load the breadcrumbs and add them to the {@link ModelMap} and show the view.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @return the view
     */
    @RenderMapping()
    public String showBreadcrumbs(RenderRequest request, RenderResponse response, final ModelMap model) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        Locale locale = themeDisplay.getLocale();

        boolean isSignedIn = themeDisplay.isSignedIn();
        Layout scopeLayout = themeDisplay.getLayout();

        NavigationSite navigationSiteMain = navigationSiteService.findNavigationSitesMain(companyId);

        if (navigationSiteMain == null) {
            // Fallback on current site.
            navigationSiteMain = new NavigationSite(
                    themeDisplay.getUserId(),
                    themeDisplay.getCompanyId(),
                    themeDisplay.getSiteGroupId(),
                    themeDisplay.getLayout().isPrivateLayout(),
                    true,
                    0
            );
        }

        List<BreadcrumbsItem> breadcrumbs = NavigationUtil.getBreadcrumbs(scopeLayout, locale, navigationSiteMain,
                isSignedIn);
        
        breadcrumbs = NavigationUtil.updateBreadcrumbsWithVirtualHost(breadcrumbs, request);

        model.addAttribute("breadcrumbs", breadcrumbs);

        return "view";
    }

}
