/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import DB.SQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import models.Booking;
import models.Room;
import models.Voucher;

/**
 *
 * @author Phong
 */
public class BookingDAO {

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM Booking";

        try (
                 Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingID(rs.getInt("BookingID"));
                booking.setCheckinDate(rs.getDate("Checkin_Date"));
                booking.setCheckoutDate(rs.getDate("Checkout_Date"));
                booking.setVAT(rs.getDouble("VAT"));
                booking.setTimeBooking(rs.getTimestamp("TimeBooking"));
                booking.setUsername(rs.getString("Username"));
                booking.setStatus_B(rs.getString("Status_B"));

                VoucherDAO vDao = new VoucherDAO();
                Voucher voucher = vDao.getVoucherbyID(rs.getInt("VoucherID"));
                booking.setVoucher(voucher);

                // Get rooms from BookingRoom table
                List<Room> rooms = getRoomsForBooking(rs.getInt("BookingID"));
                booking.setRoom(rooms);

                bookings.add(booking);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    public Booking getBookingById(int id) {
        String query = "SELECT * FROM Booking WHERE BookingID = ?";
        try (
                 Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingID(rs.getInt("BookingID"));
                    booking.setCheckinDate(rs.getDate("Checkin_Date"));
                    booking.setCheckoutDate(rs.getDate("Checkout_Date"));
                    booking.setVAT(rs.getDouble("VAT"));
                    booking.setTimeBooking(rs.getTimestamp("TimeBooking"));
                    booking.setUsername(rs.getString("Username"));
                    booking.setStatus_B(rs.getString("Status_B"));

                    VoucherDAO vDao = new VoucherDAO();
                    Voucher voucher = vDao.getVoucherbyID(rs.getInt("VoucherID"));
                    booking.setVoucher(voucher);

                    // Get rooms from BookingRoom table
                    List<Room> rooms = getRoomsForBooking(id);
                    booking.setRoom(rooms);

                    return booking;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Room> getRoomsForBooking(int bookingID) throws SQLException, ClassNotFoundException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT RoomID FROM BookingRoom WHERE BookingID = ?";

        try (
                 Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, bookingID);
            try ( ResultSet rs = ps.executeQuery()) {
                RoomDAO rDao = new RoomDAO();
                while (rs.next()) {
                    Room room = rDao.getRoomByID(rs.getInt("RoomID"));
                    if (room != null) {
                        rooms.add(room);
                    }
                }
            }
        }
        return rooms;
    }

    public boolean updateBookingStatus(int bookingID, String newStatus) {
        String query = "UPDATE Booking SET Status_B = ? WHERE BookingID = ?";
        try (
                 Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newStatus);
            ps.setInt(2, bookingID);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int insertBookingAndReturnID(Booking booking) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Booking (Checkin_Date, Checkout_Date, VAT, TimeBooking, Username, Status_B, VoucherID) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                 Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, booking.getCheckinDate());
            ps.setDate(2, booking.getCheckoutDate());
            ps.setDouble(3, booking.getVAT());
            ps.setTimestamp(4, booking.getTimeBooking());
            ps.setString(5, booking.getUsername());
            ps.setString(6, booking.getStatus_B() != null ? booking.getStatus_B() : "Pending");

            if (booking.getVoucher() != null) {
                ps.setInt(7, booking.getVoucher().getVoucherID());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed, no rows affected.");
            }

            try ( ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int bookingID = rs.getInt(1);
                    // Insert rooms into BookingRoom table
                    if (booking.getRoom() != null) {
                        for (Room room : booking.getRoom()) {
                            insertBookingRoom(bookingID, room.getRoomId());
                        }
                    }
                    return bookingID;
                } else {
                    throw new SQLException("Creating booking failed, no ID obtained.");
                }
            }
        }
    }

    public void insertBookingRoom(int bookingID, int roomID) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO BookingRoom (BookingID, RoomID) VALUES (?, ?)";

        try (
                 Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingID);
            ps.setInt(2, roomID);
            ps.executeUpdate();
        }
    }

    public List<Booking> getAllBookingByStatus(String status) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM Booking WHERE Status_B = ?";

        try (
                 Connection conn = SQL.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, status);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingID(rs.getInt("BookingID"));
                    booking.setCheckinDate(rs.getDate("Checkin_Date"));
                    booking.setCheckoutDate(rs.getDate("Checkout_Date"));
                    booking.setVAT(rs.getDouble("VAT"));
                    booking.setTimeBooking(rs.getTimestamp("TimeBooking"));
                    booking.setUsername(rs.getString("Username"));
                    booking.setStatus_B(rs.getString("Status_B"));
                    VoucherDAO vDao = new VoucherDAO();
                    Voucher voucher = vDao.getVoucherbyID(rs.getInt("VoucherID"));
                    booking.setVoucher(voucher);
                    List<Room> rooms = getRoomsForBooking(rs.getInt("BookingID"));
                    booking.setRoom(rooms);
                    bookings.add(booking);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
        }

        return bookings;
    }

    public List<Booking> getAllBookingByStatus(int hotelID, String status) {
    List<Booking> bookings = new ArrayList<>();
    String query = "SELECT DISTINCT b.* "
                 + "FROM Booking b "
                 + "JOIN BookingRoom br ON b.BookingID = br.BookingID "
                 + "JOIN Room r ON br.RoomID = r.RoomID "
                 + "JOIN RoomType rt ON r.RoomTypeID = rt.RoomTypeID "
                 + "WHERE b.Status_B = ? AND rt.HotelID = ?";

    try (
        Connection conn = SQL.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)
    ) {
        ps.setString(1, status);
        ps.setInt(2, hotelID);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingID(rs.getInt("BookingID"));
                booking.setCheckinDate(rs.getDate("Checkin_Date"));
                booking.setCheckoutDate(rs.getDate("Checkout_Date"));
                booking.setVAT(rs.getDouble("VAT"));
                booking.setTimeBooking(rs.getTimestamp("TimeBooking"));
                booking.setUsername(rs.getString("Username"));
                booking.setStatus_B(rs.getString("Status_B"));

                // Voucher
                VoucherDAO vDao = new VoucherDAO();
                Voucher voucher = vDao.getVoucherbyID(rs.getInt("VoucherID"));
                booking.setVoucher(voucher);

                // Rooms
                List<Room> rooms = getRoomsForBooking(rs.getInt("BookingID"));
                booking.setRoom(rooms);

                bookings.add(booking);
            }
        }
    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace(); 
    }

    return bookings;
}


    public static void main(String[] args) {
        BookingDAO dao = new BookingDAO();
        Booking bk = dao.getBookingById(5);
        System.out.println(bk);
        System.out.println(bk.getAmount());
    }
}
