/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author DELL
 */
public class VerifyRecaptchaHelper {

    public static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    public static final String SECRET_KEY = "6Lf9nmsaAAAAACkSQHS16ONE7E1BmpQ_aBZ2G7Rs";

    public static boolean verify(String gRecaptchaResponse) throws MalformedURLException, IOException {
        if (gRecaptchaResponse == null || gRecaptchaResponse.length() == 0) {
            return false;
        }

        URL verifyUrl = new URL(VERIFY_URL);

        HttpsURLConnection con = (HttpsURLConnection) verifyUrl.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        
        String postParams = "secret=" + SECRET_KEY
                + "&response=" + gRecaptchaResponse;
        
        con.setDoOutput(true);
        
        OutputStream os = con.getOutputStream();
        os.write(postParams.getBytes());
        os.flush();
        os.close();
        
        int responseCode = con.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        
        InputStream is = con.getInputStream();
        
        JsonReader jsonReader = Json.createReader(is);
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();       
        
        boolean success = jsonObject.getBoolean("success");
        return success;      
    }
}
