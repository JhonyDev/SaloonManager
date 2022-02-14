package com.app.beauty.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.models.CustomerAppointment;
import com.app.beauty.models.Saloon;
import com.app.beauty.models.SaloonService;
import com.app.beauty.utils.DialogUtils;
import com.app.beauty.utils.Utils;

import java.util.Calendar;
import java.util.UUID;

public class CustomerAppointmentActivity extends AppCompatActivity implements Info {

    public static Activity context;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    SaloonService tempSaloonService;
    Saloon tempSaloon;

    EditText etSaloonName;
    EditText etServiceTitle;
    EditText etAppointmentTime;
    EditText etAppointmentDate;
    EditText etCharges;
    EditText etTxId;

    String strEtSaloonName;
    String strEtServiceTitle;
    String strEtAppointmentTime;
    String strEtAppointmentDate;
    String strEtCharges;
    String strEtTxId;

    TextView tvAccount;
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_screen);
        context = this;
        tempSaloonService = CustomerServices.selectedSaloonService;
        tempSaloon = CustomerSaloons.selectedSaloon;

        initViews();
        initFields();

        initDatePicker();

        loadingDialog = new Dialog(this);
        DialogUtils.initLoadingDialog(loadingDialog);

    }

    private void initDatePicker() {
        date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            String date = day + "/" + month + "/" + year;
            etAppointmentDate.setText(date);
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
        etAppointmentTime = findViewById(R.id.et_timing_of_appointment);
        etAppointmentDate = findViewById(R.id.et_appointment_date);
        etCharges = findViewById(R.id.et_charges);
        etTxId = findViewById(R.id.et_txid);
        tvAccount = findViewById(R.id.tv_account);
    }

    public void back(View view) {
        finish();
    }

    public void confirm(View view) {
        castStrings();
        if (!isStringsValid())
            return;

        String id = UUID.randomUUID().toString();
        CustomerAppointment customerAppointment = new CustomerAppointment();
        customerAppointment.setSaloonId(tempSaloon.getManagerId());
        customerAppointment.setAppointmentDate(strEtAppointmentDate);
        customerAppointment.setAppointmentTime(strEtAppointmentTime);
        customerAppointment.setUserId(Utils.getCurrentUserId());
        customerAppointment.setCharges(strEtCharges);
        customerAppointment.setTxid(strEtTxId);
        customerAppointment.setTxid(Utils.userModel.getFirstName());
        customerAppointment.setAppointmentId(id);
        customerAppointment.setStatus(STATUS_PENDING);
        customerAppointment.setSaloonName(tempSaloon.getName());
        customerAppointment.setServiceTitle(tempSaloonService.getTitle());

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
        if (!Utils.validEt(etAppointmentDate, strEtAppointmentDate))
            return false;
        return Utils.validEt(etAppointmentTime, strEtAppointmentTime);
    }

    private void castStrings() {
        strEtSaloonName = etSaloonName.getText().toString();
        strEtServiceTitle = etServiceTitle.getText().toString();
        strEtAppointmentTime = etAppointmentTime.getText().toString();
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

    public void showTimeDialog(View view) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CustomerAppointmentActivity.this, (timePicker, selectedHour, selectedMinute) -> {
            boolean isPM = selectedHour > 12;
            String timeString;
            if (isPM) {
                selectedHour -= 12;
                if (selectedMinute < 10)
                    timeString = selectedHour + ":0" + selectedMinute + " PM";
                else
                    timeString = selectedHour + ":" + selectedMinute + " PM";
            } else {
                if (selectedMinute < 10)
                    timeString = selectedHour + ":0" + selectedMinute + " AM";
                else
                    timeString = selectedHour + ":" + selectedMinute + " AM";
            }
            etAppointmentTime.setText(timeString);
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}