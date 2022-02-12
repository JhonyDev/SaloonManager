package com.app.beauty.activities.dashboards;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.activities.SaloonProfileSettings;
import com.app.beauty.models.Saloon;
import com.app.beauty.utils.Utils;
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
                            Toast.makeText(SaloonManagerDashboard.this, "Saloon not Found", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void appointmentHistory(View view) {

    }

    public void seatsReserved(View view) {

    }

    public void addService(View view) {

    }

    public void saloonProfileManagement(View view) {
        startActivity(new Intent(this, SaloonProfileSettings.class));
    }

    public void reviews(View view) {

    }
}