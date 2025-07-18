/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import daos.CityDAO;
import daos.AddHotelRequestsDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.AddHotelRequest;
import models.City;

/**
 *
 * @author nguye
 */
@WebServlet(name = "AddHotelController", urlPatterns = {"/AddHotel"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 50 // 50 MB
)
public class AddHotelController extends HttpServlet {

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
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddHotelController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddHotelController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        CityDAO cityDAO = new CityDAO();
        request.setAttribute("citys", cityDAO.getAll());
        request.getRequestDispatcher("/WEB-INF/Hotel/Hotel-register.jsp").forward(request, response);

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
        AddHotelRequestsDAO requestDAO = new AddHotelRequestsDAO();
        //===============Hotel=================
        String hotelName = request.getParameter("hotelName");
        String hotelAddress = request.getParameter("hotelAddress");
        String hotelDescription = request.getParameter("hotelDescription");
        String hotelEmail = request.getParameter("hotelEmail");
        String hotelPhone = request.getParameter("hotelPhone");
        String cityId = request.getParameter("cityId");
        City city = new City();
        city.setCityID(Integer.parseInt(cityId));
        AddHotelRequest hotelRequest = new AddHotelRequest();
        hotelRequest.setHotelName(hotelName);
        hotelRequest.setAddress(hotelAddress);
        hotelRequest.setDescription(hotelDescription);
        hotelRequest.setEmailContact(hotelEmail);
        hotelRequest.setPhoneContact(hotelPhone);
        hotelRequest.setCity(city);
        hotelRequest.setImageUrl(null);
        hotelRequest.setStatus("PENDING");
        boolean success = requestDAO.insertRequest(hotelRequest);
        System.err.print("===================================================================");
        if (success) {
            response.sendRedirect("Homepage");
        } else {
            response.sendRedirect("Voucher");
        }
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
