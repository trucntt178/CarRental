/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.utilities;

import java.util.Random;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author DELL
 */
public class SendEmailHelper {
    private static final String MY_EMAIL = "toruc1234@gmail.com";
    private static final String PASSWORD = "be1yeu6oi7";
    
    public static String getRandom() {
        Random rd = new Random();
        int verifyNumber = rd.nextInt(999999);
        return String.format("%06d", verifyNumber);
    }
    
    public static void sendEmail(String userEmail, String msg) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator(MY_EMAIL, PASSWORD));
        email.setSSLOnConnect(true);
        
        email.setFrom(MY_EMAIL);
        email.setSubject("Verify email");
        email.setMsg(msg);
        
        email.addTo(userEmail);
        email.send();       
    }
}
