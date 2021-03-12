/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.tblcar;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.NamingException;
import trucntt.tblcategory.TblCategoryDTO;
import trucntt.utilities.DBHelper;

/**
 *
 * @author DELL
 */
public class TblCarDAO implements Serializable {

    private List<TblCarDTO> carList;

    public List<TblCarDTO> getCarList() {
        return carList;
    }

    public void searchCarWithDate(Date rentalDate, Date returnDate) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT od.carId, od.quantity "
                        + "FROM tblOrder AS o, tblOrderDetail AS od "
                        + "WHERE (o.rentalDate BETWEEN ? AND ? "
                        + "OR o.returnDate BETWEEN ? AND ? "
                        + "OR (o.rentalDate < ? AND o.returnDate > ?)) "
                        + "AND o.orderId = od.orderId "
                        + "AND o.status = 1";
                stm = con.prepareStatement(sql);
                stm.setDate(1, new java.sql.Date(rentalDate.getTime()));
                stm.setDate(2, new java.sql.Date(returnDate.getTime()));
                stm.setDate(3, new java.sql.Date(rentalDate.getTime()));
                stm.setDate(4, new java.sql.Date(returnDate.getTime()));
                stm.setDate(5, new java.sql.Date(rentalDate.getTime()));
                stm.setDate(6, new java.sql.Date(returnDate.getTime()));

                rs = stm.executeQuery();
                while (rs.next()) {
                    String carId = rs.getString("carId");
                    int quantity = rs.getInt("quantity");

                    for (TblCarDTO dto : carList) {
                        if (carId.equals(dto.getCarID())) {
                            int realQuantity = dto.getQuantity() - quantity;
                            dto.setAvailableQuantity(realQuantity);
                        }
                    }
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

    public void listCarByYear(String searchName, String searchCate, int searchQuantity, int firstRecord,
            int lastRecord) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT carId, name, color, year, categoryId, categoryName, price, quantity "
                        + "FROM ("
                        + "SELECT c.carId, c.name, c.color, c.year, ca.categoryId, ca.categoryName, c.price, c.quantity, "
                        + "ROW_NUMBER() OVER (ORDER BY c.year) AS RowNum "
                        + "FROM tblCar AS c, tblCategory AS ca "
                        + "WHERE c.name LIKE ? "
                        + "AND ca.categoryId LIKE ? "
                        + "AND c.quantity >= ? "
                        + "AND c.category = ca.categoryId) AS Car "
                        + "WHERE RowNum BETWEEN ? AND ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, "%" + searchName + "%");
                stm.setString(2, "%" + searchCate + "%");
                stm.setInt(3, searchQuantity);
                stm.setInt(4, firstRecord);
                stm.setInt(5, lastRecord);
                rs = stm.executeQuery();
                while (rs.next()) {
                    String carId = rs.getString("carId");
                    String name = rs.getString("name");
                    String color = rs.getString("color");
                    int year = rs.getInt("year");
                    String cateId = rs.getString("categoryId");
                    String cateName = rs.getString("categoryName");
                    float price = rs.getFloat("price");
                    int quantity = rs.getInt("quantity");

                    TblCategoryDTO cateDto = new TblCategoryDTO(cateId, cateName);
                    TblCarDTO dto = new TblCarDTO(carId, name, color, year, cateDto, price, quantity, quantity);

                    if (carList == null) {
                        carList = new ArrayList<>();
                    }

                    carList.add(dto);
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

    public int countNumOfCarPage(String searchName, String searchCate, int searchQuantity, int numOfRecords)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT COUNT(carId) AS numOfCars "
                        + "FROM tblCar "
                        + "WHERE name LIKE ? "
                        + "AND category LIKE ? "
                        + "AND quantity >= ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, "%" + searchName + "%");
                stm.setString(2, "%" + searchCate + "%");
                stm.setInt(3, searchQuantity);
                rs = stm.executeQuery();
                if (rs.next()) {
                    int numOfCars = rs.getInt("numOfCars");
                    int numOfPage = (int) Math.ceil((double) numOfCars / numOfRecords);
                    return numOfPage;
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

    public TblCarDTO searchCarById(String id) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT c.name, c.color, c.year, ca.categoryId, ca.categoryName, c.price, c.quantity "
                        + "FROM tblCar AS c, tblCategory AS ca "
                        + "WHERE c.carId = ? "
                        + "AND c.category = ca.categoryId";
                stm = con.prepareStatement(sql);
                stm.setString(1, id);
                rs = stm.executeQuery();
                if (rs.next()) {
                    String name = rs.getString("name");
                    String color = rs.getString("color");
                    int year = rs.getInt("year");
                    String cateId = rs.getString("categoryId");
                    String cateName = rs.getString("categoryName");
                    float price = rs.getFloat("price");
                    int quantity = rs.getInt("quantity");

                    TblCategoryDTO cateDto = new TblCategoryDTO(cateId, cateName);
                    TblCarDTO dto = new TblCarDTO(id, name, color, year, cateDto, price, quantity, quantity);
                    return dto;
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
        return null;
    }

    public int findRealQuanity(String carId, Date rentalDate, Date returnDate) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        int realQuantity = -1;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT c.quantity - o.quantity AS realQuantity "
                        + "FROM "
                        + "(SELECT od.carId, od.quantity "
                        + "FROM tblOrder AS o, tblOrderDetail AS od "
                        + "WHERE (o.rentalDate BETWEEN ? AND ? "
                        + "OR o.returnDate BETWEEN ? AND ? "
                        + "OR (o.rentalDate < ? AND o.returnDate > ?)) "
                        + "AND o.orderId = od.orderId "
                        + "AND o.status = 1) AS o, "
                        + "(SELECT carId, quantity "
                        + "FROM tblCar) AS c "
                        + "WHERE o.carId = ? "
                        + "AND o.carId = c.carId";
                stm = con.prepareStatement(sql);
                stm.setDate(1, new java.sql.Date(rentalDate.getTime()));
                stm.setDate(2, new java.sql.Date(returnDate.getTime()));
                stm.setDate(3, new java.sql.Date(rentalDate.getTime()));
                stm.setDate(4, new java.sql.Date(returnDate.getTime()));
                stm.setDate(5, new java.sql.Date(rentalDate.getTime()));
                stm.setDate(6, new java.sql.Date(returnDate.getTime()));
                stm.setString(7, carId);
                
                rs = stm.executeQuery();
                if (rs.next()) {
                    realQuantity = rs.getInt("realQuantity");
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
        return realQuantity;
    }
}
