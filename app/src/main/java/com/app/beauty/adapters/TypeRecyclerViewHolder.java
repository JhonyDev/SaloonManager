package com.app.beauty.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
    TextView tvStatus;
    TextView tvDate;
    TextView tvTime;
    TextView tvCustomerName;

    TextView tvSaloonName;
    TextView tvTimingFrom;
    TextView tvTimingTo;
    TextView tvTxId;

    RatingBar rbRating;
    EditText etReview;

    Button btnEdit;
    Button btnReject;
    Button btnConfirm;
    Button btnRate;
    LinearLayout llClick;

    public TypeRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        tvCharges = itemView.findViewById(R.id.tv_charges);
        tvDesc = itemView.findViewById(R.id.tv_desc);
        tvTitle = itemView.findViewById(R.id.tv_title);
        btnEdit = itemView.findViewById(R.id.btn_edit);
        btnConfirm = itemView.findViewById(R.id.btn_confirm);
        btnReject = itemView.findViewById(R.id.btn_reject);
        btnRate = itemView.findViewById(R.id.btn_rate);

        tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
        mdpDayPicker = itemView.findViewById(R.id.mdp_days);
        tvSaloonName = itemView.findViewById(R.id.tv_saloon_name);
        tvTimingFrom = itemView.findViewById(R.id.tv_timing_from);
        tvTimingTo = itemView.findViewById(R.id.tv_timing_to);
        tvTxId = itemView.findViewById(R.id.tv_txid);
        llClick = itemView.findViewById(R.id.ll_click);

        tvStatus = itemView.findViewById(R.id.tv_status);
        tvDate = itemView.findViewById(R.id.tv_date);
        tvTime = itemView.findViewById(R.id.tv_time);

        etReview = itemView.findViewById(R.id.et_review);
        rbRating = itemView.findViewById(R.id.rb_rating);
    }

}


