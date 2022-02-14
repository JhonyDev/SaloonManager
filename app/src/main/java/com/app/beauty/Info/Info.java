package com.app.beauty.Info;

public interface Info {
    String TAG = "mytag";

    String NODE_USER = "Users";
    String NODE_SALOONS = "Saloons";
    String NODE_SERVICES = "Services";
    String NODE_APPOINTMENTS = "Appointments";

    String STATUS_CONFIRMED = "Confirmed";
    String STATUS_PENDING = "Pending";
    String STATUS_REJECTED = "Rejected";

    int RV_TYPE_EDIT_SERVICES = 1;
    int RV_TYPE_SALOONS = 2;
    int RV_TYPE_CUSTOMER_SERVICES = 3;


    String EXTRA_IS_FROM_SERVICE_EDIT = "zxcasdd";

    String CUSTOMER = "Customer";
    String SALOON_MANAGER = "SaloonManager";
}
