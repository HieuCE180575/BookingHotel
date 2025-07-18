/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import DB.SQL;
import models.Hotel;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.City;

/**
 *
 * @author Nguyen Minh Tuong - CE182294
 */
public class HotelDAO extends SQL {

    public boolean insertHotel(Hotel hotel) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Hotel (Hotel_Name, description_H, CityID,Address,phone,email) VALUES (?, ?, ?,?,?,?)";
        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hotel.getHotelName());
            ps.setString(2, hotel.getDescription());
            ps.setInt(3, hotel.getCityID());
            ps.setString(4, hotel.getAddress());
            ps.setString(5, hotel.getEmail());
            ps.setString(6, hotel.getPhone());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     *
     * @param cityID
     * @return
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public Hotel[] getTop5HotelsByRating(int cityID) throws SQLException, ClassNotFoundException {
        Hotel[] hotels = new Hotel[5];
        String sql = "SELECT TOP 5 h.HotelID, h.Hotel_Name, h.Address, h.description_H, "
                + "c.Name_city, "
                + "ISNULL(AVG(CAST(r.Rating AS FLOAT)), 0) AS AvgRating, "
                + "COUNT(r.ReviewsID) AS ReviewCount, "
                + "ISNULL((SELECT TOP 1 i.ImageURL FROM Image i WHERE i.HotelID = h.HotelID ORDER BY i.ImageID), '') AS ImageURL "
                + "FROM Hotel h "
                + "JOIN City c ON h.CityID = c.CityID "
                + "LEFT JOIN Reviews r ON r.HotelID = h.HotelID "
                + "WHERE h.CityID = ? "
                + "GROUP BY h.HotelID, h.Hotel_Name, h.Address, h.description_H, c.Name_city "
                + "ORDER BY AvgRating DESC";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cityID);
            ResultSet rs = ps.executeQuery();

            int count = 0;
            while (rs.next() && count < 5) {
                Hotel hotel = new Hotel();
                hotel.setHotelID(rs.getInt("HotelID"));
                hotel.setHotelName(rs.getString("Hotel_Name"));
                hotel.setAddress(rs.getString("Address"));
                hotel.setDescription(rs.getString("description_H"));
                hotel.setCityName(rs.getString("Name_city"));
                hotel.setRating(rs.getDouble("AvgRating"));
                hotel.setReviewCount(rs.getInt("ReviewCount"));

                ImageDAO imgDAO = new ImageDAO();
                hotel.setImg(imgDAO.getImgByHotelID(rs.getInt("HotelID")));
                hotels[count++] = hotel;
            }
        } catch (Exception e) {

        }

        return hotels;
    }

    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT h.HotelID, h.Hotel_Name, h.Address, h.description_H, c.Name_city, "
                + "CAST(ISNULL(AVG(CAST(r.Rating AS FLOAT)), 0) AS DECIMAL(3,1)) AS AvgRating, "
                + "COUNT(r.ReviewsID) AS ReviewCount, "
                + "ISNULL((SELECT TOP 1 i.ImageURL FROM Image i WHERE i.HotelID = h.HotelID ORDER BY i.ImageID), '') AS ImageURL "
                + "FROM Hotel h "
                + "JOIN City c ON h.CityID = c.CityID "
                + "LEFT JOIN Reviews r ON r.HotelID = h.HotelID "
                + "GROUP BY h.HotelID, h.Hotel_Name, h.Address, h.description_H, c.Name_city";

        try ( ResultSet rs = SQL.executeQuery(sql)) {
            while (rs != null && rs.next()) {
                Hotel hotel = new Hotel();
                hotel.setHotelID(rs.getInt("HotelID"));
                hotel.setHotelName(rs.getString("Hotel_Name"));
                hotel.setAddress(rs.getString("Address"));
                hotel.setDescription(rs.getString("description_H"));
                hotel.setCityName(rs.getString("Name_city"));
                hotel.setRating(rs.getDouble("AvgRating"));
                hotel.setReviewCount(rs.getInt("ReviewCount"));
                ImageDAO imgDAO = new ImageDAO();
                hotel.setImg(imgDAO.getImgByHotelID(rs.getInt("HotelID")));
                hotels.add(hotel);
            }
        } catch (Exception e) {

        }
        return hotels;
    }

    public Hotel getHotelById(int id) {
        String sql = "SELECT h.HotelID, h.Hotel_Name, h.Address, h.description_H, c.Name_city,h.CityID, "
                + "ISNULL(AVG(CAST(r.Rating AS FLOAT)), 0) AS AvgRating, "
                + "COUNT(r.ReviewsID) AS ReviewCount, "
                + "ISNULL((SELECT TOP 1 i.ImageURL FROM Image i WHERE i.HotelID = h.HotelID ORDER BY i.ImageID), '') AS ImageURL "
                + "FROM Hotel h "
                + "JOIN City c ON h.CityID = c.CityID "
                + "LEFT JOIN Reviews r ON h.HotelID = r.HotelID "
                + "WHERE h.HotelID = ? "
                + "GROUP BY h.HotelID, h.Hotel_Name, h.Address, h.description_H, c.Name_city,h.CityID";

        try ( ResultSet rs = SQL.executeQuery(sql, id)) {
            if (rs.next()) {
                ImageDAO imgDAO = new ImageDAO();
                Hotel hotel = new Hotel();
                hotel.setHotelID(rs.getInt("HotelID"));
                hotel.setHotelName(rs.getString("Hotel_Name"));
                hotel.setAddress(rs.getString("Address"));
                hotel.setDescription(rs.getString("description_H"));
                hotel.setCityName(rs.getString("Name_city"));
                hotel.setRating(rs.getDouble("AvgRating"));
                hotel.setReviewCount(rs.getInt("ReviewCount"));
                hotel.setCityID(rs.getInt("CityID"));
                hotel.setImg(imgDAO.getImgByHotelID(rs.getInt("HotelID")));
                return hotel;
            }
        } catch (Exception e) {

        }
        return null;
    }

    public List<String> getImagesByHotel(int hotelId, boolean excludeFirst) {
        List<String> images = new ArrayList<>();
        String sql = excludeFirst
                ? "SELECT ImageURL FROM Image WHERE HotelID = ? ORDER BY ImageID OFFSET 1 ROWS"
                : "SELECT ImageURL FROM Image WHERE HotelID = ? ORDER BY ImageID";

        try ( ResultSet rs = SQL.executeQuery(sql, hotelId)) {
            while (rs.next()) {
                images.add(rs.getString("ImageURL"));
            }
        } catch (Exception e) {
        }
        return images;
    }

    public List<Hotel> getHotelsByCityID(int cityID) {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT h.HotelID, h.Hotel_Name, h.Address, h.description_H, c.Name_city, "
                + "CAST(ISNULL(AVG(CAST(r.Rating AS FLOAT)), 0) AS DECIMAL(3,1)) AS AvgRating, "
                + "COUNT(r.ReviewsID) AS ReviewCount, "
                + "ISNULL((SELECT TOP 1 i.ImageURL FROM Image i WHERE i.HotelID = h.HotelID ORDER BY i.ImageID), '') AS ImageURL "
                + "FROM Hotel h "
                + "JOIN City c ON h.CityID = c.CityID "
                + "LEFT JOIN Reviews r ON r.HotelID = h.HotelID "
                + "WHERE h.CityID = ? "
                + "GROUP BY h.HotelID, h.Hotel_Name, h.Address, h.description_H, c.Name_city";

        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cityID);
            ResultSet rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                Hotel hotel = new Hotel();
                hotel.setHotelID(rs.getInt("HotelID"));
                hotel.setHotelName(rs.getString("Hotel_Name"));
                hotel.setAddress(rs.getString("Address"));
                hotel.setDescription(rs.getString("description_H"));
                hotel.setCityName(rs.getString("Name_city"));
                hotel.setRating(rs.getDouble("AvgRating"));
                hotel.setReviewCount(rs.getInt("ReviewCount"));

                ImageDAO imgDAO = new ImageDAO();
                hotel.setImg(imgDAO.getImgByHotelID(rs.getInt("HotelID"))); // dùng DAO nếu bạn cần nhiều ảnh
                hotels.add(hotel);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return hotels;
    }
// Trả về số phòng còn trống

    public int getAvailableRoomCountInDateRange(int hotelID, Date checkIn, Date checkOut) throws Exception {
        String sql
                = "SELECT COUNT(*) AS AvailableRoomCount "
                + "FROM Room r "
                + "JOIN Roomtype rt ON r.RoomtypeID = rt.RoomtypeID "
                + "WHERE rt.HotelID = ? "
                + "AND r.RoomID NOT IN ( "
                + "    SELECT br.RoomID "
                + "    FROM Booking b "
                + "    JOIN BookingRoom br ON b.BookingID = br.BookingID "
                + "    WHERE NOT ( "
                + "        b.Checkout_Date <= ? OR "
                + "        b.Checkin_Date >= ? "
                + "    ) "
                + ")";

        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hotelID);
            ps.setDate(2, checkIn);
            ps.setDate(3, checkOut);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("AvailableRoomCount");
                }
            }
        }

        return 0;
    }

    // Trả về tổng sức chứa còn trống
    public int getTotalCapacityInDateRange(int hotelID, Date checkIn, Date checkOut) throws Exception {
        String sql
                = "SELECT SUM(rt.Capacity) AS TotalAvailableCapacity "
                + "FROM Room r "
                + "JOIN Roomtype rt ON r.RoomtypeID = rt.RoomtypeID "
                + "WHERE rt.HotelID = ? "
                + "AND r.RoomID NOT IN ( "
                + "    SELECT br.RoomID "
                + "    FROM Booking b "
                + "    JOIN BookingRoom br ON b.BookingID = br.BookingID "
                + "    WHERE NOT ( "
                + "        b.Checkout_Date <= ? OR "
                + "        b.Checkin_Date >= ? "
                + "    ) "
                + ")";

        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hotelID);
            ps.setDate(2, checkIn);
            ps.setDate(3, checkOut);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("TotalAvailableCapacity");
                }
            }
        }

        return 0;
    }

    public int insertHotel(String hotelName, String hotelAddress, String hotelDescription,
            String hotelEmail, String hotelPhone, String cityID) {
        int hotelId = -1;

        String sql = "INSERT INTO Hotel (Hotel_Name, Address, description_H, Email, Phone, CityID) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try ( Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, hotelName);
            ps.setString(2, hotelAddress);
            ps.setString(3, hotelDescription);
            ps.setString(4, hotelEmail);
            ps.setString(5, hotelPhone);
            ps.setInt(6, Integer.parseInt(cityID));

            ps.executeUpdate();

            try ( ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    hotelId = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hotelId;
    }

    public static void main(String[] args) {
        try {
            // Tạo đối tượng Hotel
            Hotel hotel = new Hotel();
            hotel.setHotelName("Test Hotel");
            hotel.setDescription("A beautiful hotel for testing");
            hotel.setAddress("123 Test Street, Test City");

            // Tạo đối tượng City và gán CityID
            hotel.setCityID(1);

            // Tạo HotelDAO và gọi insertHotel
            HotelDAO hotelDAO = new HotelDAO();
            boolean result = hotelDAO.insertHotel(hotel);

            // In kết quả
            if (result) {
                System.out.println("Hotel inserted successfully: " + hotel.getHotelName());
            } else {
                System.out.println("Failed to insert hotel.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
