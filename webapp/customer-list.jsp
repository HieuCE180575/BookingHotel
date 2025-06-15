<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.Customer"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <title class="title">Account Customer Management</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
/*            }
            .title {
                font-size: 100px;
            }*/
            .header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                background-color: #007bff;
                padding: 5px 20px;
                color: white;
            }
            .admin-button,
            .logout-button {
                padding: 8px 16px;
                border: none;
                border-radius: 6px;
                color: white;
                font-weight: bold;
                font-size: 14px;
                cursor: pointer;
                margin-left: 10px;
            }
            .admin-button {
                background-color: gray;
            }
            .logout-button {
                background-color: black;
            }
            .nav-left a.active {
                background-color: white;
                color: #1e00ff;
                padding: 8px 16px;
                border-radius: 6px;
            }
            .nav-left a,
            .nav-right a {
                color: white;
                margin-right: 20px;
                text-decoration: none;
                font-weight: bold;
            }
            .nav-left a:last-child {
                margin-right: 0;
            }
            .nav-right {
                display: flex;
                align-items: center;
            }

            .card-container {
                display: flex;
                flex-wrap: wrap;
                gap: 70px;
                padding: 70px;
            }
            .card {
                width: 250px;
                padding: 20px;
                border: 1px solid #ddd;
                border-radius: 10px;
                text-align: center;
            }
            .btn {
                display: inline-block;
                margin: 5px 2px;
                padding: 8px 16px;
                color: #fff;
                background-color: red;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                text-decoration: none;
            }
            .btn.view {
                background-color: #007bff;
            }
            .btn.delete {
                background-color: #dc3545;
            }
            .btn.edit {
                background-color: green;
            }
        </style>
    </head>
    <body>
        <!-- Header -->
        <div class="header">
            <div class="nav-left">
                <a href="Account" class="${pageContext.request.requestURI.contains('/Account') ? 'active' : ''}">Hotel Manager Management</a>
                <a href="Customer" class="${pageContext.request.requestURI.contains('/Customer') ? 'active' : ''}">Account Customer Management</a>
            </div>

            <!-- Search -->
            <div style="text-align:center; margin-top: 10px; margin-bottom: 10px"> 
                <form method="get" action="Customer">
                    <input type="text" name="keyword" value="${searchKeyword != null ? searchKeyword : ''}"
                           placeholder="Search by name..."
                           style="padding: 8px; width: 300px; border-radius: 6px; border: 1px solid #ccc;" />
                    <button type="submit"
                            style="padding: 8px 20px; background-color: black; color: white; font-weight: bold;
                            border: none; border-radius: 6px; cursor: pointer;">Search
                    </button>
                </form>
            </div>

            <div class="nav-right">
                <button class="admin-button">Admin</button>
                <button class="logout-button">Logout</button>
            </div>
        </div>

        <h2 style="text-align:center; margin-top: 20px;">Account Customer Management</h2>

        <!-- Customer List -->
        <div class="card-container">
            <c:choose>
                <c:when test="${not empty customerList}">
                    <c:forEach var="c" items="${customerList}">
                        <div class="card">
                            <h5>${c.firstName} ${c.lastName}</h5>
                            <p>Email: ${c.email}</p>
                            <a href="CusAccountDetail?id=${c.userID}" class="btn view">View Details</a>
                            <a href="Customer?action=edit&id=${c.userID}" class="btn edit">Edit</a>
                            <form method="get" action="Customer" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this customer?');">
                                <input type="hidden" name="action" value="delete" />
                                <input type="hidden" name="id" value="${c.userID}" />
                                <button type="submit" class="btn delete">Delete</button>
                            </form>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p style="text-align:center; font-weight:bold; color:red; width:100%;">Not found account.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Edit Form -->
        <c:if test="${not empty customer}">
            <div style="margin: 40px auto; width: 500px; border: 1px solid #ccc; padding: 20px; border-radius: 10px;">
                <h3 style="text-align:center;">Edit Customer</h3>
                <form method="post" action="Customer">
                    <input type="hidden" name="id" value="${customer.userID}" />

                    <label>Username:</label><br/>
                    <input type="text" name="username" value="${customer.username}" style="width:100%; padding:8px;" /><br/><br/>

                    <label>First Name:</label><br/>
                    <input type="text" name="firstName" value="${customer.firstName}" style="width:100%; padding:8px;" /><br/><br/>

                    <label>Last Name:</label><br/>
                    <input type="text" name="lastName" value="${customer.lastName}" style="width:100%; padding:8px;" /><br/><br/>

                    <label>Password:</label><br/>
                    <input type="password" name="password" value="${customer.password}" style="width:100%; padding:8px;" /><br/><br/>

                    <label>Phone:</label><br/>
                    <input type="text" name="phone" value="${customer.phone}" style="width:100%; padding:8px;" /><br/><br/>

                    <label>Email:</label><br/>
                    <input type="email" name="email" value="${customer.email}" style="width:100%; padding:8px;" /><br/><br/>

                    <label>Sex:</label><br/>
                    <select name="sex" style="width:100%; padding:8px;">
                        <option value="0" ${customer.sex == 0 ? 'selected' : ''}>Male</option>
                        <option value="1" ${customer.sex == 1 ? 'selected' : ''}>Female</option>
                    </select><br/><br/>

                    <label>Date of Birth:</label><br/>
                    <input type="date" name="dob" value="${customer.dob}" style="width:100%; padding:8px;" /><br/><br/>

                    <label>Role:</label><br/>
                    <input type="number" name="role" value="${customer.role}" style="width:100%; padding:8px;" /><br/><br/>

                    <!-- Nút Back -->
                    <a href="customer-list.jsp" 
                       style="display:inline-block; width:100%; padding:10px; margin-bottom:10px; background-color:#6c757d; color:white; text-align:center; font-weight:bold; text-decoration:none; border-radius:6px;">
                        Back to Customer List
                    </a>

                    <!-- Nút Update -->
                    <button type="submit"
                            style="width:100%; padding:10px; background-color:#007bff; color:white; font-weight:bold; border:none; border-radius:6px;">
                        Update Customer
                    </button>

                </form>
            </div>
        </c:if>
    </body>
</html>
