/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.tbluser;

import java.io.Serializable;

/**
 *
 * @author DELL
 */
public class TblUserCreateErrs implements Serializable{
    private String emailFormatErr;
    private String emailExistedErr;
    private String nameLengthErr;
    private String phoneFormatErr;
    private String addressLengthErr;
    private String passwordLengthErr;
    private String confirmNotMatched;

    public TblUserCreateErrs() {
    }

    public TblUserCreateErrs(String emailFormatErr, String emailExistedErr, String nameLengthErr, String phoneFormatErr, String addressLengthErr, String passwordLengthErr, String confirmNotMatched) {
        this.emailFormatErr = emailFormatErr;
        this.emailExistedErr = emailExistedErr;
        this.nameLengthErr = nameLengthErr;
        this.phoneFormatErr = phoneFormatErr;
        this.addressLengthErr = addressLengthErr;
        this.passwordLengthErr = passwordLengthErr;
        this.confirmNotMatched = confirmNotMatched;
    }

    /**
     * @return the emailFormatErr
     */
    public String getEmailFormatErr() {
        return emailFormatErr;
    }

    /**
     * @param emailFormatErr the emailFormatErr to set
     */
    public void setEmailFormatErr(String emailFormatErr) {
        this.emailFormatErr = emailFormatErr;
    }

    /**
     * @return the emailExistedErr
     */
    public String getEmailExistedErr() {
        return emailExistedErr;
    }

    /**
     * @param emailExistedErr the emailExistedErr to set
     */
    public void setEmailExistedErr(String emailExistedErr) {
        this.emailExistedErr = emailExistedErr;
    }

    /**
     * @return the nameLengthErr
     */
    public String getNameLengthErr() {
        return nameLengthErr;
    }

    /**
     * @param nameLengthErr the nameLengthErr to set
     */
    public void setNameLengthErr(String nameLengthErr) {
        this.nameLengthErr = nameLengthErr;
    }

    /**
     * @return the phoneFormatErr
     */
    public String getPhoneFormatErr() {
        return phoneFormatErr;
    }

    /**
     * @param phoneFormatErr the phoneFormatErr to set
     */
    public void setPhoneFormatErr(String phoneFormatErr) {
        this.phoneFormatErr = phoneFormatErr;
    }

    /**
     * @return the addressLengthErr
     */
    public String getAddressLengthErr() {
        return addressLengthErr;
    }

    /**
     * @param addressLengthErr the addressLengthErr to set
     */
    public void setAddressLengthErr(String addressLengthErr) {
        this.addressLengthErr = addressLengthErr;
    }

    /**
     * @return the passwordLengthErr
     */
    public String getPasswordLengthErr() {
        return passwordLengthErr;
    }

    /**
     * @param passwordLengthErr the passwordLengthErr to set
     */
    public void setPasswordLengthErr(String passwordLengthErr) {
        this.passwordLengthErr = passwordLengthErr;
    }

    /**
     * @return the confirmNotMatched
     */
    public String getConfirmNotMatched() {
        return confirmNotMatched;
    }

    /**
     * @param confirmNotMatched the confirmNotMatched to set
     */
    public void setConfirmNotMatched(String confirmNotMatched) {
        this.confirmNotMatched = confirmNotMatched;
    }
    
}
