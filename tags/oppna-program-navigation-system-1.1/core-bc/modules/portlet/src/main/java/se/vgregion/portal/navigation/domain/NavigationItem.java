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

package se.vgregion.portal.navigation.domain;

import java.util.List;

import com.liferay.portal.model.Layout;

public class NavigationItem {

    private List<NavigationItem> children;
    private boolean isSelected;
    private boolean isChildSelected;
    private boolean isTypeUrl;
    private boolean isTypeLinkToLayout;
    private Layout layout;
    private String name;
    private String title;
    private String url;
    private String target;
    private String expandoNavigationDescription;
    private String expandoNavigationCssClass;

    /**
     * Constructor.
     */

    public NavigationItem() {
		super();
	}
    
    public List<NavigationItem> getChildren() {
        return children;
    }

	public void setChildren(List<NavigationItem> children) {
        this.children = children;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean getIsChildSelected() {
        return isChildSelected;
    }

    public void setIsChildSelected(boolean isChildSelected) {
        this.isChildSelected = isChildSelected;
    }
    
    public boolean getIsTypeUrl() {
        return isTypeUrl;
    }

    public void setIsTypeUrl(boolean isTypeUrl) {
        this.isTypeUrl = isTypeUrl;
    }
    
    public boolean getIsTypeLinkToLayout() {
        return isTypeLinkToLayout;
    }

    public void setIsTypeLinkToLayout(boolean isTypeLinkToLayout) {
        this.isTypeLinkToLayout = isTypeLinkToLayout;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

	public String getExpandoNavigationDescription() {
		return expandoNavigationDescription;
	}

	public void setExpandoNavigationDescription(String expandoNavigationDescription) {
		this.expandoNavigationDescription = expandoNavigationDescription;
	}

	public String getExpandoNavigationCssClass() {
		return expandoNavigationCssClass;
	}

	public void setExpandoNavigationCssClass(String expandoNavigationCssClass) {
		this.expandoNavigationCssClass = expandoNavigationCssClass;
	}

}
