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

package se.vgregion.portal.navigation.domain.jpa;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

import javax.persistence.*;

/**
 * JPA entity class representing a site/community used for navigation.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Entity
@Table(name = "vgr_navigation_site"
        //,uniqueConstraints = @UniqueConstraint(columnNames = {"folder_id", "documentid"})
)
public class NavigationSite extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "group_id")
    private long groupId;

    @Column(name = "company_id")
    private long companyId;

    @Column(name = "order_index", unique = true)
    private int orderIndex;

    @Column(name = "private_layout")
    private boolean isPrivateLayout;

    @Column(name = "user_id")
    private long userId;

    /**
     * Constructor.
     */
    public NavigationSite() {
    }

    /**
     * Constructor.
     *
     * @param userId    the userId
     * @param companyId the companyId
     * @param groupId   the groupId
     */
    public NavigationSite(long userId, long companyId, long groupId) {
        this.userId = userId;
        this.companyId = companyId;
        this.groupId = groupId;
    }

    /**
     * Constructor.
     *
     * @param userId          the userId
     * @param companyId       the companyId
     * @param groupId         the groupId
     * @param isPrivateLayout whether isPrivateLayout
     * @param isActive        whether isActive
     * @param orderIndex      the orderIndex
     */
    public NavigationSite(long userId, long companyId, long groupId, boolean isPrivateLayout, boolean isActive,
                          int orderIndex) {
        this.userId = userId;
        this.companyId = companyId;
        this.groupId = groupId;
        this.isPrivateLayout = isPrivateLayout;
        this.isActive = isActive;
        this.orderIndex = orderIndex;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public boolean getIsPrivateLayout() {
        return isPrivateLayout;
    }

    public void setPrivateLayout(boolean isPrivateLayout) {
        this.isPrivateLayout = isPrivateLayout;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public Long getId() {
        return id;
    }
}
