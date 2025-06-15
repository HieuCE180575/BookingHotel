<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.Account"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
    Account editingAccount = (Account) request.getAttribute("editingAccount");
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Hotel Manager Management</title>
        <style>
            /* CSS giữ nguyên, thêm form chỉnh sửa */
            body {
                font-family: Arial;
                margin: 0;
                padding: 0;
            }
            .header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                background-color: #007bff;
                padding: 15px 20px;
                color: white;
            }
            .admin-button, .logout-button {
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
            .nav-left a, .nav-right a {
                color: white;
                margin-right: 20px;
                text-decoration: none;
                font-weight: bold;
            }
            .nav-right {
                display: flex;
                align-items: center;
            }
            .search-container {
                text-align: center;
                margin: 20px 0;
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
                background-color: orange;
            }
            .edit-form {
                max-width: 500px;
                margin: auto;
                background: #f9f9f9;
                padding: 20px;
                border-radius: 10px;
                border: 1px solid #ccc;
                margin-top: 30px;
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
            <form method="get" action="Account">
                <input type="text" name="keyword" value="${searchKeyword != null ? searchKeyword : ''}" placeholder="Search by name..."
                       style="padding: 8px; width: 300px; border-radius: 6px; border: 1px solid #ccc;" />
                <button type="submit"
                        style="padding: 8px 20px; background-color: black; color: white; font-weight: bold; border: none; border-radius: 6px; cursor: pointer;">
                    Search
                </button>
            </form>
            <div class="nav-right">
                <button class="admin-button">Admin</button>
                <button class="logout-button">Logout</button>
            </div>
        </div>

        <h2 style="text-align:center; margin-top: 20px;">Hotel Manager Management</h2>
        <div style="text-align: right; padding: 0 70px 10px 0;">
            <a href="Account?action=add"
               style="padding: 10px 20px; background-color: green; color: white; text-decoration: none; border-radius: 6px;">
                Create New Manager
            </a>
        </div>



        <div class="card-container">
            <c:choose>
                <c:when test="${not empty managerList}">
                    <c:forEach var="manager" items="${managerList}">
                        <div class="card">
                            <h5>${manager.firstName_A} ${manager.lastName_A}</h5>
                            <p>Email: ${manager.email_A}</p>
                            <a href="CusAccountDetail?id=${manager.adminID}" class="btn view">View Details</a>
                            <a href="Account?action=edit&id=${manager.adminID}" class="btn edit">Edit</a>
                            <form method="get" action="Account" style="display:inline;"
                                  onsubmit="return confirm('Are you sure you want to delete this manager?');">
                                <input type="hidden" name="action" value="delete" />
                                <input type="hidden" name="id" value="${manager.adminID}" />
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
        <c:if test="${not empty isAdding}">
            <div class="edit-form">
                <h3>Add New Hotel Manager</h3>
                <form method="post" action="Account">
                    <input type="hidden" name="action" value="add" />

                    <label>Username:</label><br/>
                    <input type="text" name="username_A" required /><br/>

                    <label>First Name:</label><br/>
                    <input type="text" name="firstName_A" required /><br/>

                    <label>Last Name:</label><br/>
                    <input type="text" name="lastName_A" required /><br/>

                    <label>Password:</label><br/>
                    <input type="password" name="password_A" required /><br/>

                    <label>Phone:</label><br/>
                    <input type="text" name="phone_A" /><br/>

                    <label>Email:</label><br/>
                    <input type="email" name="email_A" /><br/>

                    <label>Gender:</label><br/>
                    <select name="sex_A">
                        <option value="1">Nam</option>
                        <option value="0">Nữ</option>
                    </select><br/>

                    <label>Date of Birth:</label><br/>
                    <input type="date" name="dob_A" /><br/>

                    <label>Role:</label><br/>
                    <select name="role_A">
                        <option value="1">Hotel Manager</option>
                        <option value="2">Other</option>
                    </select><br/>

                    <br/>
                    <button type="submit" style="padding:10px; background-color:green; color:white;">Add Account</button>
                    <a href="Account" style="padding:10px; background-color:gray; color:white; text-decoration:none;">Cancel</a>
                </form>
            </div>
        </c:if>

        <!-- FORM EDIT -->
        <c:if test="${not empty editingAccount}">
            <div class="edit-form">
                <h3>Edit Hotel Manager</h3>
                <form method="post" action="Account">
                    <input type="hidden" name="action" value="update" />
                    <input type="hidden" name="id" value="${editingAccount.adminID}" />

                    <label>Username:</label><br/>
                    <input type="text" name="username" value="${editingAccount.username_A}" required /><br/>

                    <label>First Name:</label><br/>
                    <input type="text" name="firstName" value="${editingAccount.firstName_A}" required /><br/>

                    <label>Last Name:</label><br/>
                    <input type="text" name="lastName" value="${editingAccount.lastName_A}" required /><br/>

                    <label>Password:</label><br/>
                    <input type="password" name="password" value="${editingAccount.password_A}" required /><br/>

                    <label>Phone:</label><br/>
                    <input type="text" name="phone" value="${editingAccount.phone_A}" /><br/>

                    <label>Email:</label><br/>
                    <input type="email" name="email" value="${editingAccount.email_A}" /><br/>

                    <label>Gender:</label><br/>
                    <select name="sex">
                        <option value="1" ${editingAccount.sex_A == 1 ? 'selected' : ''}>Nam</option>
                        <option value="0" ${editingAccount.sex_A == 0 ? 'selected' : ''}>Nữ</option>
                    </select><br/>

                    <label>Date of Birth:</label><br/>
                    <input type="date" name="dob" value="${editingAccount.dob_A}" /><br/>

                    <br/>
                    <button type="submit" style="padding:10px; background-color:#007bff; color:white;">Cập nhật</button>
                    <a href="Account" style="padding:10px; background-color:gray; color:white; text-decoration:none;">Hủy</a>
                </form>
            </div>
        </c:if>

    </body>
</html>
