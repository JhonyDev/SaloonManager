package com.app.beauty.activities.dashboards;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.R;
import com.app.beauty.activities.CustomerSaloons;
import com.app.beauty.activities.ReviewList;

public class CustomerDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

    }

    public void saloon(View view) {
        startActivity(new Intent(this, CustomerSaloons.class));
    }

    public void myReviews(View view) {
        startActivity(new Intent(this, ReviewList.class));
    }

    public void reviewsIGot(View view) {
        startActivity(new Intent(this, ReviewList.class));
    }

}