package com.app.beauty.activities.saloon.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.models.SaloonService;
import com.app.beauty.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SaloonEditService extends AppCompatActivity implements Info {

    EditText etTitle;
    EditText etDesc;
    EditText etCharges;
    Spinner spnCat;

    String strEtTitle;
    String strEtDesc;
    String strEtCharges;
    String strSpnCat;
    Button btnDelete;

    boolean isEdit;
    SaloonService editSaloonService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        initViews();
        btnDelete.setVisibility(View.GONE);
        isEdit = getIntent().getBooleanExtra(EXTRA_IS_FROM_SERVICE_EDIT, false);
        if (isEdit) {
            editSaloonService = SaloonServicesActivity.selectedSaloonService;
            initFields();
            btnDelete.setVisibility(View.VISIBLE);
        }

        spnCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(getColor(R.color.white));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initFields() {
        etTitle.setText(editSaloonService.getTitle());
        etDesc.setText(editSaloonService.getDescription());
        etCharges.setText(editSaloonService.getCharges());

        List<String> stringList = Arrays.asList(getResources().getStringArray(R.array.categories));
        try {
            spnCat.setSelection(stringList.indexOf(editSaloonService.getCategory()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        etCharges = findViewById(R.id.et_charges);
        etTitle = findViewById(R.id.et_title);
        spnCat = findViewById(R.id.spn_type);
        etDesc = findViewById(R.id.et_description);
        btnDelete = findViewById(R.id.remove);
    }

    public void submit(View view) {
        castStrings();
        if (!isEdit) {
            Toast.makeText(this, "Checking", Toast.LENGTH_SHORT).show();
            Utils.getReference()
                    .child(NODE_SERVICES)
                    .child(Utils.getCurrentUserId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean isRepeated = false;
                            for (DataSnapshot child : snapshot.getChildren()) {
                                SaloonService saloonService = child.getValue(SaloonService.class);
                                if (saloonService != null && saloonService.getTitle().equals(strEtTitle)) {
                                    isRepeated = true;
                                    break;
                                }
                            }
                            if (!isRepeated) {
                                Toast.makeText(SaloonEditService.this, "Updating", Toast.LENGTH_SHORT).show();
                                if (!isValidStrings())
                                    return;
                                initService();
                            } else
                                Toast.makeText(SaloonEditService.this, "Service already exists", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        } else {
            Toast.makeText(this, "Updating", Toast.LENGTH_SHORT).show();
            if (!isValidStrings())
                return;
            initService();
        }
    }

    private void initService() {
        String id = UUID.randomUUID().toString();
        if (isEdit)
            id = editSaloonService.getId();

        if (!strEtCharges.contains("Rs."))
            strEtCharges = "Rs. " + strEtCharges;

        SaloonService saloonService
                = new SaloonService(id, strEtTitle, strEtDesc, strEtCharges, strSpnCat);

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
        strSpnCat = spnCat.getSelectedItem().toString();
    }

    public void back(View view) {
        finish();
    }

    public void delete(View view) {
        FirebaseDatabase.getInstance().getReference()
                .child(NODE_SERVICES)
                .child(Utils.getCurrentUserId())
                .child(editSaloonService.getId())
                .removeValue();
        finish();
    }
}