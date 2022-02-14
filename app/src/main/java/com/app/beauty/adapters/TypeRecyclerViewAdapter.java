package com.app.beauty.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.activities.CustomerAppointmentActivity;
import com.app.beauty.activities.CustomerSaloons;
import com.app.beauty.activities.CustomerServices;
import com.app.beauty.activities.SaloonEditService;
import com.app.beauty.activities.SaloonServicesActivity;
import com.app.beauty.models.Saloon;
import com.app.beauty.models.SaloonService;
import com.app.beauty.models.Super;

import java.util.ArrayList;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;


public class TypeRecyclerViewAdapter extends RecyclerView.Adapter<TypeRecyclerViewHolder> implements Info {
    Context context;
    List<Super> listInstances;
    int type;

    public TypeRecyclerViewAdapter(Context context, List<Super> listInstances, int type) {
        this.context = context;
        this.listInstances = listInstances;
        this.type = type;
    }

    @NonNull
    @Override
    public TypeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout = R.layout.rv_services;

        if (type == RV_TYPE_SALOONS)
            layout = R.layout.rv_saloons;

        if (type == RV_TYPE_CUSTOMER_SERVICES)
            layout = R.layout.rv_customer_service;

        return new TypeRecyclerViewHolder(LayoutInflater.from(context)
                .inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TypeRecyclerViewHolder holder, int position) {
        if (type == RV_TYPE_SALOONS) {
            initSaloons(holder, position);
            return;
        }

        if (type == RV_TYPE_CUSTOMER_SERVICES) {
            initCustomerServices(holder, position);
            return;
        }

        initServices(holder, position);
    }

    private void initCustomerServices(TypeRecyclerViewHolder holder, int position) {
        SaloonService saloonService = (SaloonService) listInstances.get(position);
        holder.tvTitle.setText(saloonService.getTitle());
        holder.tvDesc.setText(saloonService.getDescription());
        holder.tvCharges.setText(saloonService.getCharges());
        holder.llClick.setOnClickListener(view -> {
            CustomerServices.selectedSaloonService = saloonService;
            context.startActivity(new Intent(context, CustomerAppointmentActivity.class));
        });
    }

    private void initSaloons(TypeRecyclerViewHolder holder, int position) {
        Saloon saloon = (Saloon) listInstances.get(position);

        holder.tvSaloonName.setText(saloon.getName());
        holder.tvTimingFrom.setText(saloon.getTimingFrom());
        holder.tvTimingTo.setText(saloon.getTimingTo());

        List<MaterialDayPicker.Weekday> weekdays = new ArrayList<>();
        String workingDays = saloon.getWorkingDays();
        for (String workingDay : workingDays.split(", "))
            weekdays.add(MaterialDayPicker.Weekday.valueOf(workingDay));
        holder.mdpDayPicker.setSelectedDays(weekdays);

        holder.llClick.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                CustomerSaloons.selectedSaloon = saloon;
                context.startActivity(new Intent(context, CustomerServices.class));
            }
            return true;
        });
    }

    private void initServices(TypeRecyclerViewHolder holder, int position) {
        SaloonService saloonService = (SaloonService) listInstances.get(position);
        holder.btnEdit.setOnClickListener(view -> {
            SaloonServicesActivity.selectedSaloonService = saloonService;
            Intent intent = new Intent(context, SaloonEditService.class);
            intent.putExtra(EXTRA_IS_FROM_SERVICE_EDIT, true);
            context.startActivity(intent);
        });
        holder.tvTitle.setText(saloonService.getTitle());
        holder.tvDesc.setText(saloonService.getDescription());
        holder.tvCharges.setText(saloonService.getCharges());
    }

    @Override
    public int getItemCount() {
        return listInstances.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
