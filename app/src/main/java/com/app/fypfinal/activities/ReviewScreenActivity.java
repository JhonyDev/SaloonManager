package com.app.fypfinal.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app.fypfinal.R;
import com.app.fypfinal.utils.Utils;

public class ReviewScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_screen);

    }

    public void back(View view) {
        finish();
    }

    public void submit(View view) {

    }
}