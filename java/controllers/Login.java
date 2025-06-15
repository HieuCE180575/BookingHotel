///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
// */
//package controllers;
//import daos.AccountDAO;
//import services.Encryption;
//import models.Account;
//import models.Customer;
//import java.io.IOException;
//import java.io.PrintWriter;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//
///**
// *
// * @author HieuNT
// */
//public class Login extends HttpServlet {
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
//        
//        String check = request.getParameter("check");
//        if (check != null) {
//            if (check.equals("signout")) {
//                if (session.getAttribute("mainUser") == null) {
//                    session.invalidate();
//                } else {
//                    session.setAttribute("user", session.getAttribute("mainUser"));
//                }
//            }
//        }
//
//        if (user != null) {
//            response.sendRedirect("index.jsp");
//            return;
//        }
//        response.sendRedirect("login.jsp");
//        return;
//
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//     @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        UserDAO u = new UserDAO();
//        String email = request.getParameter("email");
//        String password = request.getParameter("password");
//        String campus = request.getParameter("campus");
//
//        System.out.println("EMAIL: " + email);
//        System.out.println("PASS (After MD5): " + password);
//        System.out.println("CAMPUS: " + campus);
//
//        // Mã hóa mật khẩu bằng MD5
//        String hashedPassword = hashMD5(password);
//        System.out.println("PASS (After MD5): " + hashedPassword);
//
//        try {
//            // Gọi hàm Login với mật khẩu đã mã hóa
//            User user = u.Login(email, hashedPassword, campus);
//
//            if (user != null) {
//                session.setAttribute("user", user);
//                System.out.println("Login thành công!");
//                response.sendRedirect("index.jsp");
//            } else {
//                System.out.println("Thông tin đăng nhập không đúng!");
//                String errorMessage = "Inputed information is incorrect!";
//                request.setAttribute("errorLogin", errorMessage);
//                request.getRequestDispatcher("login.jsp").forward(request, response);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Hàm mã hóa MD5
//    private String hashMD5(String input) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            md.update(input.getBytes());
//            byte[] digest = md.digest();
//            StringBuilder sb = new StringBuilder();
//            for (byte b : digest) {
//                sb.append(String.format("%02x", b));
//            }
//            return sb.toString();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Error hashing password", e);
//        }
//    }
//    
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}
