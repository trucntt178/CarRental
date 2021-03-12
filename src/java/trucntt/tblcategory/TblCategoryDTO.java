/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.tblcategory;

import java.io.Serializable;

/**
 *
 * @author DELL
 */
public class TblCategoryDTO implements Serializable{
    private String categoryId;
    private String name;

    public TblCategoryDTO() {
    }

    public TblCategoryDTO(String categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    /**
     * @return the categoryId
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
