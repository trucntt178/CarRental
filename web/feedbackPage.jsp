<%-- 
    Document   : feedbackPage
    Created on : Mar 6, 2021, 10:49:32 PM
    Author     : DELL
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Feedback Page</title>
    </head>
    <body>
        <c:set var="fullname" value="${sessionScope.FULLNAME}"/>
        <c:if test="${empty fullname}">
            <c:redirect url="loginPage"/>
        </c:if>
        <c:if test="${not empty fullname}">
            <h1>Feedback Page</h1>
            <font color="red">
            Welcome, ${fullname}
            </font>
            <a href="logOut"><button>Log out</button></a>
            <c:set var="detail" value="${requestScope.DETAILLIST}"/>
            <c:forEach var="car" items="${detail}">
                ${car.carName}<br>
                <input type="ar" name="${car.id}" value="${param[car.id]}" />
            </c:forEach>
        </c:if>
    </body>
</html>
