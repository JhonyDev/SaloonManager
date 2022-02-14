package com.app.beauty.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.models.CustomerAppointment;
import com.app.beauty.models.CustomerReview;
import com.app.beauty.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReviewPost extends AppCompatActivity implements Info {

    EditText etReviewText;
    String strEtReviewText;
    RatingBar rbRating;
    String strRbRating;
    CustomerReview currentCustomerReview;
    CustomerAppointment tempCustomerAppointment;
    boolean isFromSaloon = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_screen);
        initViews();
        isFromSaloon = getIntent().getBooleanExtra(EXTRA_IS_FROM_SALOON, false);

        if (isFromSaloon) {
            tempCustomerAppointment = SaloonAppointmentsActivity.selectedAppointment;
        }

        initPrevReview();
    }

    private void initPrevReview() {
        DatabaseReference dbRef;
        if (isFromSaloon)
            dbRef = Utils.getReference()
                    .child(NODE_REVIEWS)
                    .child(tempCustomerAppointment.getUserId())
                    .child(Utils.getCurrentUserId());
        else
            dbRef = Utils.getReference()
                    .child(NODE_REVIEWS)
                    .child(CustomerSaloons.selectedSaloon.getManagerId())
                    .child(Utils.getCurrentUserId());


        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentCustomerReview = snapshot.getValue(CustomerReview.class);
                initFields();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initFields() {
        if (currentCustomerReview == null)
            return;
        etReviewText.setText(currentCustomerReview.getReview());
        rbRating.setRating(Float.parseFloat(currentCustomerReview.getRating()));
        Toast.makeText(this, "Review is already posted", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        etReviewText = findViewById(R.id.et_review_text);
        rbRating = findViewById(R.id.rb_rating);
    }

    public void back(View view) {
        finish();
    }

    public void submit(View view) {
        castStrings();
        if (!isValidStrings())
            return;
        initReviewPost();
    }

    private void initReviewPost() {
        CustomerReview customerReview = new CustomerReview();
        if (isFromSaloon) {
            customerReview.setUserId(tempCustomerAppointment.getUserId());
            customerReview.setSaloonName(tempCustomerAppointment.getSaloonName());
            customerReview.setCustomerName(tempCustomerAppointment.getCustomerName());
            customerReview.setSaloonId(tempCustomerAppointment.getSaloonId());
        } else {
            customerReview.setUserId(Utils.getCurrentUserId());
            customerReview.setSaloonId(CustomerSaloons.selectedSaloon.getManagerId());
            customerReview.setCustomerName(Utils.userModel.getFirstName());
            customerReview.setSaloonName(CustomerSaloons.selectedSaloon.getName());
        }
        customerReview.setReview(strEtReviewText);
        customerReview.setRating(strRbRating);

        DatabaseReference dbRef;

        if (isFromSaloon)
            dbRef = Utils.getReference()
                    .child(NODE_REVIEWS)
                    .child(tempCustomerAppointment.getUserId())
                    .child(Utils.getCurrentUserId());
        else
            dbRef = Utils.getReference()
                    .child(NODE_REVIEWS)
                    .child(CustomerSaloons.selectedSaloon.getManagerId())
                    .child(Utils.getCurrentUserId());

        dbRef.setValue(customerReview)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ReviewPost.this, "Review Posted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }

    private boolean isValidStrings() {
        return Utils.validEt(etReviewText, strEtReviewText);
    }

    private void castStrings() {
        strEtReviewText = etReviewText.getText().toString();
        strRbRating = String.valueOf(rbRating.getRating());
    }
}