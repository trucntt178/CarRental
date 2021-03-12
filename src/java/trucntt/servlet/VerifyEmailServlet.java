/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
import trucntt.tbluser.TblUserDAO;

/**
 *
 * @author DELL
 */
@WebServlet(name = "VerifyEmailServlet", urlPatterns = {"/VerifyEmailServlet"})
public class VerifyEmailServlet extends HttpServlet {

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

        String inputCode = request.getParameter("txtVerifyCode");
        ServletContext context = request.getServletContext();

        boolean foundErr = false;
        Map<String, String> indexMap = (Map<String, String>) context.getAttribute("INDEXMAP");
        String url = indexMap.get("verifyPage");
        try {
            HttpSession session = request.getSession();
            String verifyCode = (String) session.getAttribute("VERIFYCODE");
            String verifyEmail = (String) session.getAttribute("VERIFYEMAIL");
            String verifyName = (String) session.getAttribute("VERIFYFULLNAME");           
            if (inputCode.equals(verifyCode)) {
                TblUserDAO dao = new TblUserDAO();
                dao.updateStatus(verifyEmail);
                session.setAttribute("EMAIL", verifyEmail);
                session.setAttribute("FULLNAME", verifyName);
                session.removeAttribute("VERIFYCODE");
                session.removeAttribute("VERIFYEMAIL");
                session.removeAttribute("VERIFYFULLNAME");
                url = "";
            } else {
                foundErr = true;
                request.setAttribute("VERIFYINCORRECT", "Incorrect Verification Code");
            }
        } catch (NamingException ex) {
            log("VerifyEmailServlet_Naming " + ex.getMessage());
        } catch (SQLException ex) {
            log("VerifyEmailServlet_SQL " + ex.getMessage());
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
