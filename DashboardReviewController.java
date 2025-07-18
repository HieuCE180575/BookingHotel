/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import daos.ReviewDAO;
import daos.ReviewDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Review;

/**
 *
 * @author LEGION
 */
@WebServlet(name = "DashboardReviewController", urlPatterns = {"/Reviews"})
public class DashboardReviewController extends HttpServlet {

    private ReviewDAO reviewDAO;

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
            out.println("<title>Servlet DashboardReviewController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DashboardReviewController at " + request.getContextPath() + "</h1>");
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
        try {
            ReviewDAO dao = new ReviewDAO();
            String hotelIdRaw = request.getParameter("hotelId");

            // Nếu có tham số hotelId, tìm kiếm reviews của khách sạn cụ thể
            if (hotelIdRaw != null && !hotelIdRaw.isEmpty()) {
                int hotelId = Integer.parseInt(hotelIdRaw);
                List<Review> reviews = dao.getReviewsByHotelId(hotelId);

                // Đưa reviews và hotelId vào request attribute
                request.setAttribute("reviews", reviews);
                request.setAttribute("hotelId", hotelId);
                request.getRequestDispatcher("/WEB-INF/Review/review-list.jsp").forward(request, response);
            } else {
                // Nếu không có tham số hotelId, hiển thị danh sách toàn bộ các reviews
                List<Review> allReviews = dao.getAllReviews();  // Lấy tất cả các review
                request.setAttribute("reviews", allReviews);  // Đưa danh sách review vào request
                request.getRequestDispatcher("/WEB-INF/Review/review-list.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
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
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            String reviewIdRaw = request.getParameter("id");  // Lấy reviewId từ request

            if (reviewIdRaw != null && !reviewIdRaw.isEmpty()) {
                try {
                    int reviewId = Integer.parseInt(reviewIdRaw);  // Chuyển đổi reviewId
                    ReviewDAO dao = new ReviewDAO();
                    dao.deleteReview(reviewId);  // Gọi phương thức deleteReview để xóa review từ DB

                    // Sau khi xóa, chuyển hướng lại về danh sách review chung
                    response.sendRedirect(request.getContextPath() + "/dashReview?success=delete");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid review ID.");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Review ID is missing.");
            }
        } else if ("reply".equals(action)) {
            // Thêm phản hồi cho review
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            String reply = request.getParameter("reply");

            // Kiểm tra xem phản hồi có hợp lệ không
            if (reply != null && !reply.isEmpty()) {
                ReviewDAO dao = new ReviewDAO();
                dao.addReplyToReview(reviewId, reply);  // Cập nhật phản hồi vào DB
            }

            // Sau khi trả lời, chuyển hướng lại về trang review chung
            response.sendRedirect(request.getContextPath() + "/dashReview?success=1");
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
