package com.app.beauty.activities.customer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.adapters.TypeRecyclerViewAdapter;
import com.app.beauty.models.Saloon;
import com.app.beauty.models.Super;
import com.app.beauty.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerSaloons extends AppCompatActivity implements Info {

    public static Activity context;
    public static Saloon selectedSaloon;
    RecyclerView rvSaloons;
    List<Super> superList;
    TypeRecyclerViewAdapter typeRecyclerViewAdapter;
    TextView tvNoReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_saloon_services);
        context = this;
        initViews();
        Toast.makeText(context, "Yellow days are saloon available days and unshaded days are saloon off days", Toast.LENGTH_SHORT).show();
        initRv();
        initRvData();
    }

    private void initViews() {

        rvSaloons = findViewById(R.id.rv_saloons);
        tvNoReview = findViewById(R.id.tv_no_review);
    }

    private void initRvData() {
        Utils.getReference()
                .child(NODE_SALOONS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Saloon saloon = child.getValue(Saloon.class);
                            superList.add(saloon);
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
                = new TypeRecyclerViewAdapter(this, superList, Info.RV_TYPE_SALOONS);
        rvSaloons.setAdapter(typeRecyclerViewAdapter);
    }

}