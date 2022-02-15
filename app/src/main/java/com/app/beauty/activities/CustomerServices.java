package com.app.beauty.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.adapters.TypeRecyclerViewAdapter;
import com.app.beauty.models.Saloon;
import com.app.beauty.models.SaloonService;
import com.app.beauty.models.Super;
import com.app.beauty.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerServices extends AppCompatActivity implements Info {

    public static Activity context;
    public static SaloonService selectedSaloonService;

    Saloon tempSaloon;
    TextView tvTitle;
    RecyclerView rvServices;
    List<Super> superList;
    TypeRecyclerViewAdapter typeRecyclerViewAdapter;
    TextView tvNoReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_services);

        context = this;

        tempSaloon = CustomerSaloons.selectedSaloon;
        initViews();


        try {
            initFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRv();
        initRvData();
    }

    private void initRvData() {
        Utils.getReference().child(NODE_SERVICES)
                .child(tempSaloon.getManagerId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            SaloonService saloonService = child.getValue(SaloonService.class);
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

    private void initRv() {
        superList = new ArrayList<>();
        typeRecyclerViewAdapter
                = new TypeRecyclerViewAdapter(context, superList, RV_TYPE_CUSTOMER_SERVICES);
        rvServices.setAdapter(typeRecyclerViewAdapter);
    }

    private void initFields() {
        tvTitle.setText(tempSaloon.getName());
    }

    private void initViews() {
        tvTitle = findViewById(R.id.title);
        rvServices = findViewById(R.id.rv_services);
        tvNoReview = findViewById(R.id.tv_no_review);
    }

    public void serviceHair(View view) {
        startActivity(new Intent(this, CustomerAppointmentActivity.class));
    }

    public void rateScreen(View view) {
        startActivity(new Intent(this, ReviewPost.class));
    }
}