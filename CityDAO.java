/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import DB.SQL;
import models.City;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Image;

/**
 *
 * @author Nguyen Minh Tuong - CE182294
 */
public class CityDAO extends SQL {

    

 public City getCitybyID(int id) {
    String sql = "SELECT * FROM City WHERE CityID = ?";
    City city = null; // Khởi tạo là null để chỉ ra không tìm thấy thành phố
    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id); // Gán tham số TRƯỚC khi thực thi truy vấn
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) { // Sử dụng if vì CityID thường là duy nhất
                city = new City();
                city.setCityID(rs.getInt("CityID"));
                city.setCityName(rs.getString("Name_city"));
                city.setCountry(rs.getString("Country"));
                city.setImageURL(rs.getString("ImageURL"));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Ghi log lỗi để debug
        // Tùy chọn: ném ngoại lệ tùy chỉnh hoặc xử lý theo nhu cầu
    } catch (ClassNotFoundException ex) {
         Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE, null, ex);
     }
    return city; // Trả về null nếu không tìm thấy, hoặc đối tượng City đã được điền dữ liệu
}
//    public City[] find5CityTopBooking() {
//        City[] cities = new City[5];
//        String sql = "SELECT TOP 5 c.CityID, c.Name_City, COUNT(b.BookingID) AS BookingCount "
//                + "FROM Booking b "
//                + "JOIN Room r ON b.RoomID = r.RoomID "
//                + "JOIN RoomType rt ON r.RoomTypeID = rt.RoomTypeID "
//                + "JOIN Hotel h ON rt.HotelID = h.HotelID "
//                + "JOIN City c ON h.CityID = c.CityID "
//                + "GROUP BY c.CityID, c.CityName "
//                + "ORDER BY BookingCount DESC ";
//        try ( Connection con = SQL.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
//
//            int index = 0;
//            while (rs.next() && index < 5) {
//                City city = new City();
//                city.setCityID(rs.getInt("CityID"));
//                city.setCityName(rs.getString("CityName"));
//                city.setCountry("Country");
//                ImageDAO imgDao = new ImageDAO();
//                Image img = imgDao.getImagebyCityID(rs.getInt("CityID"));
//                city.setCityImg(img);
//                cities[index++] = city;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return cities;
//    }

    public City[] getAll() {
        City[] cities = new City[34];
        String sql = "SELECT * FROM City";

        try ( Connection con = getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            int index = 0;
            while (rs.next() && index < 34) {
                City city = new City();
                city.setCityID(rs.getInt("CityID"));
                city.setCityName(rs.getString("Name_city"));
                city.setCountry(rs.getString("Country"));
                city.setImageURL(rs.getString("ImageURL"));
                cities[index++] = city;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cities;
    }

    public City[] get5() {
        City[] cities = new City[5];
        String sql = "SELECT TOP 5 * FROM City";

        try ( Connection con = getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            int index = 0;
            while (rs.next() && index < 5) {
                City city = new City();
                city.setCityID(rs.getInt("CityID"));
                city.setCityName(rs.getString("Name_city"));
                city.setCountry(rs.getString("Country"));
                city.setImageURL(rs.getString("ImageURL"));

                cities[index++] = city;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cities;
    }

    public int getHotelID(String hotelName) {
        String sql = "SELECT CityID FROM City WHERE Name_city LIKE ?";

        try ( Connection con = getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            // Gán tham số truy vấn
            ps.setString(1, hotelName);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CityID");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0; // Nếu không tìm thấy
    }

    public static void main(String[] args) {
        CityDAO dao = new CityDAO();
        City city = dao.getCitybyID(1);
        System.err.println(city);

    }
}
