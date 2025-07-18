/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import DB.SQL;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.Account;
import models.Customer;
import static services.Encryption.encryptMD5;

/**
 *
 * @author Phong
 */
public class AccountDAO {

    public List<Account> getAllHotelManagers() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM Admin WHERE Role_A = 2";
        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HotelDAO hdao = new HotelDAO();
                Account a = new Account(
                        rs.getInt("AdminID"),
                        rs.getString("Username_A"),
                        rs.getString("FirstName_A"),
                        rs.getString("LastName_A"),
                        rs.getString("Password_A"),
                        rs.getString("Phone_A"),
                        rs.getString("Email_A"),
                        rs.getInt("Sex_A"),
                        rs.getDate("Dob_A"),
                        rs.getInt("Role_A"),
                        hdao.getHotelById(rs.getInt("HotelID"))
                );
                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Account getAccountById(int id) {
        String sql = "SELECT * FROM Admin WHERE AdminID = ?";
        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                            rs.getInt("AdminID"),
                            rs.getString("Username_A"),
                            rs.getString("FirstName_A"),
                            rs.getString("LastName_A"),
                            rs.getString("Password_A"),
                            rs.getString("Phone_A"),
                            rs.getString("Email_A"),
                            rs.getInt("Sex_A"),
                            rs.getDate("Dob_A"),
                            rs.getInt("Role_A")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addAccount(Account account) {
        String sql = "INSERT INTO Admin (Username_A, FirstName_A, LastName_A, Password_A, Phone_A, Email_A, Sex_A, Dob_A, Role_A) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, account.getUsername_A());
            ps.setString(2, account.getFirstName_A());
            ps.setString(3, account.getLastName_A());
            // Mã hóa mật khẩu trước khi lưu
            ps.setString(4, encryptMD5(account.getPassword_A()));
            ps.setString(5, account.getPhone_A());
            ps.setString(6, account.getEmail_A());
            ps.setInt(7, account.getSex_A());
            ps.setDate(8, new java.sql.Date(account.getDob_A().getTime()));
            ps.setInt(9, 2);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkAccountById(int adminID) {
        String sql = "SELECT * FROM Admin WHERE AdminID = ?";
        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, adminID);
            try ( ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Account> searchAccountByName(String keyword) {
        List<Account> list = new ArrayList<>();
        String query = "SELECT * FROM Admin WHERE (FirstName_A LIKE ? OR LastName_A LIKE ?) AND Role_A = 2;";
        try ( ResultSet rs = SQL.executeQuery(query, keyword + "%", keyword + "%")) {
            while (rs.next()) {
                list.add(new Account(
                        rs.getInt("AdminID"),
                        rs.getString("Username_A"),
                        rs.getString("FirstName_A"),
                        rs.getString("LastName_A"),
                        rs.getString("Password_A"),
                        rs.getString("Phone_A"),
                        rs.getString("Email_A"),
                        rs.getInt("Sex_A"),
                        rs.getDate("Dob_A"),
                        rs.getInt("Role_A")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean editAccount(Account account) {

        String sql = "UPDATE Admin SET "
                + "Username_A = ?, "
                + "FirstName_A = ?, "
                + "LastName_A = ?, "
                + "Password_A = ?, "
                + "Phone_A = ?, "
                + "Email_A = ?, "
                + "Sex_A = ?, "
                + "Dob_A = ?, "
                + "Role_A = ? "
                + "WHERE AdminID = ?";
        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, account.getUsername_A());
            ps.setString(2, account.getFirstName_A());
            ps.setString(3, account.getLastName_A());
            ps.setString(4, account.getPassword_A());
            ps.setString(5, account.getPhone_A());
            ps.setString(6, account.getEmail_A());
            ps.setInt(7, account.getSex_A());
            ps.setDate(8, account.getDob_A());
            ps.setInt(9, 2);
            ps.setInt(10, account.getAdminID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
      
        }
        return false;
    }

    public int deleteAccount(int adminID) {
        String sql = "DELETE FROM Admin WHERE AdminID = ?";
        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, adminID);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) throws ParseException {
        AccountDAO dao = new AccountDAO();
        // Giả sử bạn đã có đầy đủ dữ liệu

        java.sql.Date dob = java.sql.Date.valueOf("2024-12-25");

        // Tạo đối tượng Account với dữ liệu mẫu
        Account account;
        account = new Account(
                4, // adminID
                "admin011121", // username
                "Tuan", // firstName
                "Nguyen", // lastName
                "123456", // password
                "0909123456", // phone
                "admin01@example.com", // email
                1,//sex
                dob,
                0 // role (0 = admin, 1 = user, tùy bạn định nghĩa)
        );

        // In ra console
        System.out.println(account);
        dao.editAccount(account);

    }

}
