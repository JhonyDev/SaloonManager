package com.app.beauty.activities.saloon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.activities.authentication.Login;
import com.app.beauty.models.Saloon;
import com.app.beauty.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SaloonManagerDashboard extends AppCompatActivity implements Info {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_manager);
        initSaloon();
    }

    private void initSaloon() {
        Utils.getReference()
                .child(NODE_SALOONS)
                .child(Utils.getCurrentUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Saloon saloon = snapshot.getValue(Saloon.class);
                        if (saloon != null)
                            Utils.currentSaloon = saloon;
                        else
                            Toast.makeText(SaloonManagerDashboard.this, "Please enter saloon details in saloon profile settings", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void seatsReserved(View view) {
        startActivity(new Intent(this, SaloonAppointmentsActivity.class));
    }

    public void addService(View view) {
        startActivity(new Intent(this, SaloonServicesActivity.class));
    }

    public void saloonProfileManagement(View view) {
        startActivity(new Intent(this, SaloonProfileSettings.class));
    }

    public void reviews(View view) {
        startActivity(new Intent(this, SaloonReviewList.class));
    }

    public void reviewsWeGave(View view) {
        startActivity(new Intent(this, SaloonCustomerReviews.class));
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }

    public void addStaff(View view) {
        startActivity(new Intent(this, StaffList.class));

    }
}