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

package se.vgregion.portal.navigation.controller;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.navigation.domain.jpa.NavigationSite;
import se.vgregion.portal.navigation.service.NavigationSiteService;
import se.vgregion.portal.navigation.util.NavigationUtil;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.theme.ThemeDisplay;

/**
 * Controller for the edit view for the navigation portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping("EDIT")
public class NavigationEditController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationEditController.class.getName());
    private NavigationSiteService navigationSiteService;

    /**
     * Constructor.
     *
     * @param navigationSiteService the {@link NavigationSiteService}
     */
    @Autowired
    public NavigationEditController(NavigationSiteService navigationSiteService) {
        this.navigationSiteService = navigationSiteService;
    }

    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping("saveRpNavigationSettings")
    public final void saveRpNavigationSettings(
            ActionRequest request, ActionResponse response, final ModelMap model) {

        LOGGER.info("saveRpNavigationSettings");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long userId = themeDisplay.getUserId();

        List<NavigationSite> navigationSites = navigationSiteService.findNavigationSitesByCompanyId(companyId);

        long groupIdMainSite = ParamUtil.getLong(request, "groupIdMainSite", 0);
        boolean isPrivateMainSite = ParamUtil.getBoolean(request, "isPrivateMainSite", false);

        long groupIdSubSite = ParamUtil.getLong(request, "groupIdSubSite", 0);
        boolean isPrivateSubSite = ParamUtil.getBoolean(request, "isPrivateSubSite", false);

        LOGGER.info("saveRpNavigationSettings groupIdMainSite: " + groupIdMainSite);
        LOGGER.info("saveRpNavigationSettings isPrivateMainSite: " + isPrivateMainSite);

        if (navigationSites.size() > 0) {
            navigationSiteService.removeAll();
        }

        boolean isActive = true;
        int orderIndexMainSite = 0;
        int orderIndexSubSite = 1;

        if (groupIdMainSite != 0) {
            navigationSiteService.addNavigationSite(userId, companyId, groupIdMainSite, isPrivateMainSite, isActive,
                    orderIndexMainSite);
        }

        if (groupIdSubSite != 0) {
            navigationSiteService.addNavigationSite(userId, companyId, groupIdSubSite, isPrivateSubSite, isActive,
                    orderIndexSubSite);
        }


    }


    /**
     * Method handling Render request, fetching portlet preferences for view.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @return jsp url for view
     */
    @RenderMapping
    public final String showEdit(RenderRequest request, RenderResponse response, final ModelMap model) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        long companyId = themeDisplay.getCompanyId();

        long groupIdRp = NavigationUtil.getExpandoGroupIdRp(companyId);
        long groupIdVap = NavigationUtil.getExpandoGroupIdVap(companyId);

        List<Group> companyGroups = NavigationUtil.getCompanyGroups(companyId);

        NavigationSite mainNavigationSite = navigationSiteService.findNavigationSitesMain(companyId);

        NavigationSite subNavigationSite = null;
        List<NavigationSite> subNavigationSites = navigationSiteService.findNavigationSitesSub(companyId);

        LOGGER.info("showEdit - subNavigationSites.size: " + subNavigationSites.size());

        if (subNavigationSites.size() > 0) {
            subNavigationSite = subNavigationSites.get(0);
        }

        model.addAttribute("mainNavigationSite", mainNavigationSite);
        model.addAttribute("subNavigationSite", subNavigationSite);
        model.addAttribute("subNavigationSites", subNavigationSites);

        model.addAttribute("companyGroups", companyGroups);
        model.addAttribute("groupIdRp", groupIdRp);
        model.addAttribute("groupIdVap", groupIdVap);

        return "edit";
    }


}
