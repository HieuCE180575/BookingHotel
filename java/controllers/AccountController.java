/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import daos.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Account;

/**
 *
 * @author Phong
 */
@WebServlet(name = "AccountController", urlPatterns = {"/Account"})
public class AccountController extends HttpServlet {

    private AccountDAO accountDAO;

    @Override
    public void init() {
        accountDAO = new AccountDAO();
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
            out.println("<title>Servlet AccountController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AccountController at " + request.getContextPath() + "</h1>");
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

        List<Account> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = accountDAO.searchAccountByName(keyword);
            request.setAttribute("searchKeyword", keyword);
        } else {
            list = accountDAO.getAllHotelManagers();
        }
        request.setAttribute("managerList", list);

        if (action == null) {
            request.getRequestDispatcher("account-list.jsp").forward(request, response);
            return;
        }

        switch (action) {
            case "add":
                // Đánh dấu để JSP hiển thị form Add
                request.setAttribute("isAdding", true);
                request.getRequestDispatcher("account-list.jsp").forward(request, response);
                break;

            case "edit":
                if (idParam != null) {
                    int id = Integer.parseInt(idParam);
                    for (Account a : list) {
                        if (a.getAdminID() == id) {
                            request.setAttribute("editingAccount", a);
                            break;
                        }
                    }
                    request.getRequestDispatcher("account-list.jsp").forward(request, response);
                }
                break;

            case "delete":
                if (idParam != null) {
                    int id = Integer.parseInt(idParam);
                    accountDAO.deleteAccount(id);
                }
                response.sendRedirect("Account"); // refresh list
                break;

            default:
                response.sendRedirect("Account");
                break;
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

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        String username = request.getParameter("username_A");
        String firstName = request.getParameter("firstName_A");
        String lastName = request.getParameter("lastName_A");
        String password = request.getParameter("password_A");
        String phone = request.getParameter("phone_A");
        String email = request.getParameter("email_A");
        int sex = Integer.parseInt(request.getParameter("sex_A"));
        java.sql.Date dob = java.sql.Date.valueOf(request.getParameter("dob_A"));
        int role = Integer.parseInt(request.getParameter("role_A"));

        Account account = new Account(0, username, firstName, lastName, password, phone, email, sex, dob, role);

        boolean success = false;

        if ("add".equals(action)) {
            success = accountDAO.addAccount(account);
        } else if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("AdminID"));
            account.setAdminID(id);
            success = accountDAO.editAccount(account);
        }

        if (success) {
            response.sendRedirect("Account");
        } else {
            request.setAttribute("error", (action.equals("add") ? "Add failed." : "Update failed."));
            request.setAttribute("account", account);
            request.getRequestDispatcher(action.equals("add") ? "account-add.jsp" : "account-list.jsp").forward(request, response);
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
