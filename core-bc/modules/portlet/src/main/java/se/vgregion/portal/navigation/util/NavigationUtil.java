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

package se.vgregion.portal.navigation.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;

import com.liferay.expando.kernel.service.ExpandoValueLocalServiceUtil;
import se.vgregion.portal.breadcrumbs.domain.BreadcrumbsItem;
import se.vgregion.portal.navigation.domain.NavigationItem;
import se.vgregion.portal.navigation.domain.jpa.NavigationSite;

import com.liferay.portal.kernel.exception.NoSuchLayoutException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoTableConstants;
import com.liferay.expando.kernel.service.ExpandoColumnLocalServiceUtil;
import com.liferay.expando.kernel.service.ExpandoTableLocalServiceUtil;
import com.liferay.expando.kernel.service.ExpandoValueLocalServiceUtil;

public final class NavigationUtil {

    private static final Log LOGGER = LogFactoryUtil.getLog(NavigationUtil.class);

    private NavigationUtil() {

    }

    /**
     * Get {@link BreadcrumbsItem}s. The items will be those from the scopeLayout to the root layout.
     *
     * @param scopeLayout        the scopeLayout
     * @param locale             the locale
     * @param navigationSiteMain the main site
     * @param isSignedIn         whether the user is signed in
     * @return a {@link List} of {@link BreadcrumbsItem}s
     */
    public static List<BreadcrumbsItem> getBreadcrumbs(Layout scopeLayout, Locale locale,
                                                       NavigationSite navigationSiteMain, boolean isSignedIn) {

        ArrayList<BreadcrumbsItem> breadcrumbsItems = new ArrayList<BreadcrumbsItem>();

        BreadcrumbsItem scopeBreadcrumbsItem = getBreadcrumbsItem(scopeLayout, scopeLayout, locale);
        breadcrumbsItems.add(scopeBreadcrumbsItem);

        boolean foundRootLayout = scopeBreadcrumbsItem.getIsRootLayout();

        while (!foundRootLayout) {

            BreadcrumbsItem previousBreadcrumbsItem = breadcrumbsItems.get(breadcrumbsItems.size() - 1);

            Layout previousLayout = previousBreadcrumbsItem.getLayout();
            Layout currentLayout = getParentLayout(previousLayout);

            BreadcrumbsItem breadcrumbsItem = getBreadcrumbsItem(currentLayout, scopeLayout, locale);

            breadcrumbsItems.add(breadcrumbsItem);

            foundRootLayout = breadcrumbsItem.getIsRootLayout();
        }

        boolean isNavigationSiteMain = ((scopeLayout.getGroupId() == navigationSiteMain.getGroupId())
                && (scopeLayout.isPrivateLayout() == navigationSiteMain.getIsPrivateLayout()));

        if (!isNavigationSiteMain) {

            try {
                Group scopeGroup = scopeLayout.getGroup();
                long scopeGroupDefaultPlid = LayoutLocalServiceUtil.getDefaultPlid(scopeGroup.getGroupId(),
                        scopeLayout.isPrivateLayout());
                Layout scopeGroupDefaultLayout = LayoutLocalServiceUtil.getLayout(scopeGroupDefaultPlid);

                BreadcrumbsItem scopeGroupBreadcrumbsItem = getBreadcrumbsItem(scopeGroupDefaultLayout, scopeLayout,
                        locale);

                // Change name for this breadcrumb to group name
                scopeGroupBreadcrumbsItem.setName(scopeGroup.getName());

                breadcrumbsItems.add(scopeGroupBreadcrumbsItem);

            } catch (PortalException e) {
                LOGGER.error(e, e);
            } catch (SystemException e) {
                LOGGER.error(e, e);
            }
        }

        if (isSignedIn) {
            // Add base breadcrumb (start page for main navigation site)

            long mainGroupId = navigationSiteMain.getGroupId();
            boolean isMainPrivate = navigationSiteMain.getIsPrivateLayout();

            try {
                long mainGroupDefaultPlid = LayoutLocalServiceUtil.getDefaultPlid(mainGroupId, isMainPrivate);
                Layout mainGroupDefaultLayout = LayoutLocalServiceUtil.getLayout(mainGroupDefaultPlid);
                BreadcrumbsItem mainGroupBreadcrumbsItem = getBreadcrumbsItem(mainGroupDefaultLayout, scopeLayout,
                        locale);

                breadcrumbsItems.add(mainGroupBreadcrumbsItem);

            } catch (PortalException e) {
                LOGGER.error(e, e);
            } catch (SystemException e) {
                LOGGER.error(e, e);
            }
        }

        // Reverse order
        Collections.reverse(breadcrumbsItems);

        return breadcrumbsItems;
    }

    private static BreadcrumbsItem getBreadcrumbsItem(Layout layout, Layout scopeLayout, Locale locale) {

        BreadcrumbsItem breadcrumbsItem = new BreadcrumbsItem();

        boolean isSelected = (layout.getPlid() == scopeLayout.getPlid());
        String name = layout.getName(locale);
        boolean isRootLayout = (layout.isRootLayout());

        String layoutURLPrefix = getURLPrefixFromLayout(layout);

        if (name.equals("")) {
            name = "empty";
        }

        String url = layoutURLPrefix + layout.getFriendlyURL();

        breadcrumbsItem.setIsSelected(isSelected);
        breadcrumbsItem.setLayout(layout);
        breadcrumbsItem.setName(name);
        breadcrumbsItem.setIsRootLayout(isRootLayout);
        breadcrumbsItem.setUrl(url);

        return breadcrumbsItem;
    }

    /**
     * Get the {@link Group}s within a company.
     *
     * @param companyId the companyId
     * @return a {@link List} of {@link Group}s
     */
    public static List<Group> getCompanyGroups(long companyId) {

        try {
            int companyGroupsCount = GroupLocalServiceUtil.getCompanyGroupsCount(companyId);

            List<Group> companyGroups = GroupLocalServiceUtil.getCompanyGroups(companyId, 0, companyGroupsCount);

            return companyGroups;
        } catch (SystemException e) {
            LOGGER.error(e, e);
        }

        ArrayList<Group> emptyList = new ArrayList<Group>();
        return emptyList;
    }

    /**
     * Get the groupId from an expando column for a company.
     *
     * @param companyId  the companyId
     * @param columnName the columnName
     * @return the groupId
     */
    public static long getExpandoGroupId(long companyId, String columnName) {

        long groupId = 0;

        try {

            long defaultValue = 0;

            groupId = ExpandoValueLocalServiceUtil.getData(
                    companyId, Company.class.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName, companyId,
                    defaultValue);

        } catch (SystemException e) {
            LOGGER.error(e, e);
        } catch (PortalException e) {
            LOGGER.error(e, e);
        }

        return groupId;
    }

    /**
     * Get the groupId for the Regionportal community.
     *
     * @param companyId the companyid
     * @return the groupId
     */
    public static long getExpandoGroupIdRp(long companyId) {
        return getExpandoGroupId(companyId, NavigationConstants.EXPANDO_COLUMN_GROUP_ID_RP);
    }

    /**
     * Get the groupId for the VAP community.
     *
     * @param companyId the companyid
     * @return the groupId
     */
    public static long getExpandoGroupIdVap(long companyId) {
        return getExpandoGroupId(companyId, NavigationConstants.EXPANDO_COLUMN_GROUP_ID_VAP);
    }

    // todo
    public static NavigationItem getGroupNavigationItem(long groupId, boolean isSignedIn, Layout currentLayout,
                                                        boolean privateLayout, HttpServletRequest httpServletRequest,
                                                        Locale locale, PermissionChecker permissionChecker, int depth) {

        List<NavigationItem> childItems = getGroupNavigationItems(
                groupId, isSignedIn, currentLayout, privateLayout, httpServletRequest, locale, permissionChecker,
                depth);

        NavigationItem firstChild = null;

        if (childItems.size() > 0) {
            firstChild = childItems.get(0);
        }

        NavigationItem navigationItem = new NavigationItem();

        /*
              FLAG this is where I left off. Was going to set attributes for main NavigationItem for a subsite
              Then add the childItems.
              Then update view controller to use this method
              Then update the view to take into account that we have another level (top level) for sub site navigation
              items.

           */

        if (firstChild != null) {
            /*
                *
                * 	private List<NavigationItem> children;
                   private boolean isSelected;
                   private boolean isChildSelected;
                   private Layout layout;
                   private String name;
                   private String title;
                   private String url;
                   private String target;
                *
                * */

            Layout layout = firstChild.getLayout();


        }

        return navigationItem;
    }

    /**
     * Returns Navigation Items for a group.
     *
     * @param groupId            The groupId
     * @param isSignedIn         Whether the user is signed in
     * @param currentLayout      The current layout
     * @param privateLayout      Whether to use private or public layouts
     * @param httpServletRequest The HttpServletRequest
     * @param locale             The Locale (retrieved from for example themeDisplay)
     * @param permissionChecker  The Liferay PermissionChecker (retrieved from themeDisplay)
     * @param depth              The depth of the navigation structure returned. If set to -1, return the full depth
     *                           (i.e. all children's children and so on)
     * @return List<NavigationItem>
     */
    public static List<NavigationItem> getGroupNavigationItems(long groupId, boolean isSignedIn, Layout currentLayout,
                                                               boolean privateLayout,
                                                               HttpServletRequest httpServletRequest,
                                                               Locale locale, PermissionChecker permissionChecker,
                                                               int depth) {

        ArrayList<NavigationItem> navigationItems = new ArrayList<NavigationItem>();

        // TODO - implement depth parameter to control how deep the navigation is rolled up

        try {
            // First level layouts
            List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(groupId, privateLayout, 0);

            List<Layout> filteredLayouts = filterLayouts(layouts, permissionChecker, isSignedIn);
            
            //filteredLayouts = filterLayoutsWithTypeUrl(filteredLayouts, permissionChecker, httpServletRequest);

            for (Layout layout : filteredLayouts) {

                NavigationItem navigationItem = createNavigationItem(layout, currentLayout, locale, httpServletRequest,
                        permissionChecker, isSignedIn);

                int depthCurrent = 1;

                navigationItem = addChildNavigation(
                        navigationItem, layout, currentLayout, locale, httpServletRequest, permissionChecker,
                        isSignedIn, depth, depthCurrent);

                navigationItems.add(navigationItem);
            }

        } catch (SystemException e) {
            LOGGER.error(e, e);
        }

        return navigationItems;
    }

    /**
     * Set the groupId for a company in an expando column.
     *
     * @param companyId  the company
     * @param groupId    the groupdId
     * @param columnName the columnName
     */
    public static void setExpandoGroupId(long companyId, long groupId, String columnName) {

        try {
            // First make sure that the column exists
            setupCompanyExpandoColumn(companyId, columnName);

            // Next save data
            ExpandoValueLocalServiceUtil.addValue(
                    companyId, Company.class.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName, companyId,
                    groupId);

        } catch (SystemException e) {
            LOGGER.error(e, e);
        } catch (PortalException e) {
            LOGGER.error(e, e);
        }
    }

    /**
     * Set the groupId for the Regionportal community in a given company.
     *
     * @param companyId the companyId
     * @param groupId   the groupId
     */
    public static void setGroupIdRp(long companyId, long groupId) {

        setExpandoGroupId(companyId, groupId, NavigationConstants.EXPANDO_COLUMN_GROUP_ID_RP);
    }

    /**
     * Set the groupId for the VAP community in a given company.
     *
     * @param companyId the companyId
     * @param groupId   the groupId
     */
    public static void setGroupIdVap(long companyId, long groupId) {

        setExpandoGroupId(companyId, groupId, NavigationConstants.EXPANDO_COLUMN_GROUP_ID_VAP);
    }

    // Recursive
    private static NavigationItem addChildNavigation(NavigationItem navigationItem, Layout layout, Layout currentLayout,
                                                     Locale locale, HttpServletRequest httpServletRequest,
                                                     PermissionChecker permissionChecker, boolean isSignedIn,
                                                     int depthWanted, int depthCurrent) {

        List<NavigationItem> navigationItems = new ArrayList<NavigationItem>();

        boolean hasNavigationItemChildren = hasNavigationItemChildren(layout, permissionChecker);

        if (hasNavigationItemChildren && depthCurrent < depthWanted) {
            navigationItems = createNavigationItemChildren(layout, currentLayout, locale, httpServletRequest,
                    permissionChecker, isSignedIn);

            depthCurrent++;

            for (NavigationItem childNavigationItem : navigationItems) {

                Layout childLayout = childNavigationItem.getLayout();

                boolean hasChildNavigationItemChildren = hasNavigationItemChildren(childLayout, permissionChecker);

                if (hasChildNavigationItemChildren) {
                    childNavigationItem = addChildNavigation(
                            childNavigationItem, childLayout, currentLayout, locale, httpServletRequest,
                            permissionChecker, isSignedIn, depthWanted, depthCurrent);
                }

            }

        }

        navigationItem.setChildren(navigationItems);

        return navigationItem;
    }
    
    /**
     * Updates a list of breadcrumbitems to use virtual host in the breadcrumb url
     * if a virtual host is used and the client is currently on the virtual host
     * @param companyId the companyId
     * @param groupId   the groupId
     */
    public static List<BreadcrumbsItem> updateBreadcrumbsWithVirtualHost(List<BreadcrumbsItem> breadcrumbs, RenderRequest renderRequest) {
    	
        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
        Group scopeGroup = themeDisplay.getScopeGroup();
        Layout scopeLayout = themeDisplay.getLayout();
        
        String requestCurrentURL = (String)renderRequest.getAttribute("CURRENT_URL");
        String requestFriendlyURL = (String)renderRequest.getAttribute("FRIENDLY_URL");

        if (requestCurrentURL != null && requestFriendlyURL != null) {
            boolean clientOnVirtualHost = !requestCurrentURL.contains(requestFriendlyURL);
            if (clientOnVirtualHost) {

                for (BreadcrumbsItem breadcrumb : breadcrumbs) {
                    String breadcrumbURL = breadcrumb.getUrl();

                    String layoutSetFriendyURL = "/web";

                    if (scopeLayout.isPrivateLayout()) {
                        layoutSetFriendyURL = "/group";
                    }

                    String scopeGroupFriendlyURL = scopeGroup.getFriendlyURL();

                    String urlPrefix = layoutSetFriendyURL + scopeGroupFriendlyURL;

                    breadcrumbURL = breadcrumbURL.replace(urlPrefix, "");

                    breadcrumb.setUrl(breadcrumbURL);
                }
            }
        }
    	
    	return breadcrumbs;
    }


    private static NavigationItem createNavigationItem(Layout layout, Layout currentLayout, Locale locale,
                                                       HttpServletRequest httpServletRequest,
                                                       PermissionChecker permissionChecker, boolean isSignedIn) {
        NavigationItem navigationItem = new NavigationItem();
        
        //layout.isTypeURL()

        String name = HtmlUtil.escape(layout.getName(locale));
        String title = layout.getTitle(locale);
        String target = layout.getTarget();
        boolean isTypeUrl = layout.isTypeURL();
        boolean isTypeLinkToLayout = layout.isTypeLinkToLayout();

        navigationItem.setLayout(layout);
        navigationItem.setName(name);
        navigationItem.setTitle(title);
        navigationItem.setTarget(target);
        navigationItem.setIsTypeUrl(isTypeUrl);
        navigationItem.setIsTypeLinkToLayout(isTypeLinkToLayout);

        String url;
        try {
            url = layout.getResetLayoutURL(httpServletRequest);

            boolean isSelected = currentLayout.isSelected(true, layout, layout.getAncestorPlid());
            boolean isChildSelected = layout.isChildSelected(true, currentLayout);

            navigationItem.setIsSelected(isSelected);
            navigationItem.setIsChildSelected(isChildSelected);

            navigationItem.setUrl(url);
        } catch (PortalException e) {
            LOGGER.error(e, e);
        } catch (SystemException e) {
            LOGGER.error(e, e);
        }

        return navigationItem;
    }

    private static List<NavigationItem> createNavigationItemChildren(Layout layout, Layout currentLayout, Locale locale,
                                                                     HttpServletRequest httpServletRequest,
                                                                     PermissionChecker permissionChecker,
                                                                     boolean isSignedIn) {
        ArrayList<NavigationItem> navigationItems = new ArrayList<NavigationItem>();

        try {
            List<Layout> childLayouts = layout.getChildren(permissionChecker);

            for (Layout childLayout : childLayouts) {
                NavigationItem navigationItem = createNavigationItem(childLayout, currentLayout, locale,
                        httpServletRequest, permissionChecker, isSignedIn);

                navigationItems.add(navigationItem);
            }

        } catch (PortalException e) {
            LOGGER.error(e, e);
        } catch (SystemException e) {
            LOGGER.error(e, e);
        }

        return navigationItems;
    }

    private static boolean hasNavigationItemChildren(Layout layout, PermissionChecker permissionChecker) {
        boolean hasChildren = false;

        try {
            hasChildren = layout.hasChildren();
        } catch (SystemException e) {
            LOGGER.error(e, e);
        }

        return hasChildren;
    }

    private static List<Layout> filterLayouts(List<Layout> layouts, PermissionChecker permissionChecker,
                                              boolean isSignedIn) {
        ArrayList<Layout> layoutsList = new ArrayList<Layout>();

        for (Layout layout : layouts) {

            try {
            	
            	long userId = permissionChecker.getUserId();
            	long groupId = layout.getGroupId();
            	
            	boolean isCommunityMember = GroupLocalServiceUtil.hasUserGroup(userId, groupId);

            	boolean hasPermissions = LayoutPermissionUtil.contains(permissionChecker, layout,
                        NavigationConstants.LAYOUT_ACTION_ID_VIEW);
            	
                boolean isHidden = layout.isHidden();

                boolean hasSignedInPermission = layout.isPublicLayout() || (layout.isPrivateLayout() && isSignedIn);

                if(isSignedIn) {
                    if (isCommunityMember && hasPermissions && !isHidden && hasSignedInPermission) {
                        layoutsList.add(layout);
                    }
                } else {
                    
                    if (hasPermissions && !isHidden && hasSignedInPermission) {
                        layoutsList.add(layout);
                    }
                	
                }
                
            } catch (PortalException e) {
                LOGGER.error(e, e);
            } catch (SystemException e) {
                LOGGER.error(e, e);
            }
        }

        return layoutsList;
    }

    // Not finished yet. Also, not used yet.
    private static List<Layout> filterLayoutsWithTypeUrl(List<Layout> layouts, PermissionChecker permissionChecker, HttpServletRequest httpServletRequest) {
    	ArrayList<Layout> layoutsList = new ArrayList<Layout>();
    	
    	for(Layout layout : layouts) {
    		
    		if(layout.isTypeURL()) {
    			try {
					String resetLayoutUrl = layout.getResetLayoutURL(httpServletRequest);
					
					//System.out.println("NavigationUtil - filterLayoutsWithTypeUrl - found URL layout with resetLayoutUrl: " + resetLayoutUrl);
					//boolean isAbsoluteUrl = resetLayoutUrl.startsWith("http://");
					//System.out.println("NavigationUtil - filterLayoutsWithTypeUrl - isAbsoluteUrl: " + isAbsoluteUrl);
					
					String portalURL = PortalUtil.getPortalURL(httpServletRequest);
					System.out.println("NavigationUtil - filterLayoutsWithTypeUrl - portalURL: " + portalURL);
					
					
				} catch (PortalException e) {
					LOGGER.error(e, e);
				} catch (SystemException e) {
					LOGGER.error(e, e);
				}
    			
    			//boolean isFullUrl = 
    			
    			layoutsList.add(layout);
    		} else {
    			layoutsList.add(layout);	
    		}
    	}
    	
    	return layoutsList;
    }

    private static String getURLPrefix(Group group, boolean isPrivate) {

        String urlPrefix = "";

        String groupFriendlyURL = group.getFriendlyURL();

        String visibilityFriendlyURL = isPrivate ? "/group" : "/web";

        urlPrefix = visibilityFriendlyURL + groupFriendlyURL;

        return urlPrefix;
    }

    private static String getURLPrefixFromLayout(Layout layout) {

        String urlPrefix = "";

        try {
            Group group = layout.getGroup();

            urlPrefix = getURLPrefix(group, layout.isPrivateLayout());

        } catch (SystemException e) {
            LOGGER.error(e, e);
        }

        return urlPrefix;
    }

    private static String getURLPrefixFromNavigationSite(NavigationSite navigationSite) {

        String urlPrefix = "";

        try {
            long groupId = navigationSite.getGroupId();
            Group group = GroupLocalServiceUtil.getGroup(groupId);

            urlPrefix = getURLPrefix(group, navigationSite.getIsPrivateLayout());

        } catch (PortalException e) {
            LOGGER.error(e, e);
        } catch (SystemException e) {
            LOGGER.error(e, e);
        }

        return urlPrefix;
    }

    private static Layout getParentLayout(Layout layout) {

        Layout parentLayout = null;

        try {
            long parentPlid = layout.getParentPlid();

            try {
                parentLayout = LayoutLocalServiceUtil.getLayout(parentPlid);
            } catch (NoSuchLayoutException nsle) {
                // Do nothing
                LOGGER.debug(nsle.getMessage());
            }
        } catch (PortalException e) {
            LOGGER.error(e, e);
        } catch (SystemException e) {
            LOGGER.error(e, e);
        }

        return parentLayout;
    }

    private static void setupCompanyExpandoColumn(long companyId, String columnName) throws SystemException,
            PortalException {

        ExpandoTable table = null;

        try {
            table = ExpandoTableLocalServiceUtil.addDefaultTable(companyId, Company.class.getName());

            LOGGER.info("Navigationutil - setupCompanyExpandoColumn - foundTable");
        } catch (Exception e) {
            table = ExpandoTableLocalServiceUtil.getDefaultTable(companyId, Company.class.getName());
        }

        try {
            ExpandoColumnLocalServiceUtil.addColumn(table.getTableId(), columnName, ExpandoColumnConstants.LONG);
        } catch (Exception e) {
            // Do nothing (column already exists)
            LOGGER.debug(e.getMessage());
        }
    }
}
