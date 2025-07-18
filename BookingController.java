/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import daos.BookingDAO;
import daos.RoomDAO;
import daos.VoucherDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static java.lang.System.out;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Booking;
import models.Room;
import models.Voucher;

/**
 *
 * @author Phong
 */
@WebServlet(name = "BookingController", urlPatterns = {"/Booking"})
public class BookingController extends HttpServlet {

    private BookingDAO bookingDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
    }

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
            out.println("<title>Servlet BookingController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BookingController at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("action");

        if (null == action) {
            List<Booking> bookings = bookingDAO.getAllBookings();
            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("WEB-INF/Booking/booking-list.jsp").forward(request, response);
        } else {
            switch (action) {
                case "viewList":
                    List<Booking> bookings = bookingDAO.getAllBookings();
                    request.setAttribute("bookings", bookings);
                    request.getRequestDispatcher("WEB-INF/Booking/booking-list.jsp").forward(request, response);
                    break;
                case "viewDetail":
                    int id = Integer.parseInt(request.getParameter("id"));
                    Booking booking = bookingDAO.getBookingById(id);
                    request.setAttribute("booking", booking);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/Booking/booking-detail.jsp");
                    dispatcher.forward(request, response);
                    break;
                case "add":
                    String daterange = request.getParameter("daterange");
                    if (daterange != null && daterange.contains(" to ")) {
                        try {
                            String[] dates = daterange.split(" to ");
                            String checkInStr = dates[0].trim();
                            String checkOutStr = dates[1].trim();
                            // Gửi lại về JSP nếu cần
                            request.setAttribute("checkInDate", checkInStr);
                            request.setAttribute("checkOutDate", checkOutStr);
                            // Lấy và xử lý số nguyên từ chuỗi
                            String temp = request.getParameter("total");
                            temp = temp.replaceAll("\\D", "");
                            int subtotal = Integer.parseInt(temp);
                            int vat = subtotal / 100 * 10;
                            int total = vat + subtotal;

                            Locale vn = new Locale("vi", "VN");
                            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(vn);

                            String formattedSubtotal = currencyFormat.format(subtotal);
                            String formattedVat = currencyFormat.format(vat);
                            String formattedTotal = currencyFormat.format(total);
                            request.setAttribute("subtotal", formattedSubtotal);
                            request.setAttribute("vat", formattedVat);
                            request.setAttribute("finalTotal", formattedTotal);

                            RoomDAO roomDAO = new RoomDAO();
                            String[] selectedRoomIDs = request.getParameterValues("selectedRooms");
                            List<Room> selectedRoom1 = new ArrayList<>();
                            for (String roomID : selectedRoomIDs) {
                                Room room = roomDAO.getRoomByID(Integer.parseInt(roomID));
                                selectedRoom1.add(room);
                            }
                            VoucherDAO voucherDAO = new VoucherDAO();
                            List<Voucher> vouchers = voucherDAO.getAllVoucher();
                            request.setAttribute("voucherList", vouchers);
                            request.setAttribute("selectedRoomsFull", selectedRoom1);
                            request.getRequestDispatcher("/WEB-INF/Booking/Booking-add.jsp").forward(request, response);
                        } catch (SQLException | ClassNotFoundException ex) {
                            Logger.getLogger(BookingController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                    break;
                default:
                    break;
            }
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
        System.out.println(">>> doPost CALLED <<<");
        System.out.println("Action = " + request.getParameter("action"));
        System.out.println("User = " + request.getParameter("user"));
        System.out.println("RoomIDs = " + request.getParameterValues("roomIDs"));
        System.out.println("Total = " + request.getParameter("total"));
         
        String action = request.getParameter("action") + "";
        if (action.equals("add")) {
            try {
                String username = request.getParameter("user");
                String checkInStr = request.getParameter("checkin");
                String checkOutStr = request.getParameter("checkout");
                String voucherID = request.getParameter("voucherID");
                VoucherDAO vDAO = new VoucherDAO();
                String[] roomIDs = request.getParameterValues("roomIDs");
                Date checkInDate = Date.valueOf(checkInStr);       // yyyy-MM-dd format
                Date checkOutDate = Date.valueOf(checkOutStr);
                Timestamp bookingTime = new Timestamp(System.currentTimeMillis());
                Booking booking = new Booking();
                booking.setUsername(username);
                booking.setCheckinDate(checkInDate);
                booking.setCheckoutDate(checkOutDate);
                booking.setTimeBooking(bookingTime);
                booking.setVAT(10);
                booking.setStatus_B("Pending");  // or default
                if (voucherID != null && !voucherID.isEmpty()) {
                    Voucher voucher;
                    voucher = vDAO.getVoucherbyID(Integer.parseInt(voucherID));
                    booking.setVoucher(voucher);
                }
                BookingDAO bookingdao = new BookingDAO();
                int bookingID = bookingdao.insertBookingAndReturnID(booking);
                if (roomIDs != null) {
                    for (String rid : roomIDs) {
                        int roomID = Integer.parseInt(rid.trim());
                        bookingdao.insertBookingRoom(bookingID, roomID);
                    }
                }
                //for payment
                String totalStr = request.getParameter("total");
                String method = request.getParameter("paymentMethod");
                request.setAttribute("method", method);
                request.setAttribute("total", totalStr);
                request.setAttribute("bID", bookingID);                
                RequestDispatcher dispatcher = request.getRequestDispatcher("Payment");
                dispatcher.forward(request, response);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(BookingController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action != null && (action.equals("confirm") || action.equals("cancel"))) {
            int id = Integer.parseInt(request.getParameter("id"));
            String status = action.equals("confirm") ? "Confirmed" : "Cancelled";
            bookingDAO.updateBookingStatus(id, status);
            response.sendRedirect("Booking?action=viewList");
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
