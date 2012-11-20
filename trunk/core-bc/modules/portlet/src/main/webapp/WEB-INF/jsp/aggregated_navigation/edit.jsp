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

<portlet:actionURL name="saveRpNavigationSettings" var="saveRpNavigationSettingsUrl" />
<portlet:renderURL var="viewUrl" portletMode="view" />
 
<aui:form action="${saveRpNavigationSettingsUrl}" name="settingsForm" method="post" cssClass="rp-navigation-settings-form">

	<liferay-ui:header title="settings" />

	<aui:fieldset label="main-site">
		<aui:select label="choose-site" name="groupIdMainSite">
			<aui:option value="0">
				- <liferay-ui:message key="choose-site" />
			</aui:option>
			<c:forEach var="group" items="${companyGroups}">
				<c:set var="optionSelected" value="false" scope="page" />
				
				<c:if test="${mainNavigationSite != null}">
					<c:if test="${group.groupId eq mainNavigationSite.groupId}">
						<c:set var="optionSelected" value="true" scope="page" />
					</c:if>
				</c:if>
				<aui:option value="${group.groupId}" selected="${optionSelected}">${group.name}</aui:option>									
			</c:forEach>
		</aui:select>
		<aui:select label="visibility" name="isPrivateMainSite">
			<aui:option value="-1">
				- <liferay-ui:message key="choose-visibility" />
			</aui:option>
			<c:set var="optionPrivateSelected" value="false" scope="page" />
			<c:if test="${mainNavigationSite != null}">
				<c:if test="${mainNavigationSite.isPrivateLayout}">
					<c:set var="optionPrivateSelected" value="true" scope="page" />
				</c:if>
			</c:if>
			<c:set var="optionPublicSelected" value="false" scope="page" />
			<c:if test="${mainNavigationSite != null}">
				<c:if test="${!mainNavigationSite.isPrivateLayout}">
					<c:set var="optionPublicSelected" value="true" scope="page" />
				</c:if>
			</c:if>
			<aui:option value="0" selected="${optionPublicSelected}">
				<liferay-ui:message key="public" />
			</aui:option>
			<aui:option value="1" selected="${optionPrivateSelected}">
				<liferay-ui:message key="private" />
			</aui:option>
		</aui:select>
	</aui:fieldset>
	
	<aui:fieldset label="sub-sites">
		<aui:select label="choose-site" name="groupIdSubSite">
			<aui:option value="0">
				- <liferay-ui:message key="choose-site" />
			</aui:option>
			<c:forEach var="group" items="${companyGroups}">
				<c:set var="optionSelected" value="false" scope="page" />
				
				<c:if test="${subNavigationSite != null}">
					<c:if test="${group.groupId eq subNavigationSite.groupId}">
						<c:set var="optionSelected" value="true" scope="page" />
					</c:if>
				</c:if>
				<aui:option value="${group.groupId}" selected="${optionSelected}">${group.name}</aui:option>									
			</c:forEach>
		</aui:select>
		<aui:select label="visibility" name="isPrivateSubSite">
			<aui:option value="-1">
				- <liferay-ui:message key="choose-visibility" />
			</aui:option>
			<c:set var="optionPrivateSelected" value="false" scope="page" />
			<c:if test="${subNavigationSite != null}">
				<c:if test="${subNavigationSite.isPrivateLayout}">
					<c:set var="optionPrivateSelected" value="true" scope="page" />
				</c:if>
			</c:if>
			<c:set var="optionPublicSelected" value="false" scope="page" />
			<c:if test="${subNavigationSite != null}">
				<c:if test="${!subNavigationSite.isPrivateLayout}">
					<c:set var="optionPublicSelected" value="true" scope="page" />
				</c:if>
			</c:if>
			<aui:option value="0" selected="${optionPublicSelected}">
				<liferay-ui:message key="public" />
			</aui:option>
			<aui:option value="1" selected="${optionPrivateSelected}">
				<liferay-ui:message key="private" />
			</aui:option>
		</aui:select>
	</aui:fieldset>
	
	<aui:button-row>
		<aui:button type="submit" name="save" />
		<aui:button type="cancel" name="cancel" onClick=" window.location = '${viewUrl}'; " />
	</aui:button-row>

</aui:form>