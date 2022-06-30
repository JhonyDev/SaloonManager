package com.app.beauty.singletons;

import com.app.beauty.models.Staff;

public class StaffSingleton {
    public static Staff staff;

    private StaffSingleton() {
    }

    public static Staff getStaff() {
        return staff;
    }

    public static void setStaff(Staff staff) {
        StaffSingleton.staff = staff;
    }
}
