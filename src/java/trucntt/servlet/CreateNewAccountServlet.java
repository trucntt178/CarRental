/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trucntt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
import javax.servlet.http.HttpSession;
import org.apache.commons.mail.EmailException;
import trucntt.tbluser.TblUserCreateErrs;
import trucntt.tbluser.TblUserDAO;
import trucntt.tbluser.TblUserDTO;
import trucntt.utilities.SendEmailHelper;

/**
 *
 * @author DELL
 */
@WebServlet(name = "CreateNewAccountServlet", urlPatterns = {"/CreateNewAccountServlet"})
public class CreateNewAccountServlet extends HttpServlet {

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

        String email = request.getParameter("txtEmail").trim();
        String name = request.getParameter("txtName").trim();
        String phone = request.getParameter("txtPhone").trim();
        String address = request.getParameter("txtAddress").trim();
        String password = request.getParameter("txtPassword").trim();
        String confirm = request.getParameter("txtConfirm").trim();
        String status = "New";
        Date createDate = new Date();

        boolean foundErr = false;
        ServletContext context = request.getServletContext();
        Map<String, String> indexMap = (Map<String, String>) context.getAttribute("INDEXMAP");
        String url = indexMap.get("createAccountErrPage");
        try {
            TblUserCreateErrs errs = new TblUserCreateErrs();
            TblUserDAO dao = new TblUserDAO();

            if (!email.matches("\\w+@\\w+[.]\\w+([.]\\w+)?") || email.length() > 50) {
                foundErr = true;
                errs.setEmailFormatErr("Email invalidate");
            }
            if (dao.checkPrimaryKey(email)) {
                foundErr = true;
                errs.setEmailExistedErr("Email is existed");
            }
            if (name.length() < 1 || name.length() > 50) {
                foundErr = true;
                errs.setNameLengthErr("Name requires 1 to 50 chars");
            }
            if (!phone.matches("\\d{7,15}")) {
                foundErr = true;
                errs.setPhoneFormatErr("Phone invalidate");
            }
            if (address.length() < 6 || address.length() > 100) {
                foundErr = true;
                errs.setAddressLengthErr("Address requires 6 to 100 chars");
            }
            if (!confirm.equals(password)) {
                foundErr = true;
                errs.setConfirmNotMatched("Confirm must match password");
            }

            if (!foundErr) {
                TblUserDTO dto = new TblUserDTO(email, password, name, phone, address, status, createDate);
                boolean result = dao.createAccount(dto);

                if (result) {
                    String verifyNumber = SendEmailHelper.getRandom();
                    SendEmailHelper.sendEmail(email, "To active your account, use this verification code: " + verifyNumber);
                    HttpSession session = request.getSession();
                    session.setAttribute("VERIFYCODE", verifyNumber);
                    session.setAttribute("VERIFYEMAIL", email);
                    session.setAttribute("VERIFYFULLNAME", name);
                    url = "verifyPage";
                }
            } else {
                request.setAttribute("CREATEERR", errs);
            }
        } catch (NamingException ex) {
            log("CreateNewAccountServlet_Naming " + ex.getMessage());
        } catch (SQLException ex) {
            log("CreateNewAccountServlet_SQL " + ex.getMessage());
        } catch (EmailException ex) {
            log("CreateNewAccountServlet_Email " + ex.getMessage());
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
