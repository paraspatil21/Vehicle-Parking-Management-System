package com.vpms.dao;

import com.vpms.model.Resident;
import com.vpms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResidentDAO {
    public boolean addResident(Resident resident) {
        String sql = "INSERT INTO residents (name, flat_number, floor_number, contact_number, vehicle_number, vehicle_name, vehicle_type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resident.getName());
            stmt.setString(2, resident.getFlatNumber());
            stmt.setString(3, resident.getFloorNumber());
            stmt.setString(4, resident.getContactNumber());
            stmt.setString(5, resident.getVehicleNumber());
            stmt.setString(6, resident.getVehicleName());
            stmt.setString(7, resident.getVehicleType());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Resident> getAllResidents() {
        List<Resident> residents = new ArrayList<>();
        String sql = "SELECT r.*, s.slot_number FROM residents r LEFT JOIN slots s ON r.slot_id = s.id";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Resident r = new Resident(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("flat_number"),
                        rs.getString("floor_number"),
                        rs.getString("contact_number"),
                        rs.getString("vehicle_number"),
                        rs.getString("vehicle_name"),
                        rs.getString("vehicle_type"));
                r.setSlotId(rs.getInt("slot_id"));
                r.setSlotNumber(rs.getString("slot_number"));
                residents.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return residents;
    }

    public boolean updateResident(Resident resident) {
        String sql = "UPDATE residents SET name=?, flat_number=?, floor_number=?, contact_number=?, vehicle_number=?, vehicle_name=?, vehicle_type=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resident.getName());
            stmt.setString(2, resident.getFlatNumber());
            stmt.setString(3, resident.getFloorNumber());
            stmt.setString(4, resident.getContactNumber());
            stmt.setString(5, resident.getVehicleNumber());
            stmt.setString(6, resident.getVehicleName());
            stmt.setString(7, resident.getVehicleType());
            stmt.setInt(8, resident.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteResident(int id) {
        String sql = "DELETE FROM residents WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Resident getResidentByVehicle(String vehicleNumber) {
        String sql = "SELECT r.*, s.slot_number FROM residents r LEFT JOIN slots s ON r.slot_id = s.id WHERE r.vehicle_number = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vehicleNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Resident r = new Resident(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("flat_number"),
                        rs.getString("floor_number"),
                        rs.getString("contact_number"),
                        rs.getString("vehicle_number"),
                        rs.getString("vehicle_name"),
                        rs.getString("vehicle_type"));
                r.setSlotId(rs.getInt("slot_id"));
                r.setSlotNumber(rs.getString("slot_number"));
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
