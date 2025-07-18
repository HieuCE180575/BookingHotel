/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import daos.AccountDAO;
import daos.HotelDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Account;
import models.Customer;

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
        HotelDAO hdao = new HotelDAO();
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");
        String keyword = request.getParameter("keyword");

        List<Account> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = accountDAO.searchAccountByName(keyword);
         
        } else {
            list = accountDAO.getAllHotelManagers();
        }
        request.setAttribute("managerList", list);

        if (action == null) {
            request.getRequestDispatcher("WEB-INF/Admin/account-list.jsp").forward(request, response);
            return;
        }

        switch (action) {
            case "add":
                request.setAttribute("hotels", hdao.getAllHotels());
                request.setAttribute("isAdding", true);
                request.getRequestDispatcher("WEB-INF/Admin/add-account.jsp").forward(request, response);
                break;

            case "edit":
                if (idParam != null) {
                    int id = Integer.parseInt(idParam);
                    Account editingAccount = accountDAO.getAccountById(id); // đúng kiểu
                    if (editingAccount != null) {
                        request.setAttribute("editingAccount", editingAccount);
                        request.getRequestDispatcher("WEB-INF/Admin/edit-account.jsp").forward(request, response);
                    } else {
                        response.sendRedirect("Account"); // hoặc hiển thị lỗi nếu không tìm thấy
                    }
                } else {
                    response.sendRedirect("Account");
                }
                break;

            case "delete":
                if (idParam != null) {
                    int id = Integer.parseInt(idParam);
                    Account deleteAcc = accountDAO.getAccountById(id); // lấy đúng từ DB
                    if (deleteAcc != null) {
                        request.setAttribute("deleteAcc", deleteAcc);
                        request.getRequestDispatcher("WEB-INF/Admin/delete-account.jsp").forward(request, response); // forward đúng file JSP
                    } else {
                        response.sendRedirect("Account");
                    }
                } else {
                    response.sendRedirect("Account");
                }
                break;

            case "detail":

                if (idParam != null) {
                    int id = Integer.parseInt(idParam);
                    for (Account a : list) {
                        if (a.getAdminID() == id) {
                            request.setAttribute("detailsAccount", a);
                            break;
                        }
                    }
                }
               request.getRequestDispatcher("WEB-INF/Admin/detail-account.jsp").forward(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                boolean success = accountDAO.deleteAccount(id) > 0;

                if (success) {
                    response.sendRedirect("Account");
                } else {
                    request.setAttribute("error", "Delete failed.");
                    request.getRequestDispatcher("account-list.jsp").forward(request, response);
                }
            } else {
                response.sendRedirect("Account");
            }

        } else {
            String username = request.getParameter("username_A");
            String firstName = request.getParameter("firstName_A");
            String lastName = request.getParameter("lastName_A");
            String password = request.getParameter("password_A");
            String phone = request.getParameter("phone_A");
            String email = request.getParameter("email_A");
            int sex = Integer.parseInt(request.getParameter("sex_A"));
            java.sql.Date dob = java.sql.Date.valueOf(request.getParameter("dob"));
            int role = 1;

            Account account = new Account(0, username, firstName, lastName, password, phone, email, sex, dob, role);
           
            boolean success = false;

            if ("add".equals(action)) {
                success = accountDAO.addAccount(account);
                response.sendRedirect("Account");
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                account.setAdminID(id);
                success = accountDAO.editAccount(account);
                response.sendRedirect("Account");

            }

//            if (success) {
//                response.sendRedirect("Account");
//            } else {
//                request.setAttribute("error", action.equals("add") ? "Add failed." : "Update failed.");
//                request.setAttribute("account", account);
//                String targetPage = action.equals("add") ? "add-account.jsp" : "edit-account.jsp";
//                request.getRequestDispatcher(targetPage).forward(request, response);
//            }
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
