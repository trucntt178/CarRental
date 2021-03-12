/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.tblorderdetail;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.NamingException;
import trucntt.utilities.DBHelper;

/**
 *
 * @author DELL
 */
public class TblOrderDetailDAO implements Serializable{
    public boolean createOrderDetail(int orderId, String carId, int quantity) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO tblOrderDetail(orderId, carId, quantity) "
                        + "VALUES(?,?,?)";
                stm = con.prepareStatement(sql);
                stm.setInt(1, orderId);
                stm.setString(2, carId);
                stm.setInt(3, quantity);
                
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
}
