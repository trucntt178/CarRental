/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.listener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author DELL
 */
public class MyContextListener implements ServletContextListener {
    private final String FILE_PATH = "/WEB-INF/indexPage.txt";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        ServletContext context = sce.getServletContext();
        try {            
            String appPath = context.getRealPath("");

            fileReader = new FileReader(appPath + FILE_PATH);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            String[] tokens;
            Map<String, String> indexMap = new HashMap<>();
            while ((line = bufferedReader.readLine()) != null) {
                tokens = line.split("=");
                String key, value;
                if (tokens.length == 1) {
                    key = "";
                    value = tokens[0];
                    indexMap.put(key, value);
                } else if (tokens.length == 2) {
                    key = tokens[0];
                    value = tokens[1];
                    indexMap.put(key, value);
                }
            }
            context.setAttribute("INDEXMAP", indexMap);
        } catch (FileNotFoundException ex) {
            context.log("MyContextListener_FileNotFound " + ex.getMessage());
        } catch (IOException ex) {
            context.log("MyContextListener_IO " + ex.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException ex) {
                context.log("MyContextListener_IO " + ex.getMessage());
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.removeAttribute("INDEXMAP");
    }
}
