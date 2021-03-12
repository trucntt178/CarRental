<%-- 
    Document   : verify
    Created on : Mar 2, 2021, 5:09:46 PM
    Author     : DELL
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Verify Page</title>
    </head>
    <body>
        <c:if test="${empty sessionScope.VERIFYCODE}">
            <c:redirect url="loginPage"/>
        </c:if> 
        <c:if test="${not empty sessionScope.VERIFYCODE}">
            <h1>Verify</h1>
            <form action="verify" method="POST">
                Input your verify code:<br>
                <input type="text" name="txtVerifyCode" value="" />
                <input type="submit" value="Verify" />
            </form>
            <c:set var="incorrect" value="${requestScope.VERIFYINCORRECT}"/>
            <c:if test="${not empty incorrect}">
                <font color="red">
                ${incorrect}
                </font>
            </c:if>
        </c:if>
    </body>
</html>
