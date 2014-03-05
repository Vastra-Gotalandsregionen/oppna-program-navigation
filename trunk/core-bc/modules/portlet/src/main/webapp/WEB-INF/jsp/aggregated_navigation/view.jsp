<%--

    Copyright 2010 Västra Götalandsregionen

      This library is free software; you can redistribute it and/or modify
      it under the terms of version 2.1 of the GNU Lesser General Public
      License as published by the Free Software Foundation.

      This library is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU Lesser General Public License for more details.

      You should have received a copy of the GNU Lesser General Public
      License along with this library; if not, write to the
      Free Software Foundation, Inc., 59 Temple Place, Suite 330,
      Boston, MA 02111-1307  USA

--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<portlet:defineObjects />

<c:choose>
	<c:when test="${(not empty navigationItemsRP) or not empty navigationItemsVAP}">
		<nav class="sort-pages_ modify-pages_" id="navigation">
			<ul class="nav-list clearfix">
			
					<%-- First level --%>
					<c:forEach var="navItem" items="${navigationItemsRP}" varStatus="status">
					
						<c:set var="selectedCssClass" scope="page" value=""  />
						<c:choose>
							<c:when test="${navItem.isSelected or navItem.isChildSelected}">
								<c:set var="selectedCssClass" scope="page" value="selected" />		
							</c:when>
						</c:choose>				
					
						<li class="${selectedCssClass}">
							<a href="${navItem.url}" target="${navItem.target}"><span>${navItem.name}</span></a>
							<c:if test="${not empty navItem.children}">
								<%-- Second level --%>
								<div class="nav-list-sub-wrap">
									<ul class="nav-list-sub nav-list-sub-1">
										<c:forEach var="navItemLevel2" items="${navItem.children}" varStatus="status">
										
											<c:set var="orderCssClass" scope="page" value="" />
											<c:choose>
												<c:when test="${status.first}">
													<c:set var="orderCssClass" scope="page" value="first" />		
												</c:when>
												<c:when test="${status.last}">
													<c:set var="orderCssClass" scope="page" value="last" />		
												</c:when>
											</c:choose>												
										
											<li class="${orderCssClass}">
												<a href="${navItemLevel2.url}" target="${navItemLevel2.target}"><span>${navItemLevel2.name}</span></a>
												<c:if test="${not empty navItemLevel2.children}">
													<%-- Third level --%>
													<ul class="nav-list-sub nav-list-sub-2 helper-hidden">
														<c:forEach var="navItemLevel3" items="${navItemLevel2.children}">
															<li>
																<a href="${navItemLevel3.url}" target="${navItemLevel3.target}"><span>${navItemLevel3.name}</span></a>
																<c:if test="${not empty navItemLevel3.children}">
																	<%-- Fourth level --%>
																	<ul class="nav-list-sub nav-list-sub-3 helper-hidden">
																		<c:forEach var="navItemLevel4" items="${navItemLevel3.children}">
																			<li>
																				<a href="${navItemLevel4.url}" target="${navItemLevel4.target}"><span>${navItemLevel4.name}</span></a>
																			</li>
																		</c:forEach>
																	</ul>
																</c:if>
															</li>
														</c:forEach>
													</ul>
												</c:if>
											</li>
										</c:forEach>
									</ul>
								</div>
							</c:if>
						</li>
					</c:forEach>
					
					<c:if test="${not empty navigationItemsVAP}">
						<c:set var="selectedCssClass" value="" scope="page" />
						
						<c:choose>
							<c:when test="${navigationItemsVAP[0].layout.groupId == scopeGroupId}">
								<c:set var="selectedCssClass" value="selected" scope="page" />		
							</c:when>
						</c:choose>
						
						<li class="${selectedCssClass}">
							<a href="${navigationItemsVAP[0].url}" target="${navigationItemsVAP[0].target}">
								 
								<span>V&aring;rdakt&ouml;rsportalen</span>
								<%--
								<span>${navigationItemsVAP[0].name}</span>
								--%>
							</a>
							
							<div class="nav-list-sub-wrap">
								<ul class="nav-list-sub nav-list-sub-1">
									<c:forEach var="navItem" items="${navigationItemsVAP}" varStatus="status">
										<c:set var="orderCssClass" scope="page" value="" />
										<c:choose>
											<c:when test="${status.first}">
												<c:set var="orderCssClass" scope="page" value="first" />		
											</c:when>
											<c:when test="${status.last}">
												<c:set var="orderCssClass" scope="page" value="last" />		
											</c:when>
										</c:choose>											
										<li class="${orderCssClass}">
											<a href="${navItem.url}" target="${navItem.target}"><span>${navItem.name}</span></a>
										</li>
									</c:forEach>
								</ul>
							</div>							
							
							<%-- 
							<ul class="child-menu">
								<c:forEach var="navItem" items="${navigationItemsVAP}">
									<li>
										<a href="${navItem.url}" target="${navItem.target}"><span>${navItem.name}</span></a>
									</li>
								</c:forEach>
							</ul>
							--%>
						</li>
					</c:if>
	
			</ul>
		</nav>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-error">
			Grupper har inte konfigurerats f&ouml;r navigationsportleten. G&ouml;r detta p&aring; portletens inst&auml;llningar.
		</div>
	</c:otherwise>
</c:choose>