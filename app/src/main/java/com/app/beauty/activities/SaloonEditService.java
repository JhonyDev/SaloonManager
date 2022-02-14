package com.app.beauty.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.models.SaloonService;
import com.app.beauty.utils.Utils;

import java.util.Objects;
import java.util.UUID;

public class SaloonEditService extends AppCompatActivity implements Info {

    EditText etTitle;
    EditText etDesc;
    EditText etCharges;

    String strEtTitle;
    String strEtDesc;
    String strEtCharges;

    boolean isEdit;
    SaloonService editSaloonService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        initViews();

        isEdit = getIntent().getBooleanExtra(EXTRA_IS_FROM_SERVICE_EDIT, false);
        if (isEdit) {
            editSaloonService = SaloonServicesActivity.selectedSaloonService;
            initFields();
        }

    }

    private void initFields() {
        etTitle.setText(editSaloonService.getTitle());
        etDesc.setText(editSaloonService.getDescription());
        etCharges.setText(editSaloonService.getCharges());
    }

    private void initViews() {
        etCharges = findViewById(R.id.et_charges);
        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_description);
    }

    public void submit(View view) {
        castStrings();
        if (!isValidStrings())
            return;
        initService();
    }

    private void initService() {
        String id = UUID.randomUUID().toString();
        if (isEdit)
            id = editSaloonService.getId();

        SaloonService saloonService
                = new SaloonService(id, strEtTitle, strEtDesc, "Rs. " + strEtCharges);

        Utils.getReference()
                .child(NODE_SERVICES)
                .child(Utils.getCurrentUserId())
                .child(id)
                .setValue(saloonService)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (isEdit)
                            Toast.makeText(SaloonEditService.this, "Service Updated", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(SaloonEditService.this, "Service added", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        try {
                            Objects.requireNonNull(task.getException()).printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                });
    }

    private boolean isValidStrings() {
        if (!Utils.validEt(etCharges, strEtCharges))
            return false;
        if (!Utils.validEt(etDesc, strEtDesc))
            return false;
        return Utils.validEt(etTitle, strEtTitle);
    }


    private void castStrings() {
        strEtCharges = etCharges.getText().toString();
        strEtDesc = etDesc.getText().toString();
        strEtTitle = etTitle.getText().toString();
    }
}