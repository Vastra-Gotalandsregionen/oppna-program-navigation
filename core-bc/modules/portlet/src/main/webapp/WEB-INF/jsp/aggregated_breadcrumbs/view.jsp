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
	<c:when test="${not empty breadcrumbs}">
		<nav id="breadcrumbs" class="site-breadcrumbs helper-hidden_ clearfix">
			<div class="breadcrumbs-list-wrap clearfix">
				<div class="breadcrumbs-label"> Du är här:</div>
				<ul class="breadcrumbs">
					<c:forEach var="breadcrumb" items="${breadcrumbs}">
						<li>
							<c:choose>
								<c:when test="${!breadcrumb.isSelected}">
									<a href="${breadcrumb.url}">${breadcrumb.name}</a>
								</c:when>
								<c:otherwise>
									<span>${breadcrumb.name}</span>
								</c:otherwise>
							</c:choose>
							
						</li>
					</c:forEach>
				</ul>
			</div>
		</nav>
	</c:when>
	<c:otherwise>
		<p>There are no breadcrumbs</p>
	</c:otherwise>
</c:choose>