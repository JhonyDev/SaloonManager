package com.app.beauty.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.R;

public class AppointmentScreenActivity extends AppCompatActivity {

    public static Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_screen);
        context = this;
    }

    public void back(View view) {
        finish();
    }

    public void confirm(View view) {
        Toast.makeText(this, "Payment Confirmed", Toast.LENGTH_SHORT).show();
        ListSaloonActivity.context.finish();
        SaloonServicesActivity.context.finish();
        finish();
    }
}