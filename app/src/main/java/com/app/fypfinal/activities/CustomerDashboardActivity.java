package com.app.fypfinal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app.fypfinal.R;

public class CustomerDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

    }

    public void saloon(View view) {
        startActivity(new Intent(this, ListSaloonActivity.class));
    }

    public void myReviews(View view) {
        startActivity(new Intent(this, ReviewListActivity.class));
    }

    public void reviewsIGot(View view) {
        startActivity(new Intent(this, ReviewListActivity.class));
    }

    public void appointmentHistory(View view) {

    }
}