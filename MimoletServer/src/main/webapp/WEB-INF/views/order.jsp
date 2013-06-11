<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf8">
	<title><spring:message code="label.title" /></title>
</head>
<body>

<a href="<c:url value="/logout" />">
	<spring:message code="label.logout" />
</a>
  
<h2><spring:message code="label.title" /></h2>

<form:form method="post" action="add" commandName="order">

	<table>
		<tr>
			<td><form:label path="link">
				<spring:message code="label.link" />
			</form:label></td>
			<td><form:input path="link" /></td>
		</tr>
		<tr>
			<td><form:label path="status">
				<spring:message code="label.status" />
			</form:label></td>
			<td><form:input path="status" /></td>
		</tr>
		<tr>
			<td><form:label path="ownerId">
				<spring:message code="label.ownerId" />
			</form:label></td>
			<td><form:input path="ownerId" /></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit"
				value="<spring:message code="label.addorder"/>" /></td>
		</tr>
	</table>
</form:form>

<h3><spring:message code="label.orders" /></h3>
<c:if test="${!empty orderList}">
	<table class="data">
		<tr>
			<th><spring:message code="label.link" /></th>
			<th><spring:message code="label.status" /></th>
			<th><spring:message code="label.ownerId" /></th>
			<th>&nbsp;</th>
		</tr>
		<c:forEach items="${orderList}" var="order">
			<tr>
				<td>${order.link}</td>
				<td>${order.status}</td>
				<td>${order.ownerId}</td>
				<td><a href="delete/${order.id}"><spring:message code="label.delete" /></a></td>
			</tr>
		</c:forEach>
	</table>
</c:if>

</body>
</html>