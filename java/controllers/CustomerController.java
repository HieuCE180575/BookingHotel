/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import daos.CustomerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Customer;

/**
 *
 * @author Phong
 */
@WebServlet(name = "CustomerController", urlPatterns = {"/Customer"})
public class CustomerController extends HttpServlet {

    private CustomerDAO customerDAO;

    @Override
    public void init() {
        customerDAO = new CustomerDAO();
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
            out.println("<title>Servlet CustomerController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CustomerController at " + request.getContextPath() + "</h1>");
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
        String idParam = request.getParameter("id");
        String keyword = request.getParameter("keyword");

        if (action == null) {
            List<Customer> list;

            if (keyword != null && !keyword.trim().isEmpty()) {
                list = customerDAO.searchCustomersByName(keyword.trim());
            } else {
                list = customerDAO.getAllCustomers();
            }

            request.setAttribute("customerList", list);
            request.setAttribute("searchKeyword", keyword);
            request.getRequestDispatcher("customer-list.jsp").forward(request, response);

        }
        if (action.equals("delete") && idParam != null) {
            int id = Integer.parseInt(idParam);
            customerDAO.deleteCustomerById(id);
            response.sendRedirect("Customer"); // sau khi xóa thì load lại danh sách
        } else if (action.equals("edit") && idParam != null) {
            int id = Integer.parseInt(idParam);
            Customer customer = customerDAO.getCustomerById(id);
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("customer-list.jsp").forward(request, response);
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

        request.setCharacterEncoding("UTF-8"); // xử lý Unicode

        // Lấy dữ liệu từ form
        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        int sex = Integer.parseInt(request.getParameter("sex"));
        java.sql.Date dob = java.sql.Date.valueOf(request.getParameter("dob"));
        int role = Integer.parseInt(request.getParameter("role"));

        // Tạo đối tượng Customer
        Customer customer = new Customer(id, username, firstName, lastName, password, phone, email, sex, dob, role);

        // Gọi DAO cập nhật
        boolean updated = customerDAO.updateCustomer(customer);

        if (updated) {
            response.sendRedirect("Customer"); // cập nhật xong quay về danh sách
        } else {
            request.setAttribute("error", "Update failed.");
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("customer-list.jsp").forward(request, response);
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
