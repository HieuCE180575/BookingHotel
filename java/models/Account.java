/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Date;

/**
 *
 * @author Phong
 */
public class Account {

    private int adminID;
    private String username_A;
    private String firstName_A;
    private String lastName_A;
    private String password_A;
    private String phone_A;
    private String email_A;
    private int sex_A;
    private Date dob_A;
    private int role_A;

    public Account(int adminID, String username_A, String firstName_A, String lastName_A,
            String password_A, String phone_A, String email_A, int sex_A, Date dob_A, int role_A) {
        this.adminID = adminID;
        this.username_A = username_A;
        this.firstName_A = firstName_A;
        this.lastName_A = lastName_A;
        this.password_A = password_A;
        this.phone_A = phone_A;
        this.email_A = email_A;
        this.sex_A = sex_A;
        this.dob_A = dob_A;
        this.role_A = role_A;
    }

    public int getAdminID() {
        return adminID;
    }

    public String getUsername_A() {
        return username_A;
    }

    public String getFirstName_A() {
        return firstName_A;
    }

    public String getLastName_A() {
        return lastName_A;
    }

    public String getPassword_A() {
        return password_A;
    }

    public String getPhone_A() {
        return phone_A;
    }

    public String getEmail_A() {
        return email_A;
    }

    public int getSex_A() {
        return sex_A;
    }

    public Date getDob_A() {
        return dob_A;
    }

    public int getRole_A() {
        return role_A;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public void setUsername_A(String username_A) {
        this.username_A = username_A;
    }

    public void setFirstName_A(String firstName_A) {
        this.firstName_A = firstName_A;
    }

    public void setLastName_A(String lastName_A) {
        this.lastName_A = lastName_A;
    }

    public void setPassword_A(String password_A) {
        this.password_A = password_A;
    }

    public void setPhone_A(String phone_A) {
        this.phone_A = phone_A;
    }

    public void setEmail_A(String email_A) {
        this.email_A = email_A;
    }

    public void setSex_A(int sex_A) {
        this.sex_A = sex_A;
    }

    public void setDob_A(Date dob_A) {
        this.dob_A = dob_A;
    }

    public void setRole_A(int role_A) {
        this.role_A = role_A;
    }

    public String getFullName_A() {
        return firstName_A + " " + lastName_A;
    }

    @Override
    public String toString() {
        return "Account{" + "adminID=" + adminID + ", username_A=" + username_A + ", firstName_A=" + firstName_A + ", lastName_A=" + lastName_A + ", password_A=" + password_A + ", phone_A=" + phone_A + ", email_A=" + email_A + ", sex_A=" + sex_A + ", dob_A=" + dob_A + ", role_A=" + role_A + '}';
    }
    
}
