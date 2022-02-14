package com.app.beauty.models;

public class CustomerAppointment {
    String appointmentId;
    String saloonId;
    String userId;
    String appointmentDate;
    String appointmentTime;
    String charges;
    String txid;
    String status;

    public CustomerAppointment() {
    }

    public CustomerAppointment(String appointmentId, String saloonId, String userId, String appointmentDate, String appointmentTime, String charges, String txid) {
        this.appointmentId = appointmentId;
        this.saloonId = saloonId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.charges = charges;
        this.txid = txid;
        this.userId = userId;
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

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
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
