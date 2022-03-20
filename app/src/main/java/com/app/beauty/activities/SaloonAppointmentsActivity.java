package com.app.beauty.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.adapters.TypeRecyclerViewAdapter;
import com.app.beauty.models.CustomerAppointment;
import com.app.beauty.models.Super;
import com.app.beauty.utils.DialogUtils;
import com.app.beauty.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SaloonAppointmentsActivity extends AppCompatActivity implements Info {
    public static CustomerAppointment selectedAppointment;
    RecyclerView rvAppointments;
    List<Super> superList;
    TypeRecyclerViewAdapter typeRecyclerViewAdapter;
    Dialog loadingDialog;
    TextView tvNoReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_appointments);

        loadingDialog = new Dialog(this);
        DialogUtils.initLoadingDialog(loadingDialog);
        initViews();
        initRv();
        initRvData();
    }

    private void initRvData() {
        loadingDialog.show();
        Utils.getReference()
                .child(NODE_APPOINTMENTS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingDialog.dismiss();
                        superList.clear();
                        for (DataSnapshot child : snapshot.getChildren())
                            for (DataSnapshot grandChild : child.getChildren())
                                for (DataSnapshot greatGrandChild : grandChild.getChildren()) {
                                    CustomerAppointment saloon = greatGrandChild.getValue(CustomerAppointment.class);
                                    if (saloon == null)
                                        continue;
                                    if (saloon.getSaloonId().equals(Utils.getCurrentUserId()))
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
                = new TypeRecyclerViewAdapter(this, superList, Info.RV_TYPE_SALOON_APPOINTMENTS);
        rvAppointments.setAdapter(typeRecyclerViewAdapter);
    }

    public void back(View view) {
        finish();
    }

    private void initViews() {
        rvAppointments = findViewById(R.id.rv_saloon_appointment);
        tvNoReview = findViewById(R.id.tv_no_review);
    }
}