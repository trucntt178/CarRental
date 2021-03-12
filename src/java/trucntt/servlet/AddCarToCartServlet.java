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
import java.util.concurrent.TimeUnit;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trucntt.cart.CartObj;
import trucntt.tblcar.TblCarDAO;
import trucntt.tblcar.TblCarDTO;

/**
 *
 * @author DELL
 */
@WebServlet(name = "AddCarToCartServlet", urlPatterns = {"/AddCarToCartServlet"})
public class AddCarToCartServlet extends HttpServlet {

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

        String carId = request.getParameter("carId");
        String searchName = request.getParameter("txtSearchName");
        String searchCate = request.getParameter("cboCategory");
        String searchQuantity = request.getParameter("txtQuantity");
        String currentPage = request.getParameter("currPage");
        String rentalStr = request.getParameter("rentalDate");
        String returnStr = request.getParameter("returnDate");
        boolean foundErr = false;

        ServletContext context = request.getServletContext();
        Map<String, String> indexMap = (Map<String, String>) context.getAttribute("INDEXMAP");
        String url = indexMap.get("search");
        try {
            HttpSession session = request.getSession();
            CartObj cart = (CartObj) session.getAttribute("CARCART");

            if (cart == null) {
                cart = new CartObj();
            }
            Map<TblCarDTO, Integer> car = cart.getCar();
            TblCarDAO dao = new TblCarDAO();
            TblCarDTO dto = dao.searchCarById(carId);
            if (dto != null) {
                if (rentalStr.isEmpty()) {
                    int realQuantity = dto.getQuantity();
                    int carQuantity = 0;
                    if (car != null) {
                        carQuantity = car.getOrDefault(dto, 0);
                    }
                    if (realQuantity <= carQuantity) {
                        foundErr = true;
                        request.setAttribute("ADDERR", "Quantity has reached its limit");
                    } else {
                        cart.addCarToCart(dto);
                    }
                } else {
                    Date rentalDate = new SimpleDateFormat("yyyy-MM-dd").parse(rentalStr);
                    Date returnDate = new SimpleDateFormat("yyyy-MM-dd").parse(returnStr);                    
                    int realQuantity = dao.findRealQuanity(carId, rentalDate, returnDate);
                    if (realQuantity == -1) {
                        realQuantity = dto.getQuantity();
                    }
                    int carQuantity = 0;
                    if (car != null) {
                        carQuantity = car.getOrDefault(dto, 0);
                    }
                    if (realQuantity <= carQuantity) {
                        foundErr = true;
                        request.setAttribute("ADDERR", "Quantity has reached its limit");
                    } else {
                        cart.setRentalDate(rentalDate);
                        cart.setReturnDate(returnDate);
                        cart.addCarToCart(dto);
                    }
                }
            }
            if (cart.getRentalDate() != null && !foundErr) {
                long diff = cart.getReturnDate().getTime() - cart.getRentalDate().getTime();
                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                int numOfDay = (int) days + 1;
                cart.calculatePrice(numOfDay);
            } else {
                cart.setTotalPrice(0);
            }
            session.setAttribute("CARCART", cart);
            String email = (String) session.getAttribute("EMAIL");
            if (email == null) {
                foundErr = false;
                url = "loginPage";
            } else {
                if (!foundErr) {
                    url = "search?"
                            + "&carId=" + carId
                            + "&txtSearchName=" + searchName
                            + "&cboCategory=" + searchCate
                            + "&txtQuantity=" + searchQuantity
                            + "&currPage=" + currentPage
                            + "&rentalDate=" + rentalStr
                            + "&returnDate=" + returnStr;
                }
            }
        } catch (NamingException ex) {
            log("AddCarToCartServlet_Naming " + ex.getMessage());
        } catch (SQLException ex) {
            log("AddCarToCartServlet_SQL " + ex.getMessage());
        } catch (ParseException ex) {
            log("AddCarToCartServlet_Parse " + ex.getMessage());
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
