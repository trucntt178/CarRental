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
import trucntt.cart.CartObj;
import trucntt.tblcar.TblCarDAO;
import trucntt.tblcar.TblCarDTO;
import trucntt.tblorder.TblOrderDAO;
import trucntt.tblorder.TblOrderDTO;
import trucntt.tblorderdetail.TblOrderDetailDAO;

/**
 *
 * @author DELL
 */
@WebServlet(name = "CheckOutServlet", urlPatterns = {"/CheckOutServlet"})
public class CheckOutServlet extends HttpServlet {

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

        String url = "viewCartPage";
        boolean foundErr = false;
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                CartObj cart = (CartObj) session.getAttribute("CARCART");
                if (cart != null) {
                    if (cart.getRentalDate() != null) {
                        Map<TblCarDTO, Integer> car = cart.getCar();
                        if (car != null) {
                            TblCarDAO dao = new TblCarDAO();
                            Date rentalDate = cart.getRentalDate();
                            Date returnDate = cart.getReturnDate();
                            for (Map.Entry<TblCarDTO, Integer> carEntry : car.entrySet()) {
                                String carId = carEntry.getKey().getCarID();
                                TblCarDTO dto = dao.searchCarById(carId);
                                if (dto != null) {
                                    int realQuantity = dao.findRealQuanity(carId, rentalDate, returnDate);
                                    if (realQuantity == -1) {
                                        realQuantity = dto.getQuantity();
                                    }
                                    int carQuantity = carEntry.getValue();
                                    if (realQuantity < carQuantity) {
                                        foundErr = true;
                                        request.setAttribute(carId, realQuantity + " car(s) left");
                                    }
                                } else {
                                    foundErr = true;
                                    request.setAttribute(carId, "This car is not exist");
                                }
                            }
                        }
                    }
                }
                if (!foundErr) {
                    TblOrderDAO orderDao = new TblOrderDAO();
                    Date orderDate = new Date();
                    String orderUser = (String) session.getAttribute("EMAIL");
                    TblOrderDTO orderDto = new TblOrderDTO(0, orderDate, orderUser, null, cart.getRentalDate(),
                            cart.getReturnDate(), cart.getDiscount().getDiscountId(), cart.getTotalPrice(), true);
                    int orderId = orderDao.createOrder(orderDto);
                    if (orderId > 0) {
                        TblOrderDetailDAO detailDao = new TblOrderDetailDAO();
                        Map<TblCarDTO, Integer> car = cart.getCar();
                        for (Map.Entry<TblCarDTO, Integer> orderCarEntry : car.entrySet()) {
                            String orderCarId = orderCarEntry.getKey().getCarID();
                            int quantity = orderCarEntry.getValue();
                            detailDao.createOrderDetail(orderId, orderCarId, quantity);
                        }
                        session.removeAttribute("CARCART");
                    }
                } else {
                    if (cart != null) {
                        cart.setTotalPrice(0);
                    }
                }
            }
            if (foundErr) {
                ServletContext context = request.getServletContext();
                Map<String, String> indexMap = (Map<String, String>) context.getAttribute("INDEXMAP");
                url = indexMap.get("viewCartPage");
            }
        } catch (NamingException ex) {
            log("CheckOutServlet_Naming " + ex.getMessage());
        } catch (SQLException ex) {
            log("CheckOutServlet_SQL " + ex.getMessage());
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
