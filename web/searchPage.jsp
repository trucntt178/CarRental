<%-- 
    Document   : searchPage
    Created on : Mar 1, 2021, 11:27:42 PM
    Author     : DELL
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Car Rental - Search Page</title>
    </head>
    <body>
        <h1>Car Rental</h1>
        <c:set var="fullname" value="${sessionScope.FULLNAME}"/>
        <c:if test="${empty fullname}">
            <a href="loginPage"><button>Login</button></a>
        </c:if>
        <c:if test="${not empty fullname}">
            <font color="red">
            Welcome, ${fullname}
            </font>
            <form action="logOut">
                <input type="hidden" name="txtSearchName" value="${param.txtSearchName}" />
                <input type="hidden" name="cboCategory" value="${param.cboCategory}" />
                <input type="hidden" name="txtQuantity" value="${param.txtQuantity}" />
                <input type="hidden" name="rentalDate" value="${param.rentalDate}" />
                <input type="hidden" name="returnDate" value="${param.returnDate}" />
                <input type="hidden" name="currPage" value="${param.currPage}" />
                <input type="submit" value="Log Out" />
            </form>
        </c:if>
        <form action="search">
            Search By Name <input type="text" name="txtSearchName" value="${param.txtSearchName}"/>
            By Category <select name="cboCategory">
                <option>All</option>
                <c:forEach var="category" items="${requestScope.CATEGORY}">
                    <option value="${category.categoryId}"
                            ${category.categoryId == param.cboCategory ? 'selected="selected"' : ''}>${category.name}
                    </option>
                </c:forEach>
            </select>
            By Quantity >= <input type="text" name="txtQuantity" value="${param.txtQuantity}" /><br><br>
            By Rental Date <input type="date" name="rentalDate" value="${param.rentalDate}" />
            By Return Date <input type="date" name="returnDate" value="${param.returnDate}" />
            <input type="submit" value="Search" /><br><br>
            <c:if test="${not empty requestScope.SEARCHNAMEERR}">
                <font color="red">
                ${requestScope.SEARCHNAMEERR}
                </font><br>
            </c:if>
            <c:if test="${not empty requestScope.SEARCHQUANTITYERR}">
                <font color="red">
                ${requestScope.SEARCHQUANTITYERR}
                </font><br>
            </c:if>
            <c:if test="${not empty requestScope.SEARCHDATEERR}">
                <font color="red">
                ${requestScope.SEARCHDATEERR}
                </font><br>
            </c:if>
        </form>
        <a href="viewCart"><button>View Your Cart</button></a>
        <a href="searchHistory"><button>Show History</button></a>
        <c:set var="result" value="${requestScope.SEARCHCARRESULT}"/>
        <c:if test="${not empty result}">
            <table border="1">
                <thead>
                    <tr>
                        <th>No</th>
                        <th>Car Name</th>
                        <th>Color</th>
                        <th>Year</th>
                        <th>Category</th>
                        <th>Price (per day)</th>
                        <th>Quantity</th>
                        <th>Available Quantity</th>
                        <th>Add Car To Cart</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="dto" items="${result}" varStatus="counter">
                    <form action="addToCart">                    
                        <tr>
                            <td>
                                ${counter.count}
                                .</td>
                            </td>
                            <td>
                                ${dto.name}
                            </td>
                            <td>
                                ${dto.color}
                            </td>
                            <td>
                                ${dto.year}
                            </td>
                            <td>
                                ${dto.category.name}
                            </td>
                            <td>
                                ${dto.price}
                            </td>
                            <td>
                                ${dto.quantity}
                            </td>
                            <td>
                                ${dto.availableQuantity}
                            </td>
                            <td> 
                                <input type="submit" value="Add" />
                                <input type="hidden" name="carId" value="${dto.carID}" />
                                <input type="hidden" name="txtSearchName" value="${param.txtSearchName}" />
                                <input type="hidden" name="cboCategory" value="${param.cboCategory}" />
                                <input type="hidden" name="txtQuantity" value="${param.txtQuantity}" />
                                <input type="hidden" name="rentalDate" value="${param.rentalDate}" />
                                <input type="hidden" name="returnDate" value="${param.returnDate}" />
                                <input type="hidden" name="currPage" value="${param.currPage}" />
                                <c:if test="${not empty requestScope.ADDERR && dto.carID == param.carId}">
                                    <br><font color="red">
                                    ${requestScope.ADDERR}
                                    </font>
                                </c:if>
                            </td>
                        </tr>
                    </form>
                </c:forEach>
            </tbody>
        </table>
        <form action="search">
            <input type="submit" value="<" name="btnAction" />
            ${param.currPage} / ${param.numOfPage}
            <input type="hidden" name="txtSearchName" value="${param.txtSearchName}" />
            <input type="hidden" name="cboCategory" value="${param.cboCategory}" />
            <input type="hidden" name="txtQuantity" value="${param.txtQuantity}" />
            <input type="hidden" name="currPage" value="${param.currPage}" />
            <input type="submit" value=">" name="btnAction" />
        </form>
    </c:if>
    <c:if test="${empty result}">No record matched!!</c:if>
</body>
</html>
