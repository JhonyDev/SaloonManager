package com.app.beauty.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.R;

public class SaloonServicesActivity extends AppCompatActivity {

    public static Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_services);
        context = this;
    }

    public void serviceHair(View view) {
        startActivity(new Intent(this, AppointmentScreenActivity.class));
    }

    public void rateScreen(View view) {
        startActivity(new Intent(this, ReviewScreenActivity.class));
    }
}