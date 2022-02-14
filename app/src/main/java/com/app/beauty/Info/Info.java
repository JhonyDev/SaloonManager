package com.app.beauty.Info;

public interface Info {
    String TAG = "mytag";

    String NODE_USER = "Users";
    String NODE_SALOONS = "Saloons";
    String NODE_SERVICES = "Services";
    String NODE_APPOINTMENTS = "Appointments";
    String NODE_REVIEWS = "Reviews";

    String STATUS_CONFIRMED = "Confirmed";
    String STATUS_PENDING = "Pending";
    String STATUS_REJECTED = "Rejected";

    int RV_TYPE_EDIT_SERVICES = 1;
    int RV_TYPE_SALOONS = 2;
    int RV_TYPE_CUSTOMER_SERVICES = 3;
    int RV_TYPE_CUSTOMER_APPOINTMENTS = 4;
    int RV_TYPE_SALOON_APPOINTMENTS = 5;
    int RV_TYPE_CUSTOMER_REVIEWS = 6;


    String EXTRA_IS_FROM_SERVICE_EDIT = "zxcasdd";
    String EXTRA_IS_FROM_SALOON = "zcxzxcnbmadsb";

    String CUSTOMER = "Customer";
    String SALOON_MANAGER = "SaloonManager";
}
