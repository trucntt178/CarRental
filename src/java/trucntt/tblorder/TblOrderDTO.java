/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.tblorder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import trucntt.tblorderdetail.TblOrderDetailDTO;

/**
 *
 * @author DELL
 */
public class TblOrderDTO implements Serializable{
    private int orderId;
    private Date orderDate;
    private String orderUser;
    private List<TblOrderDetailDTO> orderCarList;
    private Date rentalDate;
    private Date returnDate;
    private String discountId;
    private float totalPrice;
    private boolean active;

    public TblOrderDTO() {
    }

    public TblOrderDTO(int orderId, Date orderDate, String orderUser, List<TblOrderDetailDTO> orderCarList, Date rentalDate, Date returnDate, String discountId, float totalPrice, boolean active) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderUser = orderUser;
        this.orderCarList = orderCarList;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.discountId = discountId;
        this.totalPrice = totalPrice;
        this.active = active;
    }

    /**
     * @return the orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the orderDate
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * @return the orderUser
     */
    public String getOrderUser() {
        return orderUser;
    }

    /**
     * @param orderUser the orderUser to set
     */
    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    /**
     * @return the orderCarList
     */
    public List<TblOrderDetailDTO> getOrderCarList() {
        return orderCarList;
    }

    /**
     * @param orderCarList the orderCarList to set
     */
    public void setOrderCarList(List<TblOrderDetailDTO> orderCarList) {
        this.orderCarList = orderCarList;
    }

    /**
     * @return the rentalDate
     */
    public Date getRentalDate() {
        return rentalDate;
    }

    /**
     * @param rentalDate the rentalDate to set
     */
    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    /**
     * @return the returnDate
     */
    public Date getReturnDate() {
        return returnDate;
    }

    /**
     * @param returnDate the returnDate to set
     */
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
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
     * @return the totalPrice
     */
    public float getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
}
