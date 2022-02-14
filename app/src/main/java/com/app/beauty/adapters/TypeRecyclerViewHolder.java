package com.app.beauty.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.beauty.R;

import ca.antonious.materialdaypicker.MaterialDayPicker;


public class TypeRecyclerViewHolder extends RecyclerView.ViewHolder {
    MaterialDayPicker mdpDayPicker;

    TextView tvTitle;
    TextView tvDesc;
    TextView tvCharges;

    TextView tvSaloonName;
    TextView tvTimingFrom;
    TextView tvTimingTo;

    Button btnEdit;
    LinearLayout llClick;

    public TypeRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        tvCharges = itemView.findViewById(R.id.tv_charges);
        tvDesc = itemView.findViewById(R.id.tv_desc);
        tvTitle = itemView.findViewById(R.id.tv_title);
        btnEdit = itemView.findViewById(R.id.btn_edit);

        mdpDayPicker = itemView.findViewById(R.id.mdp_days);
        tvSaloonName = itemView.findViewById(R.id.tv_saloon_name);
        tvTimingFrom = itemView.findViewById(R.id.tv_timing_from);
        tvTimingTo = itemView.findViewById(R.id.tv_timing_to);
        llClick = itemView.findViewById(R.id.ll_click);
    }

}


