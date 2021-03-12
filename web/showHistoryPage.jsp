<%-- 
    Document   : showHistoryPage
    Created on : Mar 6, 2021, 2:31:46 PM
    Author     : DELL
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>History Page</title>
    </head>
    <body>
        <c:set var="fullname" value="${sessionScope.FULLNAME}"/>
        <c:if test="${empty fullname}">
            <c:redirect url="loginPage"/>
        </c:if>
        <c:if test="${not empty fullname}">
            <h1>History Page</h1>
            <font color="red">
            Welcome, ${fullname}
            </font>
            <a href="logOut"><button>Log out</button></a>
            <form action="searchHistory">
                Search By Name <input type="text" name="txtSearchHistoryName" 
                                      value="${param.txtSearchHistoryName}" />
                Search By Date <input type="date" name="txtSearchHistoryDate" 
                                      value="${param.txtSearchHistoryDate}"/>
                <input type="submit" value="Search"/><br>
                <c:if test="${not empty requestScope.SEARCHNAMEERR}">
                    <font color="red">
                    ${requestScope.SEARCHNAMEERR}<br>
                    </font>
                </c:if>
                <c:if test="${not empty requestScope.SEARCHDATEERR}">
                    <font color="red">
                    ${requestScope.SEARCHDATEERR}<br>
                    </font>
                </c:if>
            </form>
            <br><a href="viewCart"><button>View Your Cart</button></a><br>
            <br><a href="search"><button>Add More Food To Your Cart</button></a><br><br>
            <c:set var="result" value="${requestScope.HISTORYLIST}"/>
            <c:if test="${not empty result}">
                <table border="1">
                    <thead>
                        <tr>
                            <th>No</th>
                            <th>Order Id</th>
                            <th>Order Date</th>
                            <th>Detail</th>
                            <th>Rental Date</th>
                            <th>Return Date</th>
                            <th>Discount Code</th>
                            <th>Total Price</th>
                            <th>Status</th>
                            <th>Delete</th>
                            <th>Feedback</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${result}" varStatus="counter">
                            <tr>
                        <form action="deleteOrder"> 
                            <td>
                                ${counter.count}
                                .</td>
                            <td>
                                ${order.orderId}
                                <input type="hidden" name="deleteId" value="${order.orderId}" />
                            </td>
                            <td>
                                <fmt:formatDate value='${order.orderDate}' type='date' pattern='dd-MM-yyyy'/>                                   
                            </td>
                            <td>
                                <c:forEach var="car" items="${order.orderCarList}">
                                    ${car.carName} x ${car.quantity}<br>
                                    <c:if test="${not empty car.feedback}">
                                        Feedback: ${car.feedback}<br>
                                        Point: ${car.point}<br><br>
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>                                   
                                <fmt:formatDate value='${order.rentalDate}' type='date' pattern='dd-MM-yyyy'/>
                                <input type="hidden" name="deleteRental" 
                                       value="<fmt:formatDate value='${order.rentalDate}' type='date' pattern='yyyy-MM-dd'/>" />
                            </td>
                            <td>                                 
                                <fmt:formatDate value='${order.returnDate}' type='date' pattern='dd-MM-yyyy'/> 
                            </td>
                            <td>
                                ${order.discountId}
                            </td>
                            <td>
                                ${order.totalPrice}
                            </td>
                            <td>
                                ${order.active ? 'Active' : 'Inactive'}
                            </td>
                            <td>
                                <c:if test="${order.active}">
                                    <input type="submit" value="Delete" onclick="return confirm('Do you want to remove?')"/>
                                    <input type="hidden" name="txtSearchHistoryName" value="${param.txtSearchHistoryName}" />
                                    <input type="hidden" name="txtSearchHistoryDate" value="${param.txtSearchHistoryDate}" />
                                </c:if>
                                <c:if test="${!order.active}">
                                    <button disabled="true">Delete</button>
                                </c:if>
                                <c:if test="${not empty requestScope.DELETEERR}">
                                    <font color="red">
                                    <br>${requestScope.DELETEERR}
                                    </font>                                  
                                </c:if>
                            </td>
                        </form>
                        <td>
                            <c:if test="${!order.active}">
                                <button disabled="true">Feedback</button>
                            </c:if>
                            <c:if test="${order.active}">
                                <form action="feedback">
                                    <input type="submit" value="feedback"/>
                                    <input type="hidden" name="feedbackId" value="${order.orderId}" />
                                    <input type="hidden" name="feedbackReturn" 
                                       value="<fmt:formatDate value='${order.returnDate}' type='date' pattern='yyyy-MM-dd'/>" />
                                </form>
                                <c:if test="${not empty requestScope.FEEDBACKERR}">
                                    <font color="red">
                                    <br>${requestScope.FEEDBACKERR}
                                    </font>                                  
                                </c:if>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
    <c:if test="${empty result}">
        History Not Found!!
    </c:if>
</c:if>
</body>
</html>
