package com.vpms.dao;

import com.vpms.model.Slot;
import com.vpms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SlotDAO {
    public List<Slot> getAllSlots() {
        List<Slot> slots = new ArrayList<>();
        String sql = "SELECT s.*, e.visitor_name, e.vehicle_name, e.vehicle_number, e.vehicle_type, e.contact_number, "
                +
                "e.arrival_time, e.expected_departure, e.visit_type, e.reason, e.resident_name, e.flat_number, e.floor_number "
                +
                "FROM slots s " +
                "LEFT JOIN parking_entries e ON s.id = e.slot_id AND e.status = 'PARKED'";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Slot s = new Slot(
                        rs.getInt("id"),
                        rs.getString("slot_number"),
                        rs.getString("type"),
                        rs.getString("status"));

                // Set joined data
                s.setOwnerName(rs.getString("visitor_name"));
                s.setVehicleName(rs.getString("vehicle_name"));
                s.setVehicleNumber(rs.getString("vehicle_number"));
                s.setVehicleType(rs.getString("vehicle_type"));
                s.setContactNumber(rs.getString("contact_number"));
                s.setArrivalTime(rs.getTimestamp("arrival_time"));
                s.setExpectedDeparture(rs.getTimestamp("expected_departure"));
                s.setVisitType(rs.getString("visit_type"));
                s.setReason(rs.getString("reason"));
                s.setResidentName(rs.getString("resident_name"));

                String flat = rs.getString("flat_number");
                String floor = rs.getString("floor_number");
                if (flat != null && floor != null) {
                    s.setFlatFloor(flat + " - " + floor);
                }

                slots.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    public List<Slot> getAvailableSlotsByType(String type) {
        List<Slot> slots = new ArrayList<>();
        String sql = "SELECT * FROM slots WHERE type = ? AND status = 'AVAILABLE'";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                slots.add(new Slot(
                        rs.getInt("id"),
                        rs.getString("slot_number"),
                        rs.getString("type"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    public boolean updateSlotStatus(int slotId, String status) {
        String sql = "UPDATE slots SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, slotId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Slot getSlotById(int id) {
        String sql = "SELECT * FROM slots WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Slot(
                        rs.getInt("id"),
                        rs.getString("slot_number"),
                        rs.getString("type"),
                        rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addSlot(Slot slot) {
        String sql = "INSERT INTO slots (slot_number, type, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, slot.getSlotNumber());
            stmt.setString(2, slot.getType());
            stmt.setString(3, slot.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteSlot(int id) {
        String sql = "DELETE FROM slots WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
