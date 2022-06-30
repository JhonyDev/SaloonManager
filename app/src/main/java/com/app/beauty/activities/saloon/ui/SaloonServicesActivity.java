package com.app.beauty.activities.saloon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

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
    TextView tvNoReview;
    Spinner spn_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_services);

        rvServices = findViewById(R.id.rv_services);
        tvNoReview = findViewById(R.id.tv_no_review);
        spn_type = findViewById(R.id.spn_type);
        spn_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(getColor(R.color.white));
                initServices();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        superList.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            SaloonService saloonService = child.getValue(SaloonService.class);
                            if (saloonService != null
                                    && saloonService.getCategory() != null
                                    && saloonService.getCategory().equals(spn_type.getSelectedItem().toString()))
                                superList.add(saloonService);
                        }
                        if (superList.isEmpty())
                            tvNoReview.setVisibility(View.VISIBLE);
                        else
                            tvNoReview.setVisibility(View.GONE);
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