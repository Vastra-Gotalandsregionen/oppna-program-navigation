package se.vgregion.portal.navigation.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.naming.ldap.HasControls;
import javax.servlet.http.HttpServletRequest;

import se.vgregion.portal.navigation.domain.NavigationItem;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;

public class NavigationUtil {

	private static Log LOG = LogFactoryUtil.getLog(NavigationUtil.class);
	
	public static List<Group> getCompanyGroups(long companyId) {
		
		try {
			int companyGroupsCount = GroupLocalServiceUtil.getCompanyGroupsCount(companyId);
			
			List<Group> companyGroups = GroupLocalServiceUtil.getCompanyGroups(companyId, 0, companyGroupsCount);
			
			return companyGroups;
		} catch (SystemException e) {
			LOG.error(e, e);
		}
		
		ArrayList<Group> emptyList = new ArrayList<Group>();
		return emptyList;
	}
	
	public static long getExpandoGroupId(long companyId, String columnName) {

		long groupId = 0;
		
		try {
			
			long defaultValue = 0;
			
			groupId = ExpandoValueLocalServiceUtil.getData(
					companyId, Company.class.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName, companyId, defaultValue);
			
		} catch (SystemException e) {
			LOG.error(e, e);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			LOG.error(e, e);
		}
		
		return groupId;
	}
	
	public static long getExpandoGroupIdRp(long companyId) {
		return getExpandoGroupId(companyId, NavigationConstants.EXPANDO_COLUMN_GROUP_ID_RP);
	}
	
	public static long getExpandoGroupIdVap(long companyId) {
		return getExpandoGroupId(companyId, NavigationConstants.EXPANDO_COLUMN_GROUP_ID_VAP);
	}
	
	public static NavigationItem getGroupNavigationItem(long groupId, boolean isSignedIn, Layout currentLayout, boolean privateLayout, HttpServletRequest httpServletRequest,
			Locale locale, PermissionChecker permissionChecker, int depth) {
		
		List<NavigationItem> childItems = getGroupNavigationItems(
				groupId, isSignedIn, currentLayout, privateLayout, httpServletRequest, locale, permissionChecker, depth);
		
		NavigationItem firstChild = null;
		
		if(childItems.size() > 0) {
			firstChild = childItems.get(0);
		}

		NavigationItem navigationItem = new NavigationItem();
		
		/*
			FLAG this is where I left off. Was going to set attributes for main NavigationItem for a subsite
			Then add the childItems.
			Then update view controller to use this method
			Then update the view to take into account that we have another level (top level) for sub site navigation items.

		 */
		
		if(firstChild != null) {
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
     * Returns Navigation Items for a group
     * 
     * @param groupId
     *            The groupId
     * @param privateLayout
     *            Whether to use private or public layouts
     * @param httpServletRequest
     *            The HttpServletRequest
     * @param locale
     *            The Locale (retrieved from for example themeDisplay)
     * @param permissionChecker
     *            The Liferay PermissionChecker (retrieved from themeDisplay)
     * @param depth
     *            The depth of the navigation structure returned. If set to -1, return the full depth (i.e. all children's children and so on)
     * @return List<NavigationItem>
     */
	public static List<NavigationItem> getGroupNavigationItems(long groupId, boolean isSignedIn, Layout currentLayout, boolean privateLayout, HttpServletRequest httpServletRequest,
			Locale locale, PermissionChecker permissionChecker, int depth) {
		
		ArrayList<NavigationItem> navigationItems = new ArrayList<NavigationItem>();
		
		// TODO - implement depth parameter to control how deep the navigation is rolled up
		
		try {
			// First level layouts
			List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(groupId, privateLayout, 0);
			
			List<Layout> filteredLayouts = filterLayouts(layouts, permissionChecker, isSignedIn);
			
			for(Layout layout : filteredLayouts) {
				
				NavigationItem navigationItem = createNavigationItem(layout, currentLayout, locale, httpServletRequest, permissionChecker, isSignedIn);
				
				int depthCurrent = 1;
				
				navigationItem = addChildNavigation(
						navigationItem, layout, currentLayout, locale, httpServletRequest, permissionChecker, isSignedIn, depth, depthCurrent);
				
				navigationItems.add(navigationItem);
			}
			
		} catch (SystemException e) {
			LOG.error(e, e);
		}
		
		return navigationItems;
	}
	
	public static void setExpandoGroupId(long companyId, long groupId, String columnName) {

		try {
			// First make sure that the column exists
			setupCompanyExpandoColumn(companyId, columnName);
			
			// Next save data
			ExpandoValueLocalServiceUtil.addValue(
					companyId, Company.class.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName, companyId, groupId);
			
		} catch (SystemException e) {
			LOG.error(e, e);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			LOG.error(e, e);
		}
	}
	
	public static void setGroupIdRp(long companyId, long groupId) {
		
		setExpandoGroupId(companyId, groupId, NavigationConstants.EXPANDO_COLUMN_GROUP_ID_RP);
	}
	
	public static void setGroupIdVap(long companyId, long groupId) {
		
		setExpandoGroupId(companyId, groupId, NavigationConstants.EXPANDO_COLUMN_GROUP_ID_VAP);
	}
	
	// Recursive
	private static NavigationItem addChildNavigation (NavigationItem navigationItem, Layout layout, Layout currentLayout,
			Locale locale, HttpServletRequest httpServletRequest,
			PermissionChecker permissionChecker, boolean isSignedIn, int depthWanted, int depthCurrent) {
		
		List<NavigationItem> navigationItems = new ArrayList<NavigationItem>(); 
		
		boolean hasNavigationItemChildren = hasNavigationItemChildren(layout, permissionChecker);
		
		if(hasNavigationItemChildren && depthCurrent < depthWanted) {
			navigationItems = createNavigationItemChildren(layout, currentLayout, locale, httpServletRequest, permissionChecker, isSignedIn);
						
			depthCurrent++;
			
			for(NavigationItem childNavigationItem : navigationItems) {
				
				Layout childLayout = childNavigationItem.getLayout();
				
				boolean hasChildNavigationItemChildren = hasNavigationItemChildren(childLayout, permissionChecker);
				
				if(hasChildNavigationItemChildren) {
					childNavigationItem = addChildNavigation(
							childNavigationItem, childLayout, currentLayout, locale, httpServletRequest, permissionChecker, isSignedIn, depthWanted, depthCurrent);
				}
				
			}
			
		}
		
		navigationItem.setChildren(navigationItems);
		
		return navigationItem;
	}
	
	
	private static NavigationItem createNavigationItem(Layout layout, Layout currentLayout, Locale locale, HttpServletRequest httpServletRequest, PermissionChecker permissionChecker, boolean isSignedIn) {
		NavigationItem navigationItem = new NavigationItem();
		
		String name = HtmlUtil.escape(layout.getName(locale));
		String title = layout.getTitle(locale);
		String target = layout.getTarget();
		
		navigationItem.setLayout(layout);
		navigationItem.setName(name);
		navigationItem.setTitle(title);
		navigationItem.setTarget(target);

		String url;
		try {
			url = HtmlUtil.escape(HtmlUtil.escapeHREF(layout.getResetLayoutURL(httpServletRequest)));
			
			boolean isSelected = currentLayout.isSelected(true, layout, layout.getAncestorPlid());
			boolean isChildSelected = layout.isChildSelected(true, currentLayout);
			
			navigationItem.setIsSelected(isSelected);
			navigationItem.setIsChildSelected(isChildSelected);
			
			navigationItem.setUrl(url);
		} catch (PortalException e) {
			LOG.error(e, e);
		} catch (SystemException e) {
			LOG.error(e, e);
		}
		
		return navigationItem;
	}
	
	private static List <NavigationItem> createNavigationItemChildren (Layout layout, Layout currentLayout, Locale locale, HttpServletRequest httpServletRequest, PermissionChecker permissionChecker, boolean isSignedIn) {
		ArrayList<NavigationItem> navigationItems = new ArrayList<NavigationItem>();
		
		try {
			List<Layout> childLayouts = layout.getChildren(permissionChecker);
			
			for(Layout childLayout : childLayouts) {
				NavigationItem navigationItem = createNavigationItem(childLayout, currentLayout, locale, httpServletRequest, permissionChecker, isSignedIn);
				
				navigationItems.add(navigationItem);
			}
			
		} catch (PortalException e) {
			LOG.error(e, e);
		} catch (SystemException e) {
			LOG.error(e, e);
		}
		
		return navigationItems;
	}
	
	private static boolean hasNavigationItemChildren(Layout layout, PermissionChecker permissionChecker) {
		boolean hasChildren = false;
		
		try {
			hasChildren = layout.hasChildren();
		} catch (SystemException e) {
			LOG.error(e, e);
		}
		
		return hasChildren;
	}
	
	private static List<Layout> filterLayouts(List<Layout> layouts, PermissionChecker permissionChecker, boolean isSignedIn) {
		ArrayList<Layout> layoutsList = new ArrayList<Layout>();
		
		for(Layout layout : layouts) {
			
			try {
				boolean hasPermissions = LayoutPermissionUtil.contains(permissionChecker, layout, NavigationConstants.LAYOUT_ACTION_ID_VIEW);
				
				boolean isHidden = layout.isHidden();
				
				boolean hasSignedInPermission = layout.isPublicLayout() || (layout.isPrivateLayout() && isSignedIn); 
				
				if(hasPermissions && !isHidden && hasSignedInPermission) {
					layoutsList.add(layout);
				}
			} catch (PortalException e) {
				LOG.error(e, e);
			} catch (SystemException e) {
				LOG.error(e, e);
			}
		}
		
		return layoutsList;
	}
	
	private static void setupCompanyExpandoColumn(long companyId, String columnName) throws SystemException, PortalException {
		
		ExpandoTable table = null;
		
		try {
			table = ExpandoTableLocalServiceUtil.addDefaultTable(companyId, Company.class.getName());
			
			LOG.info("Navigationutil - setupCompanyExpandoColumn - foundTable");
		}
		catch (Exception e) {
			table = ExpandoTableLocalServiceUtil.getDefaultTable(companyId, Company.class.getName());
		}
		
		try {
			ExpandoColumnLocalServiceUtil.addColumn(table.getTableId(), columnName, ExpandoColumnConstants.LONG);
		}
		catch(Exception e) {
			// Do nothing (column already exists)
		}
	}
}
