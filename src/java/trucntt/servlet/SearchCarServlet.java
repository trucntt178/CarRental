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
import trucntt.cart.CartObj;
import trucntt.tblcar.TblCarDAO;
import trucntt.tblcar.TblCarDTO;
import trucntt.tblcategory.TblCategoryDAO;
import trucntt.tblcategory.TblCategoryDTO;

/**
 *
 * @author DELL
 */
@WebServlet(name = "SearchCarServlet", urlPatterns = {"/SearchCarServlet"})
public class SearchCarServlet extends HttpServlet {

    private final int NUM_OF_RECORDS = 20;

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

        int currPage = 1, firstRecord, lastRecord;
        Date rentalDate = null, returnDate = null;
        String currentPage = request.getParameter("currPage");
        String button = request.getParameter("btnAction");
        String searchName = request.getParameter("txtSearchName");
        String searchCate = request.getParameter("cboCategory");
        String searchQuantity = request.getParameter("txtQuantity");
        String rentalStr = request.getParameter("rentalDate");
        String returnStr = request.getParameter("returnDate");

        ServletContext context = request.getServletContext();
        Map<String, String> indexMap = (Map<String, String>) context.getAttribute("INDEXMAP");
        String url = indexMap.get("searchPage");
        String urlRewriting = url;
        try {
            if (searchName == null) {
                searchName = "";
            } else if (searchName.length() > (2 ^ 31 - 1)) {
                searchName = "";
                request.setAttribute("SEARCHNAMEERR", "Search name too long");
            } else {
                searchName = searchName.trim();
            }

            TblCategoryDAO cateDao = new TblCategoryDAO();
            cateDao.listCategory();
            List<TblCategoryDTO> cateList = cateDao.getCategoryList();
            request.setAttribute("CATEGORY", cateList);
            if ("All".equals(searchCate) || searchCate == null) {
                searchCate = "";
            }

            int quantity = 1;
            if (searchQuantity != null) {
                try {
                    quantity = Integer.parseInt(searchQuantity);
                } catch (NumberFormatException ex) {
                    request.setAttribute("SEARCHQUANTITYERR", "Quantity must be integer");
                    log("SearchCarServlet_NumberFormat " + ex.getMessage());
                }
                if (quantity <= 0) {
                    quantity = 1;
                    request.setAttribute("SEARCHQUANTITYERR", "Quantity must > 0");
                } else if (quantity > Integer.MAX_VALUE) {
                    quantity = 1;
                    request.setAttribute("SEARCHQUANTITYERR", "Quantity too large");
                }
            }

            if (rentalStr != null) {
                if (rentalStr.isEmpty() && !returnStr.isEmpty()) {
                    request.setAttribute("SEARCHDATEERR", "Please choose rental date");
                } else if (!rentalStr.isEmpty() && returnStr.isEmpty()) {
                    request.setAttribute("SEARCHDATEERR", "Please choose return date");
                } else if (!rentalStr.isEmpty() && !returnStr.isEmpty()) {
                    rentalDate = new SimpleDateFormat("yyyy-MM-dd").parse(rentalStr);
                    Date now = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    if (rentalDate.getTime() < now.getTime()) {
                        request.setAttribute("SEARCHDATEERR", "Rental date must >= now");
                        rentalDate = null;
                    } else {
                        returnDate = new SimpleDateFormat("yyyy-MM-dd").parse(returnStr);
                        if (returnDate.getTime() < rentalDate.getTime()) {
                            rentalDate = returnDate = null;
                            request.setAttribute("SEARCHDATEERR", "Return date must >= rental date");
                        }
                    }
                }
            } else {
                rentalStr = returnStr = "";
                HttpSession session = request.getSession();
                CartObj cart = (CartObj) session.getAttribute("CARCART");

                if (cart != null) {
                    if (cart.getRentalDate() != null) {
                        rentalStr = new SimpleDateFormat("yyyy-MM-dd").format(cart.getRentalDate());
                        returnStr = new SimpleDateFormat("yyyy-MM-dd").format(cart.getReturnDate());
                        rentalDate = new SimpleDateFormat("yyyy-MM-dd").parse(rentalStr);
                        returnDate = new SimpleDateFormat("yyyy-MM-dd").parse(returnStr);
                    }
                }
            }
            TblCarDAO carDao = new TblCarDAO();
            int numOfPage = carDao.countNumOfCarPage(searchName, searchCate, quantity, NUM_OF_RECORDS);

            if (currentPage != null) {
                currPage = Integer.parseInt(currentPage);
            }
            if ("<".equals(button) && currPage > 1) {
                currPage--;
            } else if (">".equals(button) && currPage < numOfPage) {
                currPage++;
            } else if (currPage > numOfPage) {
                currPage = 1;
            }

            firstRecord = NUM_OF_RECORDS * (currPage - 1) + 1;
            lastRecord = NUM_OF_RECORDS * currPage;

            carDao.listCarByYear(searchName, searchCate, quantity, firstRecord, lastRecord);
            if (rentalDate != null) {
                carDao.searchCarWithDate(rentalDate, returnDate);
            }
            List<TblCarDTO> carList = carDao.getCarList();
            request.setAttribute("SEARCHCARRESULT", carList);

            urlRewriting = url + "?"
                    + "&txtSearchName=" + searchName
                    + "&txtQuantity=" + quantity
                    + "&currPage=" + currPage
                    + "&numOfPage=" + numOfPage
                    + "&rentalDate=" + rentalStr
                    + "&returnDate=" + returnStr;
        } catch (NamingException ex) {
            log("SearchCarServlet_Naming " + ex.getMessage());
        } catch (SQLException ex) {
            log("SearchCarServlet_SQL " + ex.getMessage());
        } catch (ParseException ex) {
            log("SearchCarServlet_Parse " + ex.getMessage());
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(urlRewriting);
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
