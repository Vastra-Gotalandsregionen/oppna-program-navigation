package se.vgregion.portal.breadcrumbs.domain;

import com.liferay.portal.model.Layout;

public class BreadcrumbsItem {

    private boolean isRootLayout;
    private boolean isSelected;
    private Layout layout;
    private String name;
    private String url;

    /**
     * Constructor.
     */
    public BreadcrumbsItem() {
    }

    public boolean getIsRootLayout() {
        return isRootLayout;
    }

    public void setIsRootLayout(boolean isRootLayout) {
        this.isRootLayout = isRootLayout;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
