package com.vpms.dao;

import com.vpms.model.ParkingEntry;
import com.vpms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingEntryDAO {
    public boolean saveEntry(ParkingEntry entry) {
        String sqlEntry = "INSERT INTO parking_entries (visitor_name, vehicle_name, vehicle_number, vehicle_type, contact_number, visit_type, reason, resident_name, flat_number, floor_number, slot_id, expected_departure, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'PARKED')";
        String sqlSlot = "UPDATE slots SET status = 'OCCUPIED' WHERE id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // ACID: Atomicity starts here

            try (PreparedStatement pstmtEntry = conn.prepareStatement(sqlEntry);
                    PreparedStatement pstmtSlot = conn.prepareStatement(sqlSlot)) {

                pstmtEntry.setString(1, entry.getVisitorName());
                pstmtEntry.setString(2, entry.getVehicleName());
                pstmtEntry.setString(3, entry.getVehicleNumber());
                pstmtEntry.setString(4, entry.getVehicleType());
                pstmtEntry.setString(5, entry.getContactNumber());
                pstmtEntry.setString(6, entry.getVisitType());
                pstmtEntry.setString(7, entry.getReason());
                pstmtEntry.setString(8, entry.getResidentName());
                pstmtEntry.setString(9, entry.getFlatNumber());
                pstmtEntry.setString(10, entry.getFloorNumber());
                pstmtEntry.setInt(11, entry.getSlotId());
                pstmtEntry.setTimestamp(12, entry.getExpectedDeparture());
                pstmtEntry.executeUpdate();

                pstmtSlot.setInt(1, entry.getSlotId());
                pstmtSlot.executeUpdate();

                conn.commit(); // ACID: Consistency and Durability
                return true;
            } catch (SQLException e) {
                if (conn != null)
                    conn.rollback(); // ACID: Atomicity (All or nothing)
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
            }
        }
        return false;
    }

    public List<ParkingEntry> getActiveEntries() {
        List<ParkingEntry> entries = new ArrayList<>();
        String sql = "SELECT e.*, s.slot_number FROM parking_entries e JOIN slots s ON e.slot_id = s.id WHERE e.status = 'PARKED'";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ParkingEntry e = mapResultSetToEntry(rs);
                e.setSlotNumber(rs.getString("slot_number"));
                entries.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }

    public List<ParkingEntry> getAllEntries() {
        List<ParkingEntry> entries = new ArrayList<>();
        String sql = "SELECT e.*, s.slot_number FROM parking_entries e LEFT JOIN slots s ON e.slot_id = s.id";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ParkingEntry e = mapResultSetToEntry(rs);
                e.setSlotNumber(rs.getString("slot_number"));
                entries.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }

    public boolean markExit(int entryId, int slotId, String duration) {
        String sqlEntry = "UPDATE parking_entries SET actual_departure = CURRENT_TIMESTAMP, parking_duration = ?, status = 'EXITED' WHERE entry_id = ?";
        String sqlSlot = "UPDATE slots SET status = 'AVAILABLE' WHERE id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Transaction for ACID properties

            try (PreparedStatement pstmtEntry = conn.prepareStatement(sqlEntry);
                    PreparedStatement pstmtSlot = conn.prepareStatement(sqlSlot)) {

                pstmtEntry.setString(1, duration);
                pstmtEntry.setInt(2, entryId);
                pstmtEntry.executeUpdate();

                pstmtSlot.setInt(1, slotId);
                pstmtSlot.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                if (conn != null)
                    conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
            }
        }
        return false;
    }

    private ParkingEntry mapResultSetToEntry(ResultSet rs) throws SQLException {
        ParkingEntry e = new ParkingEntry();
        e.setEntryId(rs.getInt("entry_id"));
        e.setVisitorName(rs.getString("visitor_name"));
        e.setVehicleName(rs.getString("vehicle_name"));
        e.setVehicleNumber(rs.getString("vehicle_number"));
        e.setVehicleType(rs.getString("vehicle_type"));
        e.setContactNumber(rs.getString("contact_number"));
        e.setVisitType(rs.getString("visit_type"));
        e.setReason(rs.getString("reason"));
        e.setResidentName(rs.getString("resident_name"));
        e.setFlatNumber(rs.getString("flat_number"));
        e.setFloorNumber(rs.getString("floor_number"));
        e.setSlotId(rs.getInt("slot_id"));
        e.setArrivalTime(rs.getTimestamp("arrival_time"));
        e.setExpectedDeparture(rs.getTimestamp("expected_departure"));
        e.setActualDeparture(rs.getTimestamp("actual_departure"));
        e.setParkingDuration(rs.getString("parking_duration"));
        e.setStatus(rs.getString("status"));
        return e;
    }
}
