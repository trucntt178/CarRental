/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trucntt.tblorder.TblOrderDAO;
import trucntt.tblorder.TblOrderDTO;

/**
 *
 * @author DELL
 */
@WebServlet(name = "ShowHistoryServlet", urlPatterns = {"/ShowHistoryServlet"})
public class ShowHistoryServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String searchNameValue = request.getParameter("txtSearchHistoryName");
        String searchDateValue = request.getParameter("txtSearchHistoryDate");
        
        ServletContext context = request.getServletContext();
        Map<String, String> indexMap = (Map<String, String>) context.getAttribute("INDEXMAP");
        String url = indexMap.get("showHistoryPage");
        try {
            Date date = null;
            if (searchNameValue == null) {
                searchNameValue = "";
            } else if (searchNameValue.length() > (2 ^ 31 - 1)) {
                searchNameValue = "";
                request.setAttribute("SEARCHNAMEERR", "Search value too long");
            } else {
                searchNameValue = searchNameValue.trim();
            }
            try {
                if (searchDateValue != null && !searchDateValue.equals("") && !searchDateValue.equals("null")) {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(searchDateValue);
                }
            } catch (ParseException ex) {
                request.setAttribute("SEARCHDATEERR", "Date is not correct format");
                log("ShowHistoryServlet_Parse " + ex.getMessage());
            }
            
            HttpSession session = request.getSession();
            String orderUser = (String) session.getAttribute("EMAIL");
            TblOrderDAO orderDao = new TblOrderDAO();
            orderDao.searchOrderByDateAndName(searchNameValue, date, orderUser);
            List<TblOrderDTO> historyList = orderDao.getHistoryList();
            request.setAttribute("HISTORYLIST", historyList);
            
            url += "?&txtSearchHistoryName=" + searchNameValue;
        } catch (NamingException ex) {
            log("ShowHistoryServlet_Naming " + ex.getMessage());
        } catch (SQLException ex) {
            log("ShowHistoryServlet_SQL " + ex.getMessage());
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
