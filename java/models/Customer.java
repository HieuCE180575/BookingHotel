/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Phong
 */
import java.sql.Date;

public class Customer {

    private int userID;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    private String email;
    private int sex;
    private Date dob;
    private int role;

    public Customer(int userID, String username, String firstName, String lastName,
            String password, String phone, String email, int sex, Date dob, int role) {
        this.userID = userID;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.sex = sex;
        this.dob = dob;
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public int getSex() {
        return sex;
    }

    public Date getDob() {
        return dob;
    }

    public int getRole() {
        return role;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getFullName() {
        return firstName + "     " + lastName;
    }

    @Override
    public String toString() {
        return "Customer{" + "userID=" + userID + ", username=" + username + ", firstName=" + firstName + ", lastName=" + lastName + ", password=" + password + ", phone=" + phone + ", email=" + email + ", sex=" + sex + ", dob=" + dob + ", role=" + role + '}';
    }
    
}
