package com.app.beauty.activities.saloon.ui;

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
import com.app.beauty.models.Staff;
import com.app.beauty.models.Super;
import com.app.beauty.singletons.StaffSingleton;
import com.app.beauty.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StaffList extends AppCompatActivity implements Info {
    RecyclerView rvReviews;
    List<Super> superList;
    TypeRecyclerViewAdapter typeRecyclerViewAdapter;
    TextView tvNoReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);

        initViews();
        initRv();
        initRvData();
    }

    public void back(View view) {
        finish();
    }

    private void initRv() {
        superList = new ArrayList<>();
        typeRecyclerViewAdapter
                = new TypeRecyclerViewAdapter(this, superList, Info.RV_TYPE_STAFF_LIST);
        rvReviews.setAdapter(typeRecyclerViewAdapter);
    }

    private void initRvData() {
        Utils.getReference()
                .child(NODE_STAFF)
                .child(Utils.getCurrentUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        superList.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Staff staff = child.getValue(Staff.class);
                            superList.add(staff);
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

    private void initViews() {
        rvReviews = findViewById(R.id.rv_reviews);
        tvNoReview = findViewById(R.id.tv_no_review);
    }

    public void addNewStaff(View view) {
        StaffSingleton.setStaff(null);
        startActivity(new Intent(this, AddStaff.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRvData();
    }
}