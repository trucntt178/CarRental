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
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import trucntt.tblorder.TblOrderDAO;

/**
 *
 * @author DELL
 */
@WebServlet(name = "DeleteOrderServlet", urlPatterns = {"/DeleteOrderServlet"})
public class DeleteOrderServlet extends HttpServlet {

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

        String deleteId = request.getParameter("deleteId");
        String rentalStr = request.getParameter("deleteRental");
        String searchNameValue = request.getParameter("txtSearchHistoryName");
        String searchDateValue = request.getParameter("txtSearchHistoryDate");
        boolean foundErr = false;
        String url = "searchHistory?"
                + "&txtSearchHistoryName=" + searchNameValue
                + "&txtSearchHistoryDate=" + searchDateValue;
        try {
            Date rentalDate = new SimpleDateFormat("yyyy-MM-dd").parse(rentalStr);
            Date now = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            if (rentalDate.getTime() > now.getTime()) {
                int orderId = Integer.parseInt(deleteId);
                TblOrderDAO dao = new TblOrderDAO();
                dao.deleteOrder(orderId);
            } else {
                foundErr = true;
                request.setAttribute("DELETEERR", "You cannot delete this order!");
                ServletContext context = request.getServletContext();
                Map<String, String> indexMap = (Map<String, String>) context.getAttribute("INDEXMAP");
                url = indexMap.get("searchHistory");
            }
        } catch (ParseException ex) {
            log("DeleteOrderServlet_Parse " + ex.getMessage());
        } catch (NamingException ex) {
            log("DeleteOrderServlet_Naming " + ex.getMessage());
        } catch (SQLException ex) {
            log("DeleteOrderServlet_SQL " + ex.getMessage());
        } finally {
            if (!foundErr) {
                response.sendRedirect(url);
            } else {
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
            }
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
