<!DOCTYPE html>
<html lang="en" class="vocabbi_document" >
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ include file="/WEB-INF/views/include.jsp" %>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Vestibulum id ligula porta felis euismod semper.</title>
	
	<%--<spring:url value="../css/bootstrap.css" var="style_css_url" htmlEscape="true"/>--%>

    <%--<link href="${style_css_url}" rel="stylesheet">--%>

	<%--<tiles:insertAttribute name="head" />--%>

</head>

<body>
    <tiles:insertAttribute name="header" />
	<tiles:insertAttribute name="body"/>

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="${pageContext.request.contextPath}/resources/js/jquery-2.2.1.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
</body>

</html>
