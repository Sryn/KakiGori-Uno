<%-- 
    Document   : group
    Created on : Jan 11, 2016, 9:56:07 AM
    Author     : cmlee
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>${sagroup.name}</h1>
		<hr>
		<ul>
			<c:forEach var="m" items="${sagroup.members}">
				<li>
					${m.matricId}, ${m.name}, ${m.email}
				</li>
			</c:forEach>
		</ul>
		<br>
		<a href="old_index.html">Add more members</a>
		<a href="create-group">Create Group</a>
    </body>
</html>
