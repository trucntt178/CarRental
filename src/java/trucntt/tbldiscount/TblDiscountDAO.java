/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.tbldiscount;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.naming.NamingException;
import trucntt.utilities.DBHelper;

/**
 *
 * @author DELL
 */
public class TblDiscountDAO implements Serializable{

    public float getDiscountValue(String discountId, Date now) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT value "
                        + "FROM tblDiscount "
                        + "WHERE discountId = ? "
                        + "AND ? BETWEEN startDate AND endDate";
                stm = con.prepareStatement(sql);
                stm.setString(1, discountId);
                stm.setDate(2, new java.sql.Date(now.getTime()));
                
                rs = stm.executeQuery();
                if (rs.next()) {
                    float value = rs.getFloat("value");
                    return value;
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
        return 0;
    }
}
