package se.vgregion.portal.breadcrumbs.controller;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
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
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        Locale locale = themeDisplay.getLocale();

        boolean isSignedIn = themeDisplay.isSignedIn();
        Layout scopeLayout = themeDisplay.getLayout();

        NavigationSite navigationSiteMain = navigationSiteService.findNavigationSitesMain(companyId);

        List<BreadcrumbsItem> breadcrumbs = NavigationUtil.getBreadcrumbs(scopeLayout, locale, navigationSiteMain,
                isSignedIn);
        
        breadcrumbs = NavigationUtil.updateBreadcrumbsWithVirtualHost(breadcrumbs, request);

        model.addAttribute("breadcrumbs", breadcrumbs);

        return "view";
    }

}
