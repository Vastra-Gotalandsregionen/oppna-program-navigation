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
	<c:when test="${not empty navigationItems}">
		<nav class="site-navigation">
			<ul class="site-navigation-list">
				<c:forEach var="navItem" items="${navigationItems}" varStatus="status">
				    <c:if test="${not empty navItem.children or not navItem.isTypeLinkToLayout}">
                        <li class="${navItem.expandoNavigationCssClass}">
                            <div class="rp-box">
                                <div class="rp-box-inner">
                                    <div class="nav-item-top">
                                        <c:choose>
                                            <c:when test="${navItem.isTypeLinkToLayout}">
                                                <span class="nav-item-title"><span>${navItem.name}</span></span>
                                            </c:when>
                                            <c:otherwise>
                                                <a class="nav-item-title" href="${navItem.url}" target="${navItem.target}"><span>${navItem.name}</span></a>
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="nav-item-description">
                                            ${navItem.expandoNavigationDescription}
                                        </div>
                                    </div>
                                    <c:if test="${not empty navItem.children}">
                                        <ul class="site-navigation-sub-list">
                                            <c:forEach var="navItemLevel2" items="${navItem.children}" varStatus="status">
                                                <li>
                                                    <a href="${navItemLevel2.url}" target="${navItemLevel2.target}"><span>${navItemLevel2.name}</span></a>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </c:if>
                                </div>
                            </div>
                        </li>
                    </c:if>
				</c:forEach>		
			</ul>
		</nav>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-error">
			There are no navigation items
		</div>
	</c:otherwise>
</c:choose>

<%-- 
<c:choose>
	<c:when test="${(not empty navigationItemsRP) or not empty navigationItemsVAP}">
		<nav class="sort-pages_ modify-pages_" id="navigation">
			<ul class="nav-list clearfix">
			
					
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
													

													<ul class="nav-list-sub nav-list-sub-2 aui-helper-hidden">
														<c:forEach var="navItemLevel3" items="${navItemLevel2.children}">
															<li>
																<a href="${navItemLevel3.url}" target="${navItemLevel3.target}"><span>${navItemLevel3.name}</span></a>
																<c:if test="${not empty navItemLevel3.children}">
																	

																	<ul class="nav-list-sub nav-list-sub-3 aui-helper-hidden">
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
--%>