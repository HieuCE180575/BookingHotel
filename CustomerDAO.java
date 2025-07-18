/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import DB.SQL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Customer;
import services.Encryption;
import static services.Encryption.encryptMD5;

/**
 *
 * @author Phong
 */
public class CustomerDAO {

    public boolean updateProfile(Customer customer) {
        String sql = "UPDATE Customer SET "
                + "Username = ?, "
                + "FirstName = ?, "
                + "LastName = ?, "
                + "Phone = ?, "
                + "Email = ?, "
                + "Sex = ?, "
                + "Dob = ? "
                + "WHERE UserID = ?";
        try {
            return SQL.executeUpdate(sql,
                    customer.getUsername(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getSex(),
                    customer.getDob(),
                    customer.getUserID()) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM Customer WHERE Email = ?";
        try ( ResultSet rs = SQL.executeQuery(sql, email)) {
            if (rs.next()) {
                return new Customer(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Password"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("Sex"),
                        rs.getDate("Dob"),
                        rs.getInt("Role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertCustomer(Customer customer) {
        String sql = "INSERT INTO Customer (Username, FirstName, LastName, Password, Phone, Email, Sex, Dob, Role) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            String hashedPass = encryptMD5(customer.getPassword());
            return SQL.executeUpdate(sql,
                    customer.getUsername(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    hashedPass,
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getSex(),
                    customer.getDob(),
                    customer.getRole()) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String query = "SELECT * FROM Customer";
        try ( ResultSet rs = SQL.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Password"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("Sex"),
                        rs.getDate("Dob"),
                        rs.getInt("Role")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Customer getCustomerById(int id) {
        String sql = "SELECT * FROM Customer WHERE UserID = ?";
        try ( ResultSet rs = SQL.executeQuery(sql, id)) {
            if (rs.next()) {
                return new Customer(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Password"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("Sex"),
                        rs.getDate("Dob"),
                        rs.getInt("Role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Customer> searchCustomersByName(String keyword) {
        List<Customer> list = new ArrayList<>();
        String query = "SELECT * FROM Customer WHERE FirstName LIKE ? OR LastName LIKE ?";
        try ( ResultSet rs = SQL.executeQuery(query, keyword + "%", keyword + "%")) {
            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Password"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("Sex"),
                        rs.getDate("Dob"),
                        rs.getInt("Role")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE Customer SET "
                + "Username = ?, "
                + "FirstName = ?, "
                + "LastName = ?, "
                + "Password = ?, "
                + "Phone = ?, "
                + "Email = ?, "
                + "Sex = ?, "
                + "Dob = ?, "
                + "Role = ? "
                + "WHERE UserID = ?";
        try {
            String hashPass = encryptMD5(customer.getPassword());
            return SQL.executeUpdate(sql,
                    customer.getUsername(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    hashPass,
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getSex(),
                    customer.getDob(),
                    customer.getRole(),
                    customer.getUserID()) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCustomerById(int id) {
        String sql = "DELETE FROM Customer WHERE UserID = ?";
        try {
            return SQL.executeUpdate(sql, id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
