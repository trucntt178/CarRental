<%-- 
    Document   : viewPage
    Created on : Mar 5, 2021, 1:08:48 PM
    Author     : DELL
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Cart</title>
    </head>
    <body>
        <c:set var="fullname" value="${sessionScope.FULLNAME}"/>
        <c:if test="${empty fullname}">
            <c:redirect url="loginPage"/>
        </c:if>
        <c:if test="${not empty fullname}">
            <h1>View your food cart</h1>
            <font color="red">
            Welcome, ${fullname}
            </font>
            <a href="logOut"><button>Log out</button></a>
        </c:if>
        <br><a href="searchHistory"><button>Show History</button></a>
        <c:if test="${not empty sessionScope}">
            <c:set var="cart" value="${sessionScope.CARCART}"/>
            <c:if test="${not empty cart}">
                <c:set var="car" value="${cart.car}"/>
                <c:if test="${not empty car}">
                    <table border="1">
                        <thead>
                            <tr>
                                <th>No</th>
                                <th>Car Name</th>
                                <th>Car Type</th>
                                <th>Price (per day)</th>
                                <th>Quantity</th>                              
                                <th>Click For Remove</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="carElement" items="${car}" varStatus="counter">
                                <tr>
                            <form action="updateCar"> 
                                <td>${counter.count}
                                    .</td>
                                <td>
                                    ${carElement.key.name}
                                </td>
                                <td>
                                    ${carElement.key.category.name}
                                </td>
                                <td>
                                    ${carElement.key.price}
                                </td>
                                <td>
                                    <input type="number" name="carQuantity" value="${carElement.value}" min="1"/>
                                    <input type="submit" value="Update Amount Of Food"/>
                                    <input type="hidden" name="carId" value="${carElement.key.carID}" /> 
                                    <c:if test="${not empty requestScope[carElement.key.carID]}">
                                        <font color="red">
                                        <br>${requestScope[carElement.key.carID]}
                                        </font>                                     
                                    </c:if>
                                </td>
                            </form>
                            <td>
                                <c:url var="urlRewriting" value="removeCar">
                                    <c:param name="carId" value="${carElement.key.carID}"/>
                                </c:url>
                                <a href="${urlRewriting}" onclick="return confirm('Do you want to remove?')">
                                    <button>Remove</button></a>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="4">Total Price</td>
                        <td colspan="2">
                            ${cart.totalPrice}
                        </td>
                    </tr>
                </tbody>
            </table>
            <form action="chooseDate">
                Choose Rental Date <input type="date" name="rentalDate" 
                                          value="<fmt:formatDate value='${cart.rentalDate}' type='date' pattern='yyyy-MM-dd'/>" />
                Choose Return Date <input type="date" name="returnDate" 
                                          value="<fmt:formatDate value='${cart.returnDate}' type='date' pattern='yyyy-MM-dd'/>" />
                <input type="submit" value="Save" />
            </form>
            <form action="discount">
                Enter Discount Code <input type="text" name="txtDiscount" 
                                           value="${cart.discount == null ? param.txtDiscount : cart.discount.discountId}" />
                <input type="submit" value="Use" />
                <c:if test="${not empty requestScope.DISCOUNTERR}">
                    <br><font color="red">
                    ${requestScope.DISCOUNTERR}
                    </font><br>
                </c:if>                
            </form>
            <c:if test="${cart.totalPrice == 0}">
                <button disabled="true">Check Out</button>
            </c:if>
            <c:if test="${cart.totalPrice > 0}">
                <a href="checkOut"><button>Check Out</button></a>
            </c:if>
        </c:if>
        <c:if test="${empty car}">
            Your cart is empty!!!
        </c:if>
    </c:if>
    <c:if test="${empty cart}">
        Your cart is empty!!!
    </c:if>
    <br><a href="search"><button>Add More Food To Your Cart</button></a>
</c:if>
</body>
</html>
