package com.app.beauty.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.models.Saloon;
import com.app.beauty.utils.Utils;

import java.util.Objects;

public class SaloonProfileSettings extends AppCompatActivity implements Info {

    public static Saloon saloonTemp;

    EditText etSaloonName;
    EditText etManagerName;
    EditText etPhone;

    String strEtSaloonName;
    String strEtManagerName;
    String strEtPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_profile_settings);

        initViews();

        saloonTemp = new Saloon();

    }

    private void initViews() {
        etManagerName = findViewById(R.id.et_timing_to);
        etPhone = findViewById(R.id.et_phone);
        etSaloonName = findViewById(R.id.et_saloon_name);
    }

    public void back(View view) {
        finish();
    }

    public void calender(View view) {
        startActivity(new Intent(this, SaloonCalender.class));
    }

    public void submit(View view) {
        castStrings();
        if (!isValidStrings())
            return;
        initSaloon();
    }

    private void initSaloon() {
        saloonTemp.setName(strEtSaloonName);
        saloonTemp.setManagerName(strEtManagerName);
        saloonTemp.setManagerId(Utils.getCurrentUserId());
        saloonTemp.setPhone(strEtPhone);
        Utils.currentSaloon = saloonTemp;

        Utils.getReference()
                .child(NODE_SALOONS)
                .child(Utils.getCurrentUserId())
                .setValue(saloonTemp)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saloonTemp = null;
                        finish();
                    } else {
                        try {
                            Objects.requireNonNull(task.getException()).printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private boolean isValidStrings() {
        if (!Utils.validEt(etManagerName, strEtManagerName))
            return false;
        if (!Utils.validEt(etPhone, strEtPhone))
            return false;
        return Utils.validEt(etSaloonName, strEtSaloonName);
    }

    private void castStrings() {
        strEtSaloonName = etSaloonName.getText().toString();
        strEtManagerName = etManagerName.getText().toString();
        strEtPhone = etPhone.getText().toString();
    }
}