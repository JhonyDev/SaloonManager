package com.app.beauty.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.adapters.TypeRecyclerViewAdapter;
import com.app.beauty.models.SaloonService;
import com.app.beauty.models.Super;
import com.app.beauty.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SaloonServicesActivity extends AppCompatActivity implements Info {

    public static SaloonService selectedSaloonService;
    RecyclerView rvServices;
    List<Super> superList;
    TypeRecyclerViewAdapter typeRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_services);

        rvServices = findViewById(R.id.rv_services);
        initRv();
    }

    private void initRv() {
        superList = new ArrayList<>();
        typeRecyclerViewAdapter
                = new TypeRecyclerViewAdapter(this, superList, RV_TYPE_EDIT_SERVICES);
        rvServices.setAdapter(typeRecyclerViewAdapter);
    }

    private void initServices() {
        superList.clear();
        Utils.getReference()
                .child(NODE_SERVICES)
                .child(Utils.getCurrentUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            SaloonService saloonService = child.getValue(SaloonService.class);
                            superList.add(saloonService);
                        }
                        typeRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initServices();
    }

    public void back(View view) {
        finish();
    }

    public void addNewService(View view) {
        startActivity(new Intent(this, SaloonEditService.class));
    }
}