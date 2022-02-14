package com.app.beauty.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.adapters.TypeRecyclerViewAdapter;
import com.app.beauty.models.CustomerReview;
import com.app.beauty.models.Super;
import com.app.beauty.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerReviewList extends AppCompatActivity implements Info {
    RecyclerView rvReviews;
    List<Super> superList;
    TypeRecyclerViewAdapter typeRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        initViews();
        initRv();
        initRvData();
    }

    private void initRv() {
        superList = new ArrayList<>();
        typeRecyclerViewAdapter
                = new TypeRecyclerViewAdapter(this, superList, Info.RV_TYPE_CUSTOMER_REVIEWS);
        rvReviews.setAdapter(typeRecyclerViewAdapter);
    }

    private void initRvData() {
        Utils.getReference()
                .child(NODE_REVIEWS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            for (DataSnapshot grandChild : child.getChildren()) {
                                CustomerReview customerReview = grandChild.getValue(CustomerReview.class);
                                if (customerReview == null)
                                    continue;

                                if (customerReview.getUserId().equals(Utils.getCurrentUserId()))
                                    superList.add(customerReview);
                            }
                        }
                        typeRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void initViews() {
        rvReviews = findViewById(R.id.rv_reviews);
    }

    public void back(View view) {
        finish();
    }
}