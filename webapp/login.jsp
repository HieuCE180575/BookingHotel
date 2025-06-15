<%-- 
    Document   : login
    Created on : Feb 1, 2025, 7:09:54 PM
    Author     : Nguyen Trung Hieu
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="stylesheet" href="./css/style.css">
        <link rel="stylesheet"
              href="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha1/css/bootstrap.min.css"
              integrity="sha384-r4NyP46KrjDleawBgD5tp8Y7UzmLA05oM1iAEQ17CSuDqnUK2+k9luXQOfXJCJ4I"
              crossorigin="anonymous">
    </head>
    
        request.setAttribute("campus", c.getAllList());
        if (session.getAttribute("user") != null) {
            response.sendRedirect("index.jsp");
            return;
        }
    %>
    <body class="loginBody">
        <div class="myLoginForm">
            <div class="card">
                <div class="card-body">
                    <div class="myForm">
                        <div class="sloganLogo">
                            <img src="./img/2021-FPTU-Eng.png" alt="" id="logo">
                            <h1 class="captionLogo">FPT University Academic Portal</h1>
                        </div>
                        <form action="login" method="post" id="loginForm">
                            <div class="form-group ">
                                <label for="campus">Campus</label>
                                <select class="form-control" name="campus" id="campus">
                                    <c:forEach items="${requestScope.campus}" var="item">
                                        <option value="${item.campusID}" ${item.campusID=="FUCT"
                                                         ?"selected:":""}>${item.campusName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="email">Email</label>
                                <input type="email" class="form-control" name="email" id="email"
                                       aria-describedby="helpId" placeholder="">
                            </div>
                            <div class="form-group">
                                <label for="password">Password</label>
                                <input type="password" class="form-control" name="password" id="password"
                                       aria-describedby="helpId" placeholder="">
                            </div>
                            <button name="btnLogin" id="btnLogin"
                                    class="btn btn-primary btn-lg btn-block">Login</button>
                        </form>
                        <p class="errorLogin">${requestScope.errorLogin}</p>
                    </div>
                </div>
            </div>
        </div>
        <script src="./js/bootstrap.js"></script>
        <script src="./js/jquery-3.7.1.js"></script>
        <script src="./js/jquery.validate.js"></script>
        <script src="./js/additional-methods.js"></script>
        <script>
            $("#loginForm").validate({
                rules: {
                    email: {
                        required: true,
                        email: true
                    },
                    password: {
                        required: true,
                        minlength: 3
                    }
                },
                messages: {
                    email: {
                        required: "Please enter email",
                    },

                    password: {
                        required: "Please enter password",
                    }
                }
            });

        </script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha1/js/bootstrap.min.js"
                integrity="sha384-oesi62hOLfzrys4LxRF63OJCXdXDipiYWBnvTl9Y9/TRlw5xlKIEHpNyvvDShgf/"
        crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
                integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
        crossorigin="anonymous"></script>
    </body>

</html>