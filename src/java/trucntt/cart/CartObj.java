/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.cart;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import trucntt.tblcar.TblCarDTO;
import trucntt.tbldiscount.TblDiscountDTO;

/**
 *
 * @author DELL
 */
public class CartObj {

    private Map<TblCarDTO, Integer> car;
    private float totalPrice;
    private Date rentalDate;
    private Date returnDate;
    private TblDiscountDTO discount;

    public Map<TblCarDTO, Integer> getCar() {
        return car;
    }

    public void addCarToCart(TblCarDTO dto) {
        if (this.car == null) {
            car = new HashMap<>();
        }
        int quantity = 1;
        if (this.car.containsKey(dto)) {
            quantity = this.car.get(dto) + 1;
        }
        this.car.put(dto, quantity);
    }

    public void removeCarFromCart(TblCarDTO dto) {
        if (this.car == null) {
            return;
        }
        if (this.car.containsKey(dto)) {
            this.car.remove(dto);
            if (this.car.isEmpty()) {
                this.car = null;
            }
        }
    }

    public void calculatePrice(int numOfDay) {
        float total = 0;
        if (car != null) {
            for (Map.Entry<TblCarDTO, Integer> entry : car.entrySet()) {
                total += entry.getKey().getPrice() * entry.getValue() * numOfDay;
            }
        }
        float valueDiscount = 0;
        if (this.getDiscount() != null) {
            valueDiscount = this.getDiscount().getValue();
        }
        if (valueDiscount > 0) {
            if (valueDiscount <= 1) {
                total = total * (1 - valueDiscount);
            } else {
                total -= valueDiscount;
                if (total < 0) {
                    total = 0;
                }
            }
        }
        this.setTotalPrice(total);
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
     * @return the discount
     */
    public TblDiscountDTO getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(TblDiscountDTO discount) {
        this.discount = discount;
    }
}
