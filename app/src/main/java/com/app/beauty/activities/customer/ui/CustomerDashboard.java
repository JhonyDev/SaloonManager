package com.app.beauty.activities.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.R;
import com.app.beauty.activities.customer.ui.CustomerAppointmentHistory;
import com.app.beauty.activities.customer.ui.CustomerReviewList;
import com.app.beauty.activities.customer.ui.CustomerSaloonReviews;
import com.app.beauty.activities.customer.ui.CustomerSaloons;
import com.app.beauty.activities.authentication.Login;
import com.google.firebase.auth.FirebaseAuth;

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
        startActivity(new Intent(this, CustomerReviewList.class));
    }

    public void reviewsIGot(View view) {
        startActivity(new Intent(this, CustomerSaloonReviews.class));
    }

    public void appointmentHistory(View view) {
        startActivity(new Intent(this, CustomerAppointmentHistory.class));
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }

}