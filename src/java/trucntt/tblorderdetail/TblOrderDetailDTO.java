/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.tblorderdetail;

import java.io.Serializable;

/**
 *
 * @author DELL
 */
public class TblOrderDetailDTO implements Serializable{
    private int id;
    private String carName;
    private int quantity;
    private String feedback;
    private int point;

    public TblOrderDetailDTO() {
    }

    public TblOrderDetailDTO(String carName, int quantity) {
        this.carName = carName;
        this.quantity = quantity;
    }

    public TblOrderDetailDTO(String carName, int quantity, String feedback, int point) {
        this.carName = carName;
        this.quantity = quantity;
        this.feedback = feedback;
        this.point = point;
    }

    public TblOrderDetailDTO(int id, String carName) {
        this.id = id;
        this.carName = carName;
    }
    
    
   
    /**
     * @return the carName
     */
    public String getCarName() {
        return carName;
    }

    /**
     * @param carName the carName to set
     */
    public void setCarName(String carName) {
        this.carName = carName;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the point
     */
    public int getPoint() {
        return point;
    }

    /**
     * @param point the point to set
     */
    public void setPoint(int point) {
        this.point = point;
    }
    
    /**
     * @return the feedback
     */
    public String getFeedback() {
        return feedback;
    }

    /**
     * @param feedback the feedback to set
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
}
