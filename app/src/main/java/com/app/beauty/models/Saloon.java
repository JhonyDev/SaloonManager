package com.app.beauty.models;

public class Saloon {
    String managerId; // Saloon Manager will have one salon.
    String managerName;
    String name;
    String workingDays;
    String timingFrom;
    String timingTo;
    String note;
    String phone;

    public Saloon() {
    }

    public Saloon(String managerId, String managerName, String name, String workingDays, String timingFrom, String timingTo, String note, String phone) {
        this.managerId = managerId;
        this.managerName = managerName;
        this.name = name;
        this.workingDays = workingDays;
        this.timingFrom = timingFrom;
        this.timingTo = timingTo;
        this.note = note;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public String getTimingFrom() {
        return timingFrom;
    }

    public void setTimingFrom(String timingFrom) {
        this.timingFrom = timingFrom;
    }

    public String getTimingTo() {
        return timingTo;
    }

    public void setTimingTo(String timingTo) {
        this.timingTo = timingTo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
