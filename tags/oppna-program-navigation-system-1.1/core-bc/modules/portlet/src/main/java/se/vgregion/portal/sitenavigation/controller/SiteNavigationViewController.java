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

package se.vgregion.portal.sitenavigation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.navigation.domain.NavigationItem;
import se.vgregion.portal.navigation.util.NavigationConstants;
import se.vgregion.portal.navigation.util.NavigationUtil;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.expando.model.ExpandoBridge;

/**
 * Controller class for the view mode in navigation portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class SiteNavigationViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteNavigationViewController.class.getName());


    /**
     * The default render method. Load the correct {@link NavigationItem}s and add as request attributes.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @return the view
     */
    @RenderMapping()
    public String showNavigation(RenderRequest request, RenderResponse response, final ModelMap model) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        Locale locale = themeDisplay.getLocale();
        boolean isSignedIn = themeDisplay.isSignedIn();
        Layout layout = themeDisplay.getLayout();
        boolean isPrivateLayout = layout.isPrivateLayout();
        
        HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(request);
        
        List<NavigationItem> navigationItems = NavigationUtil.getGroupNavigationItems(
                scopeGroupId, isSignedIn, layout, isPrivateLayout, httpServletRequest, locale,
                themeDisplay.getPermissionChecker(), NavigationConstants.NAVIGATION_DEPTH_DEFAULT);
        
        navigationItems = navigationItems.subList(1, navigationItems.size());
        navigationItems = populateNavigationCustomFields(navigationItems); 
        
        model.addAttribute("navigationItems", navigationItems);

        return "view";
    }
    
    private List<NavigationItem> populateNavigationCustomFields (List<NavigationItem> navigationItems) {
    	
    	for(NavigationItem navigationItem : navigationItems) {
    		
    		Layout layout = navigationItem.getLayout();
    		ExpandoBridge expandoBridge = layout.getExpandoBridge();
    		
    		String navigationDescription = (String)expandoBridge.getAttribute("navigationDescription");
    		
    		if(!navigationDescription.equals("")) {
    			navigationItem.setExpandoNavigationDescription(navigationDescription);	
    		}
    		
    		String navigationCssClass = (String)expandoBridge.getAttribute("navigationCssClass");
    		
    		if(!navigationCssClass.equals("")) {
    			navigationItem.setExpandoNavigationCssClass(navigationCssClass);	
    		}
    		
    	}
    	
    	return navigationItems;
    }
    
    /*
    private List<NavigationItem> getNavigationItems(long groupId, boolean privateLayout, long parentLayoutId) {
    	
    	ArrayList<NavigationItem> navigationItems = new ArrayList<NavigationItem>();
    	
    	try {
			List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(groupId, privateLayout, parentLayoutId);
			
			for(Layout layout : layouts) {
				//NavigationItem navigationItem = new
			}
			
		} catch (SystemException e) {
			e.printStackTrace();
		}
    	
    	return navigationItems;
    }
    */
    
}
