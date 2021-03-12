/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.tbldiscount;

import java.io.Serializable;

/**
 *
 * @author DELL
 */
public class TblDiscountDTO implements Serializable{
    private String discountId;
    private float value;

    public TblDiscountDTO() {
    }

    public TblDiscountDTO(String discountId, float value) {
        this.discountId = discountId;
        this.value = value;
    }

    /**
     * @return the discountId
     */
    public String getDiscountId() {
        return discountId;
    }

    /**
     * @param discountId the discountId to set
     */
    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    /**
     * @return the value
     */
    public float getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(float value) {
        this.value = value;
    }
    
}
