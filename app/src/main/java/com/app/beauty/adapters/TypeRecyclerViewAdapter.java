package com.app.beauty.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.activities.ReviewPost;
import com.app.beauty.activities.customer.ui.CustomerAppointmentActivity;
import com.app.beauty.activities.customer.ui.CustomerSaloons;
import com.app.beauty.activities.customer.ui.CustomerServices;
import com.app.beauty.activities.saloon.ui.AddStaff;
import com.app.beauty.activities.saloon.ui.SaloonAppointmentsActivity;
import com.app.beauty.activities.saloon.ui.SaloonEditService;
import com.app.beauty.activities.saloon.ui.SaloonServicesActivity;
import com.app.beauty.models.CustomerAppointment;
import com.app.beauty.models.CustomerReview;
import com.app.beauty.models.Saloon;
import com.app.beauty.models.SaloonService;
import com.app.beauty.models.Staff;
import com.app.beauty.models.Super;
import com.app.beauty.singletons.SlotsMapSingleton;
import com.app.beauty.singletons.StaffSingleton;
import com.app.beauty.utils.Utils;

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

        if (type == RV_TYPE_CUSTOMER_REVIEWS)
            layout = R.layout.rv_reviews;

        if (type == RV_TYPE_SALOON_APPOINTMENTS)
            layout = R.layout.rv_saloon_appointment;

        if (type == RV_TYPE_SALOONS)
            layout = R.layout.rv_saloons;

        if (type == RV_TYPE_CUSTOMER_SERVICES)
            layout = R.layout.rv_customer_service;

        if (type == RV_TYPE_CUSTOMER_APPOINTMENTS)
            layout = R.layout.rv_appointment;

        return new TypeRecyclerViewHolder(LayoutInflater.from(context)
                .inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TypeRecyclerViewHolder holder, int position) {
        if (type == RV_TYPE_SALOON_APPOINTMENTS) {
            initSaloonAppointments(holder, position);
            return;
        }

        if (type == RV_TYPE_CUSTOMER_REVIEWS) {
            initCustomerReviews(holder, position);
            return;
        }

        if (type == RV_TYPE_SALOONS) {
            initSaloons(holder, position);
            return;
        }

        if (type == RV_TYPE_CUSTOMER_SERVICES) {
            initCustomerServices(holder, position);
            return;
        }

        if (type == RV_TYPE_CUSTOMER_APPOINTMENTS) {
            initCustomerAppointment(holder, position);
            return;
        }

        initServices(holder, position);
    }

    private void initSaloonAppointments(TypeRecyclerViewHolder holder, int position) {
        CustomerAppointment customerAppointment = (CustomerAppointment) listInstances.get(position);
        holder.tvCustomerName.setText(customerAppointment.getCustomerName());
        holder.tvTitle.setText(customerAppointment.getServiceTitle());
        holder.tvStatus.setText(customerAppointment.getStatus());
        try {
            holder.tvTime.setText(SlotsMapSingleton.getInstance().get(Integer.parseInt(customerAppointment.getSelectedTimeSlot())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tvTxId.setText(customerAppointment.getTxid());
        holder.tvDate.setText(customerAppointment.getAppointmentDate());
        holder.tvCharges.setText(customerAppointment.getCharges());
        try {
            holder.tv_staff.setText(customerAppointment.getRequestedStaff());
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (customerAppointment.getStatus()) {
            case STATUS_PENDING:
                holder.btnConfirm.setVisibility(View.VISIBLE);
                holder.btnReject.setVisibility(View.VISIBLE);
                holder.btnRate.setVisibility(View.GONE);
                holder.btnCompleted.setVisibility(View.GONE);
                break;
            case STATUS_COMPLETED:
                holder.tvStatus.setTextColor(context.getColor(R.color.blue));
                holder.btnRate.setVisibility(View.VISIBLE);
                holder.btnConfirm.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.btnCompleted.setVisibility(View.GONE);
                break;
            case STATUS_REJECTED:
                holder.tvStatus.setTextColor(context.getColor(R.color.red));
                holder.btnConfirm.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.btnRate.setVisibility(View.GONE);
                holder.btnCompleted.setVisibility(View.GONE);
                break;
            case STATUS_CONFIRMED:
                holder.btnCompleted.setVisibility(View.VISIBLE);
                holder.btnConfirm.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.btnRate.setVisibility(View.GONE);
                holder.tvStatus.setTextColor(context.getColor(R.color.green));
                break;
        }

        holder.btnConfirm.setOnClickListener(view -> {
            customerAppointment.setStatus(STATUS_CONFIRMED);
            updateAppointment(customerAppointment);
        });

        holder.btnReject.setOnClickListener(view -> {
            customerAppointment.setStatus(STATUS_REJECTED);
            updateAppointment(customerAppointment);
        });

        holder.btnCompleted.setOnClickListener(view -> {
            customerAppointment.setStatus(STATUS_COMPLETED);
            updateAppointment(customerAppointment);
        });

        holder.btnRate.setOnClickListener(view -> {
            SaloonAppointmentsActivity.selectedAppointment = customerAppointment;
            Intent intent = new Intent(context, ReviewPost.class);
            intent.putExtra(EXTRA_IS_FROM_SALOON, true);
            context.startActivity(intent);
        });

    }

    private void updateAppointment(CustomerAppointment customerAppointment) {
        Utils.getReference()
                .child(NODE_APPOINTMENTS)
                .child(customerAppointment.getUserId())
                .child(customerAppointment.getSaloonId())
                .child(customerAppointment.getAppointmentId())
                .setValue(customerAppointment);
    }

    private void initCustomerReviews(TypeRecyclerViewHolder holder, int position) {
        CustomerReview customerReview = (CustomerReview) listInstances.get(position);
        if (customerReview.getSaloonId().equals(Utils.getCurrentUserId()))
            holder.tvSaloonName.setText(customerReview.getCustomerName());
        else
            holder.tvSaloonName.setText(customerReview.getSaloonName());

        holder.etReview.setText(customerReview.getReview());
        holder.rbRating.setRating(Float.parseFloat(customerReview.getRating()));
    }

    private void initCustomerAppointment(TypeRecyclerViewHolder holder, int position) {
        CustomerAppointment customerAppointment = (CustomerAppointment) listInstances.get(position);
        holder.tvTitle.setText(customerAppointment.getServiceTitle());
        holder.tvCharges.setText(customerAppointment.getCharges());
        holder.tvDate.setText(customerAppointment.getAppointmentDate());
        try {
            holder.tvTime.setText(SlotsMapSingleton.getInstance().get(Integer.parseInt(customerAppointment.getSelectedTimeSlot())));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "initCustomerAppointment: " + customerAppointment.getSelectedTimeSlot());
        }
        holder.tvSaloonName.setText(customerAppointment.getSaloonName());
        holder.tvStatus.setText(customerAppointment.getStatus());
        if (customerAppointment.getStatus().equals(STATUS_REJECTED))
            holder.tvStatus.setTextColor(context.getColor(R.color.red));

        if (customerAppointment.getStatus().equals(STATUS_CONFIRMED))
            holder.tvStatus.setTextColor(context.getColor(R.color.green));

        if (customerAppointment.getStatus().equals(STATUS_COMPLETED))
            holder.tvStatus.setTextColor(context.getColor(R.color.blue));

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
        if (type == RV_TYPE_STAFF_LIST) {
            Staff staff = (Staff) listInstances.get(position);
            holder.btnEdit.setOnClickListener(view -> {
                StaffSingleton.setStaff(staff);
                Intent intent = new Intent(context, AddStaff.class);
                context.startActivity(intent);
            });
            holder.tvTitle.setText(staff.getTitle());
            holder.tvDesc.setText(staff.getDescription());
            holder.tvCharges.setVisibility(View.GONE);
            return;
        }
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
