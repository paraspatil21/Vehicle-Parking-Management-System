package com.vpms.model;

public class Slot {
    private int id;
    private String slotNumber;
    private String type;
    private String status;

    // Fields for joined active parking data
    private String ownerName;
    private String vehicleName;
    private String vehicleNumber;
    private String vehicleType;
    private String contactNumber;
    private java.sql.Timestamp arrivalTime;
    private java.sql.Timestamp expectedDeparture;
    private String visitType;
    private String reason;
    private String residentName;
    private String flatFloor;

    public Slot() {
    }

    public Slot(int id, String slotNumber, String type, String status) {
        this.id = id;
        this.slotNumber = slotNumber;
        this.type = type;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public java.sql.Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(java.sql.Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public java.sql.Timestamp getExpectedDeparture() {
        return expectedDeparture;
    }

    public void setExpectedDeparture(java.sql.Timestamp expectedDeparture) {
        this.expectedDeparture = expectedDeparture;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getFlatFloor() {
        return flatFloor;
    }

    public void setFlatFloor(String flatFloor) {
        this.flatFloor = flatFloor;
    }

    @Override
    public String toString() {
        return slotNumber + " (" + type + ")";
    }
}
