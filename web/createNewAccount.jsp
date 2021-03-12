<%-- 
    Document   : createNewAccount
    Created on : Mar 2, 2021, 10:54:45 AM
    Author     : DELL
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create New Account</title>
    </head>
    <body>
        <h1>Create New Account</h1>
        <form action="createAccount" method="POST">
            <c:set var="errs" value="${requestScope.CREATEERR}"/>
            Email <input type="text" name="txtEmail" value="${param.txtEmail}"/> (Format: name@domain. Max: 50 chars)<br><br>
            <c:if test="${not empty errs.emailFormatErr}">
                <font color="red">
                ${errs.emailFormatErr}
                </font><br>
            </c:if>
            <c:if test="${not empty errs.emailExistedErr}">
                <font color="red">
                ${errs.emailExistedErr}
                </font><br>
            </c:if>
            Name <input type="text" name="txtName" value="${param.txtName}" /> (1 - 50 chars)<br><br>
            <c:if test="${not empty errs.nameLengthErr}">
                <font color="red">
                ${errs.nameLengthErr}
                </font><br>
            </c:if>
            Phone <input type="text" name="txtPhone" value="${param.txtPhone}" /> (7 - 15 digits)<br><br>
            <c:if test="${not empty errs.phoneFormatErr}">
                <font color="red">
                ${errs.phoneFormatErr}
                </font><br>
            </c:if>
            Address <input type="text" name="txtAddress" value="${param.txtAddress}" /> (6 - 100 chars)<br><br>
            <c:if test="${not empty errs.addressLengthErr}">
                <font color="red">
                ${errs.addressLengthErr}
                </font><br>
            </c:if>
            Password <input type="password" name="txtPassword" value="" /> (6 - 20 chars)<br><br>
            <c:if test="${not empty errs.passwordLengthErr}">
                <font color="red">
                ${errs.passwordLengthErr}
                </font><br>
            </c:if>
            Confirm Password <input type="password" name="txtConfirm" value="" /><br><br>
            <c:if test="${not empty errs.passwordLengthErr}">
                <font color="red">
                ${errs.confirmNotMatched}
                </font><br>
            </c:if>
            <input type="submit" value="Create" name="btnAction" />
            <input type="reset" value="Reset" /><br><br>
        </form>
        <a href="loginPage"><button>Return To Login Page</button></a>
    </body>
</html>
