package com.app.beauty.models;

public class CustomerAppointment extends Super {
    String appointmentId;
    String saloonId;
    String userId;
    String appointmentDate;
    String customerName;
    String selectedTimeSlot;
    String charges;
    String txid;
    String status;
    String saloonName;
    String serviceTitle;
    String requestedStaff;

    public CustomerAppointment() {

    }

    public CustomerAppointment(String appointmentId,
                               String saloonId,
                               String userId,
                               String appointmentDate,
                               String selectedTimeSlot,
                               String charges,
                               String txid) {
        this.appointmentId = appointmentId;
        this.saloonId = saloonId;
        this.appointmentDate = appointmentDate;
        this.selectedTimeSlot = selectedTimeSlot;
        this.charges = charges;
        this.txid = txid;
        this.userId = userId;
    }

    public String getRequestedStaff() {
        return requestedStaff;
    }

    public void setRequestedStaff(String requestedStaff) {
        this.requestedStaff = requestedStaff;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String getSaloonName() {
        return saloonName;
    }

    public void setSaloonName(String saloonName) {
        this.saloonName = saloonName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSaloonId() {
        return saloonId;
    }

    public void setSaloonId(String saloonId) {
        this.saloonId = saloonId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getSelectedTimeSlot() {
        return selectedTimeSlot;
    }

    public void setSelectedTimeSlot(String selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }
}
