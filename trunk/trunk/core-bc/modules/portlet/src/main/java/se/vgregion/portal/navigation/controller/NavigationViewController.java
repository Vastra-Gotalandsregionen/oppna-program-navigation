package se.vgregion.portal.navigation.controller;

import java.util.List;
import java.util.Locale;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.navigation.domain.NavigationItem;
import se.vgregion.portal.navigation.domain.jpa.NavigationSite;
import se.vgregion.portal.navigation.service.NavigationSiteService;
import se.vgregion.portal.navigation.util.NavigationConstants;
import se.vgregion.portal.navigation.util.NavigationUtil;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class NavigationViewController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NavigationViewController.class.getName());
	protected NavigationSiteService navigationSiteService;
	
    @Autowired
    public NavigationViewController(NavigationSiteService navigationSiteService){
        this.navigationSiteService = navigationSiteService;
    }
	
	
    @RenderMapping()
    public String showNavigation(RenderRequest request, RenderResponse response, final ModelMap model) {

        LOGGER.info("showNavigation");
        
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		long scopeGroupId = themeDisplay.getScopeGroupId();
		long companyId = themeDisplay.getCompanyId();
		Locale locale = themeDisplay.getLocale();
		
		HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(request);
		
		boolean isSignedIn = themeDisplay.isSignedIn();
		Layout currentLayout = themeDisplay.getLayout();
		
		NavigationSite navigationSiteMain = navigationSiteService.findNavigationSitesMain(companyId);
		
		NavigationSite navigationSiteSub = null;
		List<NavigationSite> navigationSitesSub = navigationSiteService.findNavigationSitesSub(companyId);
		if(navigationSitesSub.size() > 0) {
			navigationSiteSub = navigationSitesSub.get(0);
		}
		
		boolean isRpPrivateLayout = false;
		boolean isVapPrivateLayout = false;
		
		long groupIdRp = 0;
		long groupIdVap = 0;
		
		if(navigationSiteMain != null) {
			groupIdRp = navigationSiteMain.getGroupId();
			isRpPrivateLayout = navigationSiteMain.getIsPrivateLayout();
		}
		
		if(navigationSiteSub != null) {
			groupIdVap = navigationSiteSub.getGroupId();
			isVapPrivateLayout = navigationSiteSub.getIsPrivateLayout();
		}
		
		List<NavigationItem> navigationItemsRP =  NavigationUtil.getGroupNavigationItems(
				groupIdRp, isSignedIn, currentLayout, isRpPrivateLayout, httpServletRequest, locale,
				themeDisplay.getPermissionChecker(), NavigationConstants.NAVIGATION_DEPTH_DEFAULT);
		
		List<NavigationItem> navigationItemsVAP =  NavigationUtil.getGroupNavigationItems(
				groupIdVap, isSignedIn, currentLayout, isVapPrivateLayout, httpServletRequest, locale,
				themeDisplay.getPermissionChecker(), NavigationConstants.NAVIGATION_DEPTH_DEFAULT);
		
		
		
		LOGGER.info("showNavigation - navigationItemsRP.size: " + navigationItemsRP.size());
		LOGGER.info("showNavigation - navigationItemsVAP.size: " + navigationItemsVAP.size());
		
		model.addAttribute("navigationItemsRP", navigationItemsRP);
		model.addAttribute("navigationItemsVAP", navigationItemsVAP);
		model.addAttribute("scopeGroupId", scopeGroupId);

        return "view";
    }

}
