package com.app.beauty.activities.customer.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.activities.ReviewPost;
import com.app.beauty.adapters.TypeRecyclerViewAdapter;
import com.app.beauty.models.CustomerAppointment;
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
    TextView tvNote;
    Spinner spn_type;
    Button btnRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_services);
        context = this;
        tempSaloon = CustomerSaloons.selectedSaloon;
        btnRate = findViewById(R.id.rate);
        btnRate.setVisibility(View.GONE);
        checkRating();
        initViews();
        try {
            initFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRv();
        initRvData();

    }

    private void checkRating() {
        Utils.getReference()
                .child(NODE_APPOINTMENTS)
                .child(Utils.getCurrentUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isAbleToGiveRating = false;
                        for (DataSnapshot child : snapshot.getChildren())
                            for (DataSnapshot grandChild : child.getChildren()) {
                                CustomerAppointment customerAppointment = grandChild.getValue(CustomerAppointment.class);

                                if (customerAppointment != null
                                        && !customerAppointment.getSaloonId().equals(tempSaloon.getManagerId()))
                                    continue;

                                if (customerAppointment == null)
                                    continue;
                                if (customerAppointment.getStatus().equals(STATUS_COMPLETED)) {
                                    isAbleToGiveRating = true;
                                    break;
                                }
                            }

                        if (isAbleToGiveRating)
                            btnRate.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void initRvData() {
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
    }

    private void initServices() {

        Utils.getReference().child(NODE_SERVICES)
                .child(tempSaloon.getManagerId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        superList.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            SaloonService saloonService = child.getValue(SaloonService.class);
                            Log.i(TAG, "onDataChange: " + saloonService);
                            if (saloonService != null
                                    && saloonService.getCategory() != null
                                    && saloonService.getCategory().equals(spn_type.getSelectedItem().toString()))
                                superList.add(saloonService);
                            try {
                                Log.i(TAG, "onDataChange: " + saloonService.getCategory());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (saloonService != null)
                                Log.i(TAG, "onDataChange: " + saloonService.getTitle());

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
        tvNote.setText(tempSaloon.getNote());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initServices();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.title);
        tvNote = findViewById(R.id.note);
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