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
		<nav id="breadcrumbs" class="site-breadcrumbs aui-helper-hidden_ clearfix">
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