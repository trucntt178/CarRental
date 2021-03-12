/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.tblorder;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.NamingException;
import trucntt.tblorderdetail.TblOrderDetailDTO;
import trucntt.utilities.DBHelper;

/**
 *
 * @author DELL
 */
public class TblOrderDAO implements Serializable {

    private List<TblOrderDTO> historyList;

    public List<TblOrderDTO> getHistoryList() {
        return historyList;
    }

    public int getNumOfOrders() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT COUNT(orderId) AS numOfOrders "
                        + "FROM tblOrder";
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();
                if (rs.next()) {
                    return rs.getInt("numOfOrders");
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return -1;
    }

    public int createOrder(TblOrderDTO dto)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        int numOfOrders = getNumOfOrders();
        if (numOfOrders >= 0) {
            int orderId = numOfOrders + 1;
            try {
                con = DBHelper.makeConnection();
                if (con != null) {
                    String sql = "INSERT INTO tblOrder(orderId, orderDate, orderUser, rentalDate, returnDate, discountId, totalPrice, status) "
                            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                    stm = con.prepareStatement(sql);
                    stm.setInt(1, orderId);
                    stm.setTimestamp(2, new Timestamp(dto.getOrderDate().getTime()));
                    stm.setString(3, dto.getOrderUser());
                    stm.setDate(4, new java.sql.Date(dto.getRentalDate().getTime()));
                    stm.setDate(5, new java.sql.Date(dto.getReturnDate().getTime()));
                    stm.setString(6, dto.getDiscountId());
                    stm.setFloat(7, dto.getTotalPrice());
                    stm.setBoolean(8, dto.isActive());

                    int row = stm.executeUpdate();
                    if (row > 0) {
                        return orderId;
                    }
                }
            } finally {
                if (stm != null) {
                    stm.close();
                }
                if (con != null) {
                    con.close();
                }
            }
        }
        return -1;
    }

    public void searchOrderByDateAndName(String searchValue, Date orderDate, String orderUser) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql;
                if (orderDate != null) {
                    sql = "SELECT DISTINCT o.orderId, o.orderDate, o.orderUser, c.name, od.quantity, "
                            + "o.rentalDate, o.returnDate, o.discountId, o.totalPrice, o.status, od.feedback, od.ratingPoint "
                            + "FROM tblOrder AS o, tblOrderDetail AS od, tblCar AS c, "
                            + "(SELECT od.orderId "
                            + "FROM tblOrderDetail AS od, tblCar AS c "
                            + "WHERE c.name LIKE ? "
                            + "AND c.carId = od.carId) AS odc "
                            + "WHERE cast(o.orderDate as date) = ? "
                            + "AND o.orderUser = ? "
                            + "AND o.orderId = odc.orderId "
                            + "AND o.orderId = od.orderId "
                            + "AND od.carId = c.carId "
                            + "ORDER BY o.orderDate";
                    stm = con.prepareStatement(sql);
                    stm.setString(1, "%" + searchValue + "%");
                    stm.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(orderDate));
                    stm.setString(3, orderUser);
                } else {
                    sql = "SELECT DISTINCT o.orderId, o.orderDate, o.orderUser, c.name, od.quantity, "
                            + "o.rentalDate, o.returnDate, o.discountId, o.totalPrice, o.status, od.feedback, od.ratingPoint "
                            + "FROM tblOrder AS o, tblOrderDetail AS od, tblCar AS c, "
                            + "(SELECT od.orderId "
                            + "FROM tblOrderDetail AS od, tblCar AS c "
                            + "WHERE c.name LIKE ? "
                            + "AND c.carId = od.carId) AS odc "
                            + "WHERE o.orderUser = ? "
                            + "AND o.orderId = odc.orderId "
                            + "AND o.orderId = od.orderId "
                            + "AND od.carId = c.carId "
                            + "ORDER BY o.orderDate";
                    stm = con.prepareStatement(sql);
                    stm.setString(1, "%" + searchValue + "%");
                    stm.setString(2, orderUser);
                }
                rs = stm.executeQuery();
                TblOrderDTO orderDto;
                TblOrderDetailDTO detailDto;
                List<TblOrderDetailDTO> orderedDetailList = new ArrayList<>();
                int currId = 0;
                while (rs.next()) {
                    int orderId = rs.getInt("orderId");
                    orderDate = new Date(rs.getTimestamp("orderDate").getTime());
                    String carName = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    Date rentalDate = new Date(rs.getDate("rentalDate").getTime());
                    Date returnDate = new Date(rs.getDate("returnDate").getTime());
                    String discountId = rs.getString("discountId");
                    float totalPrice = rs.getFloat("totalPrice");
                    boolean active = rs.getBoolean("status");
                    String feedback = rs.getString("feedback");
                    int point = rs.getInt("ratingPoint");

                    if (historyList == null) {
                        historyList = new ArrayList<>();
                    }

                    detailDto = new TblOrderDetailDTO(carName, quantity);
                    if (feedback != null) {
                        detailDto.setFeedback(feedback);
                        detailDto.setPoint(point);
                    }
                    if (currId != orderId) {
                        currId = orderId;
                        orderedDetailList = new ArrayList<>();
                        orderedDetailList.add(detailDto);
                        orderDto = new TblOrderDTO(orderId, orderDate, orderUser, orderedDetailList, rentalDate, returnDate, discountId, totalPrice, active);
                    } else {
                        orderedDetailList.add(detailDto);
                        historyList.remove(historyList.size() - 1);
                        orderDto = new TblOrderDTO(orderId, orderDate, orderUser, orderedDetailList, rentalDate, returnDate, discountId, totalPrice, active);
                    }

                    historyList.add(orderDto);
                }

            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean deleteOrder(int orderId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "UPDATE tblOrder "
                        + "SET status = 0 "
                        + "WHERE orderId = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, orderId);

                int row = stm.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }
    
    public List<TblOrderDetailDTO> getDetail(int orderId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT c.carId, c.name "
                        + "FROM tblOrder AS o, tblOrderDetail AS od, tblCar AS c "
                        + "WHERE o.orderId = ? "
                        + "AND o.orderId = od.orderId "
                        + "AND od.carId = c.carId";
                stm = con.prepareStatement(sql);
                stm.setInt(1, orderId);
                
                rs = stm.executeQuery();
                List<TblOrderDetailDTO> detailList = new ArrayList<>();
                while (rs.next()) {
                    int carId = rs.getInt("carId");
                    String name = rs.getString("name");
                    TblOrderDetailDTO dto = new TblOrderDetailDTO(carId, name);
                    detailList.add(dto);
                }
                return detailList;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;    
    }
}
