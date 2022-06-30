package com.app.beauty.activities.saloon.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.models.SaloonService;
import com.app.beauty.models.Staff;
import com.app.beauty.singletons.StaffSingleton;
import com.app.beauty.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.UUID;

public class AddStaff extends AppCompatActivity implements Info {

    EditText etTitle;
    EditText etDesc;

    String strEtTitle;
    String strEtDesc;

    Button btnDelete;

    boolean isEdit;
    Staff editStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        initViews();
        btnDelete.setVisibility(View.GONE);
        isEdit = StaffSingleton.getStaff() != null;
        if (isEdit) {
            editStaff = StaffSingleton.getStaff();
            initFields();
            btnDelete.setVisibility(View.VISIBLE);
        }

    }

    private void initFields() {
        etTitle.setText(editStaff.getTitle());
        etDesc.setText(editStaff.getDescription());
    }

    private void initViews() {
        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_description);
        btnDelete = findViewById(R.id.remove);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StaffSingleton.setStaff(null);
    }

    public void submit(View view) {
        castStrings();
        if (!isEdit) {
            Utils.getReference()
                    .child(NODE_STAFF)
                    .child(Utils.getCurrentUserId())
                    .addValueEventListener(new ValueEventListener() {
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
                                Toast.makeText(AddStaff.this, "Updating", Toast.LENGTH_SHORT).show();
                                if (!isValidStrings())
                                    return;
                                initService();
                            } else
                                Toast.makeText(AddStaff.this, "Staff already exists", Toast.LENGTH_SHORT).show();

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
            id = editStaff.getId();

        Staff staff = new Staff(id, strEtTitle, strEtDesc);

        Utils.getReference()
                .child(NODE_STAFF)
                .child(Utils.getCurrentUserId())
                .child(id)
                .setValue(staff)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (isEdit)
                            Toast.makeText(AddStaff.this, "Staff Updated", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(AddStaff.this, "Staff added", Toast.LENGTH_SHORT).show();
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
        if (!Utils.validEt(etDesc, strEtDesc))
            return false;
        return Utils.validEt(etTitle, strEtTitle);
    }


    private void castStrings() {
        strEtDesc = etDesc.getText().toString();
        strEtTitle = etTitle.getText().toString();
    }

    public void back(View view) {
        finish();
    }

    public void delete(View view) {
        FirebaseDatabase.getInstance().getReference()
                .child(NODE_STAFF)
                .child(Utils.getCurrentUserId())
                .child(editStaff.getId())
                .removeValue();
        finish();
    }
}