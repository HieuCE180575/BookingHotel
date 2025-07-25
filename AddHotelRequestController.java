/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import daos.AddHotelRequestsDAO;
import daos.CityDAO;
import daos.HotelDAO;
import daos.ReviewDAO;
import daos.RoomDAO;
import daos.RoomtypeDAO;
import daos.ServiceDAO;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.AddHotelRequest;

/**
 *
 * @author nguye
 */
@WebServlet(name = "AddHotelRequestController", urlPatterns = {"/AddHotelRequest"})
public class AddHotelRequestController extends HttpServlet {

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
            out.println("<title>Servlet AddHotelRequestController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddHotelRequestController at " + request.getContextPath() + "</h1>");
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
        AddHotelRequestsDAO reqDAO = new AddHotelRequestsDAO();
        request.setAttribute("reqList", reqDAO.getAllRequest());
         request.getRequestDispatcher("/WEB-INF/Request/RequestList.jsp").forward(request, response);
        
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
        CityDAO cDAO = new CityDAO();
        AddHotelRequestsDAO addreqDAO = new AddHotelRequestsDAO();
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("register".equals(action)) {
            // 1. Lấy thông tin từ form
            String hotelName = request.getParameter("hotelName");
            String address = request.getParameter("hotelAddress");
            String description = request.getParameter("hotelDescription");
            String email = request.getParameter("hotelEmail");
            String phone = request.getParameter("hotelPhone");
            int cityId = Integer.parseInt(request.getParameter("cityId"));
            // 2. Lấy file ảnh duy nhất
            Part imagePart = (Part) request.getPart("image");
            String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            
            String filePath = uploadPath + File.separator + fileName;
            imagePart.write(filePath);
            // Giả sử bạn lưu ảnh bằng đường dẫn tương đối
            String imageUrl = "uploads/" + fileName;
            
            AddHotelRequest req = new AddHotelRequest();
            req.setHotelName(hotelName);
            req.setAddress(address);
            req.setDescription(description);
            req.setPhoneContact(phone);
            req.setEmailContact(email);
            req.setStatus("Pending");
            req.setImageUrl(imageUrl);
            req.setCity(cDAO.getCitybyID(cityId));
            boolean success = addreqDAO.insertRequest(req);
            System.out.println("Insert success: " + success);
            
            response.sendRedirect("Homapage");
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
