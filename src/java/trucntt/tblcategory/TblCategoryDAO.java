/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.tblcategory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import trucntt.utilities.DBHelper;

/**
 *
 * @author DELL
 */
public class TblCategoryDAO implements Serializable{
    private List<TblCategoryDTO> categoryList;

    public List<TblCategoryDTO> getCategoryList() {
        return categoryList;
    }
    
    public void listCategory() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT categoryId, categoryName "
                           + "From tblCategory ";
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();
                while (rs.next()) {
                    String cateId = rs.getString("categoryId");
                    String cateName = rs.getString("categoryName");
                    TblCategoryDTO dto = new TblCategoryDTO(cateId, cateName);
                    
                    if (categoryList == null) {
                        categoryList = new ArrayList<>();
                    }
                    
                    categoryList.add(dto);
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
}
