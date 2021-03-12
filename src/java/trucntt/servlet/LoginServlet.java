/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.mail.EmailException;
import trucntt.tbluser.TblUserDAO;
import trucntt.tbluser.TblUserDTO;
import trucntt.utilities.SendEmailHelper;
import trucntt.utilities.VerifyRecaptchaHelper;

/**
 *
 * @author DELL
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

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

        String email = request.getParameter("txtEmail");
        String password = request.getParameter("txtPassword");

        String url = "errLogin";
        try {
            TblUserDAO dao = new TblUserDAO();
            TblUserDTO dto = dao.checkLogin(email, password);
            if (dto != null) {
                boolean isVerify = dao.checkVerify(email);
                if (isVerify) {
                    String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
                    boolean valid = VerifyRecaptchaHelper.verify(gRecaptchaResponse);
                    if (valid) {
                        url = "";
                        HttpSession session = request.getSession();
                        session.setAttribute("EMAIL", email);
                        session.setAttribute("FULLNAME", dto.getFullname());
                    } else {
                        url = "errCaptcha";
                    }
                } else {
                    String verifyNumber = SendEmailHelper.getRandom();
                    SendEmailHelper.sendEmail(email, "To active your account, use this verification code: " + verifyNumber);
                    dao.updateStatus(email);
                    HttpSession session = request.getSession();
                    session.setAttribute("VERIFYCODE", verifyNumber);
                    session.setAttribute("VERIFYEMAIL", email);
                    session.setAttribute("VERIFYFULLNAME", dto.getFullname());

                    url = "verifyPage";
                }
            }
        } catch (NamingException ex) {
            log("LoginServlet_Naming " + ex.getMessage());
        } catch (SQLException ex) {
            log("LoginServlet_SQL " + ex.getMessage());
        } catch (EmailException ex) {
            log("LoginServlet_Email " + ex.getMessage());
        } finally {
            response.sendRedirect(url);
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
