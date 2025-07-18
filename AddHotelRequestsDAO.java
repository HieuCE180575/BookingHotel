/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import DB.SQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.AddHotelRequest;
import java.util.List;
import models.City;

/**
 *
 * @author nguye
 */
public class AddHotelRequestsDAO {

    public List<AddHotelRequest> getAllRequest() {
        List<AddHotelRequest> list = new ArrayList<>();
        String sql;

     
            sql = "SELECT * FROM AddHotelRequests ORDER BY submitted_at DESC";
        
        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

           

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CityDAO cDAO = new CityDAO();
                    AddHotelRequest r = new AddHotelRequest(
                            rs.getInt("request_id"),
                            rs.getString("hotel_name"),
                            rs.getString("description"),
                            rs.getString("address"),
                            rs.getString("phone_contact"),
                            rs.getString("email_contact"),
                            rs.getString("image_url"),
                            cDAO.getCitybyID(rs.getInt("city_id")),
                            rs.getString("status"),
                            rs.getTimestamp("submitted_at")
                    );
                    list.add(r);
                }
            }

        } catch (Exception e) {

        }

        return list;
    }

    public AddHotelRequest getRequestById(int requestId) {
        String sql = "SELECT * FROM AddHotelRequests WHERE request_id = ?";
        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            try ( ResultSet rs = ps.executeQuery()) {
                CityDAO cDAO = new CityDAO();
                if (rs.next()) {
                    return new AddHotelRequest(
                            rs.getInt("request_id"),
                            rs.getString("hotel_name"),
                            rs.getString("description"),
                            rs.getString("address"),
                            rs.getString("phone_contact"),
                            rs.getString("email_contact"),
                            rs.getString("image_url"),
                            cDAO.getCitybyID(rs.getInt("city_id")),
                            rs.getString("status"),
                            rs.getTimestamp("submitted_at")
                    );
                }
            }

        } catch (Exception e) {

        }

        return null;
    }

    public boolean insertRequest(AddHotelRequest request) {
        String sql = "INSERT INTO AddHotelRequests (hotel_name, description, address, phone_contact, email_contact, image_url, city_id, status, submitted_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 'PENDING', GETDATE())";
        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, request.getHotelName());
            ps.setString(2, request.getDescription());
            ps.setString(3, request.getAddress());
            ps.setString(4, request.getPhoneContact());
            ps.setString(5, request.getEmailContact());
            ps.setString(6, request.getImageUrl());
            ps.setInt(7, request.getCity().getCityID());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateRequestStatus(int requestId, String newStatus) {
        String sql = "UPDATE AddHotelRequests SET status = ? WHERE request_id = ?";
        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, requestId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
       
            return false;
        }
    }
public static void main(String[] args) {
        try {
            // Thiết lập requestId để test
            int requestId = 1;

            // Tạo đối tượng DAO
            AddHotelRequestsDAO requestDAO = new AddHotelRequestsDAO();
            CityDAO cityDAO = new CityDAO();

            // Gọi hàm getRequestById
            AddHotelRequest request = requestDAO.getRequestById(requestId);

            // In kết quả
            if (request != null) {
                System.out.println("Request found:");
                System.out.println("Request ID: " + request.getRequestId());
                System.out.println("Hotel Name: " + request.getHotelName());
                System.out.println("Description: " + request.getDescription());
                System.out.println("Address: " + request.getAddress());
                System.out.println("Phone Contact: " + request.getPhoneContact());
                System.out.println("Email Contact: " + request.getEmailContact());
                System.out.println("Image URL: " + request.getImageUrl());
                City city = request.getCity();
                System.out.println("City ID: " + (city != null ? city.getCityID() : "null"));
                System.out.println("City Name: " + (city != null ? city.getCityName() : "null"));
                System.out.println("Status: " + request.getStatus());
                System.out.println("Submitted At: " + request.getSubmittedAt());
            } else {
                System.out.println("No request found for ID: " + requestId);
            }
        } catch (Exception e) {
            System.err.println("Error in test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
