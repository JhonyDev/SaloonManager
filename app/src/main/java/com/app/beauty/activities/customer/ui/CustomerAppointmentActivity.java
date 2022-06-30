package com.app.beauty.activities.customer.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.models.CustomerAppointment;
import com.app.beauty.models.Saloon;
import com.app.beauty.models.SaloonService;
import com.app.beauty.models.Staff;
import com.app.beauty.singletons.SlotsMapSingleton;
import com.app.beauty.utils.DialogUtils;
import com.app.beauty.utils.Utils;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class CustomerAppointmentActivity extends AppCompatActivity implements Info {

    public static Activity context;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    SaloonService tempSaloonService;
    Saloon tempSaloon;

    EditText etSaloonName;
    EditText etServiceTitle;
    EditText etAppointmentDate;
    EditText etCharges;
    EditText etTxId;

    String strEtSaloonName;
    String strEtServiceTitle;
    String strEtAppointmentTimeSlot;
    String strEtAppointmentDate;
    String strEtCharges;
    String strEtTxId;

    TextView tvAccount;
    Dialog loadingDialog;
    List<MaterialCardView> cvSlots;
    List<Integer> cvBookedSlots;
    int selectedSlotIndex = -1;
    boolean isDialogShown = false;
    Dialog dialog;
    Spinner spn_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_screen);
        context = this;
        tempSaloonService = CustomerServices.selectedSaloonService;
        tempSaloon = CustomerSaloons.selectedSaloon;

        initConfirmationDialog();

        initViews();
        initFields();

        initDatePicker();

        loadingDialog = new Dialog(this);
        DialogUtils.initLoadingDialog(loadingDialog);
        spn_type = findViewById(R.id.spn_type);
        initSpinner();

    }

    private void initSpinner() {
        Utils.getReference()
                .child(NODE_STAFF)
                .child(tempSaloon.getManagerId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> stringList = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Staff staff = child.getValue(Staff.class);
                            if (staff != null)
                                stringList.add(staff.getTitle());
                        }
                        initSpinnerData(stringList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void initSpinnerData(List<String> stringList) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, stringList);
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_type.setAdapter(spinnerArrayAdapter);
        spn_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(getColor(R.color.white));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initConfirmationDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.confirmation_dialog);
        dialog.setCanceledOnTouchOutside(false);
        DialogUtils.setDefaultDialogProperties(dialog);
        dialog.findViewById(R.id.btn_yes).setOnClickListener(view -> postAppointment());
        dialog.findViewById(R.id.btn_no).setOnClickListener(view -> dialog.dismiss());
    }

    private void initDatePicker() {
        date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            String date = day + "/" + month + "/" + year;
            etAppointmentDate.setText(date);
            checkSlotsAvailability();
        };
    }

    private void initFields() {
        etSaloonName.setText(tempSaloon.getName());
        etServiceTitle.setText(tempSaloonService.getTitle());
        etCharges.setText(tempSaloonService.getCharges());
        tvAccount.setText(Html.fromHtml("Send service charges using Easypaisa to,<br><b>Acc Number:</b> " +
                tempSaloon.getPhone() +
                "<br><b>Title:</b> " +
                tempSaloon.getManagerName()));
    }

    private void initViews() {
        etSaloonName = findViewById(R.id.et_saloon_name);
        etServiceTitle = findViewById(R.id.et_service_title);
        etAppointmentDate = findViewById(R.id.et_appointment_date);
        etCharges = findViewById(R.id.et_charges);
        etTxId = findViewById(R.id.et_txid);
        tvAccount = findViewById(R.id.tv_account);
        cvSlots = Utils.initCvSlotViews(this);
        cvBookedSlots = new ArrayList<>();
        for (MaterialCardView cardView : cvSlots) {
            cvBookedSlots.add(0);
            cardView.setVisibility(View.GONE);
            cardView.setOnClickListener(view -> slotClicked(cardView));
        }

    }

    private void checkSlotsAvailability() {
        /** This method will check slots availability (remove all booked slots) */
        Log.i(TAG, "checkSlotsAvailability: ");
        FirebaseDatabase.getInstance().getReference()
                .child(NODE_APPOINTMENTS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Integer> integers = Utils.getIntBinaryList(tempSaloon.getTimeSlotBinary());
                        Log.i(TAG, "onDataChange: " + integers);
                        for (int i = 0; i < integers.size(); i++) {
                            cvSlots.get(i).setVisibility(View.VISIBLE);
                            cvSlots.get(i).setClickable(true);
                            TextView nextChild = (TextView) cvSlots.get(i).getChildAt(0);
                            if (integers.get(i) == 0) {
                                nextChild.setTextColor(getColor(R.color.gray));
                                nextChild.setText("Saloon closed");
                                cvSlots.get(i).setClickable(false);
                            } else {
                                nextChild.setTextColor(getColor(R.color.white));
                                nextChild.setText(SlotsMapSingleton.getInstance().get(i));
                                cvSlots.get(i).setClickable(true);
                            }
                        }


                        for (DataSnapshot child : snapshot.getChildren())
                            for (DataSnapshot midChild : child.getChildren())
                                for (DataSnapshot grandChild : midChild.getChildren()) {
                                    try {
                                        Log.i(TAG, "onDataChange: " + grandChild);
                                        CustomerAppointment customerAppointment = grandChild.getValue(CustomerAppointment.class);
                                        if (customerAppointment == null)
                                            continue;

                                        if (!customerAppointment.getSaloonId().equals(tempSaloon.getManagerId()))
                                            continue;

                                        String date = etAppointmentDate.getText().toString();

                                        if (!customerAppointment.getAppointmentDate().equals(date))
                                            continue;

                                        if (customerAppointment.getStatus().equals(STATUS_REJECTED))
                                            continue;

                                        try {
                                            int removeTargetSlot = Integer.parseInt(customerAppointment.getSelectedTimeSlot());
                                            Log.i(TAG, "onDataChange: " + removeTargetSlot);
                                            TextView nextChild = (TextView) cvSlots.get(removeTargetSlot).getChildAt(0);
                                            nextChild.setTextColor(getColor(R.color.yellow));
                                            nextChild.setText("Already booked");
                                            cvSlots.get(removeTargetSlot).setClickable(false);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(CustomerAppointmentActivity.this, "Error during slot removal upon target index"
                                                    , Toast.LENGTH_SHORT).show();
                                            Log.i(TAG, "onDataChange: Investigation" + e.getMessage());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void back(View view) {
        finish();
    }

    public void slotClicked(View view) {
        int index = 0;
        for (MaterialCardView cardView : cvSlots) {
            if (view.equals(cardView)) {
                if (cardView.getCardBackgroundColor().getDefaultColor() == getColor(R.color.yellow)) {
                    cvBookedSlots.set(index, 0);
                    cardView.setCardBackgroundColor(getColor(R.color.light_gray));
                    TextView nextChild = (TextView) cardView.getChildAt(0);
                    nextChild.setTextColor(getColor(R.color.white));
                    selectedSlotIndex = -1;
                } else {
                    boolean isAlreadyBooked = false;
                    for (int bookedSlot : cvBookedSlots)
                        if (bookedSlot == 1) {
                            isAlreadyBooked = true;
                            break;
                        }
                    if (isAlreadyBooked | selectedSlotIndex != -1) {
                        Toast.makeText(this, "You have already booked a time slot", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cvBookedSlots.set(index, 1);
                    cardView.setCardBackgroundColor(getColor(R.color.yellow));
                    TextView nextChild = (TextView) cardView.getChildAt(0);
                    nextChild.setTextColor(getColor(R.color.black));
                    selectedSlotIndex = index;
                }
            }
            index++;
        }
    }

    public void confirm(View view) {
        castStrings();
        if (!isStringsValid())
            return;

        loadingDialog.show();
        if (isDialogShown) {
            postAppointment();
            return;
        }
        Utils.getReference()
                .child(NODE_APPOINTMENTS)
                .child(Utils.getCurrentUserId())
                .child(tempSaloon.getManagerId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingDialog.dismiss();
                        boolean isFound = false;
                        for (DataSnapshot child : snapshot.getChildren()) {
                            CustomerAppointment customerAppointment1 = child.getValue(CustomerAppointment.class);
                            if (customerAppointment1 == null)
                                continue;
                            if (customerAppointment1.getServiceTitle().equals(tempSaloonService.getTitle())) {
                                isFound = true;
                                break;
                            }
                        }
                        if (isFound) {
                            dialog.show();
                            isDialogShown = true;
                            return;
                        }
                        postAppointment();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void postAppointment() {
        dialog.dismiss();
        String id = UUID.randomUUID().toString();
        CustomerAppointment customerAppointment = new CustomerAppointment();
        customerAppointment.setSaloonId(tempSaloon.getManagerId());
        customerAppointment.setAppointmentDate(strEtAppointmentDate);
        customerAppointment.setSelectedTimeSlot(strEtAppointmentTimeSlot);
        customerAppointment.setUserId(Utils.getCurrentUserId());
        customerAppointment.setCharges(strEtCharges);
        customerAppointment.setTxid(strEtTxId);
        customerAppointment.setCustomerName(Utils.userModel.getFirstName());
        customerAppointment.setAppointmentId(id);
        customerAppointment.setStatus(STATUS_PENDING);
        customerAppointment.setSaloonName(tempSaloon.getName());
        customerAppointment.setServiceTitle(tempSaloonService.getTitle());
        customerAppointment.setRequestedStaff(spn_type.getSelectedItem().toString());
        loadingDialog.show();
        Utils.getReference()
                .child(NODE_APPOINTMENTS)
                .child(Utils.getCurrentUserId())
                .child(tempSaloon.getManagerId())
                .child(id)
                .setValue(customerAppointment)
                .addOnCompleteListener(task -> {
                    loadingDialog.dismiss();
                    if (task.isSuccessful())
                        initConfirmation();
                    else
                        Toast.makeText(CustomerAppointmentActivity.this,
                                "Cannot process request at the moment, please try again later", Toast.LENGTH_SHORT).show();
                });
    }

    private boolean isStringsValid() {
        if (!Utils.validEt(etTxId, strEtTxId))
            return false;

        if (!Utils.validEt(etAppointmentDate, strEtAppointmentDate))
            return false;

        if (selectedSlotIndex == -1) {
            Toast.makeText(this, "Please select a time slot for appointment", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void castStrings() {
        strEtSaloonName = etSaloonName.getText().toString();
        strEtServiceTitle = etServiceTitle.getText().toString();
        strEtAppointmentTimeSlot = String.valueOf(selectedSlotIndex);
        strEtAppointmentDate = etAppointmentDate.getText().toString();
        strEtCharges = etCharges.getText().toString();
        strEtTxId = etTxId.getText().toString();
    }

    private void initConfirmation() {
        Toast.makeText(this, "Appointment Submitted, please wait for confirmation", Toast.LENGTH_SHORT).show();
        CustomerSaloons.context.finish();
        CustomerServices.context.finish();
        finish();
    }

    public void showDateDialog(View view) {
        new DatePickerDialog(CustomerAppointmentActivity.this,
                date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}