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
@WebServlet(name = "ChooseDateServlet", urlPatterns = {"/ChooseDateServlet"})
public class ChooseDateServlet extends HttpServlet {

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

        String rentalStr = request.getParameter("rentalDate");
        String returnStr = request.getParameter("returnDate");
        Date rentalDate = null, returnDate = null;
        boolean foundErr = false;
        String url = "viewCartPage";
        try {
            if (rentalStr.isEmpty() || returnStr.isEmpty()) {
                foundErr = true;
                request.setAttribute("CHOOSEDATEERR", "Please choose rental date and return date");
            } else {
                rentalDate = new SimpleDateFormat("yyyy-MM-dd").parse(rentalStr);
                Date now = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                if (rentalDate.getTime() < now.getTime()) {
                    foundErr = true;
                    request.setAttribute("CHOOSEDATEERR", "Rental date must >= now");
                    rentalDate = null;
                } else {
                    returnDate = new SimpleDateFormat("yyyy-MM-dd").parse(returnStr);
                    if (returnDate.getTime() < rentalDate.getTime()) {
                        foundErr = true;
                        rentalDate = returnDate = null;
                        request.setAttribute("CHOOSEDATEERR", "Return date must >= rental date");
                    }
                }
            }
            if (rentalDate != null) {
                HttpSession session = request.getSession();
                CartObj cart = (CartObj) session.getAttribute("CARCART");
                if (cart != null) {
                    cart.setRentalDate(rentalDate);
                    cart.setReturnDate(returnDate);
                    Map<TblCarDTO, Integer> car = cart.getCar();
                    TblCarDAO dao = new TblCarDAO();
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
                    if (!foundErr) {
                        long diff = cart.getReturnDate().getTime() - cart.getRentalDate().getTime();
                        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                        int numOfDay = (int) days + 1;
                        cart.calculatePrice(numOfDay);
                    } else {
                        cart.setTotalPrice(0);
                    }
                    session.setAttribute("CARCART", cart);
                }
            }
            if (foundErr) {
                ServletContext context = request.getServletContext();
                Map<String, String> indexMap = (Map<String, String>) context.getAttribute("INDEXMAP");
                url = indexMap.get("viewCartPage");
            }
        } catch (ParseException ex) {
            log("ChooseDateServlet_Parse " + ex.getMessage());
        } catch (NamingException ex) {
            log("ChooseDateServlet_Naming " + ex.getMessage());
        } catch (SQLException ex) {
            log("ChooseDateServlet_SQL " + ex.getMessage());
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
