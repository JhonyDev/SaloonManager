package com.app.beauty.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class SaloonCalender extends AppCompatActivity implements Info {

    MaterialDayPicker mdpDayPicker;

    EditText etTimingFrom;
    EditText etTimingTo;
    EditText etNote;

    String strEtTimingFrom;
    String strEtTimingTo;
    String strEtNote;
    String strWorkingDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_calender);

        initViews();
        initCurrentSaloon();

    }

    private void initCurrentSaloon() {
        if (Utils.currentSaloon == null)
            return;
        try {
            List<MaterialDayPicker.Weekday> weekdays = new ArrayList<>();
            String workingDays = Utils.currentSaloon.getWorkingDays();
            for (String workingDay : workingDays.split(", "))
                weekdays.add(MaterialDayPicker.Weekday.valueOf(workingDay));
            mdpDayPicker.setSelectedDays(weekdays);
            etTimingTo.setText(Utils.currentSaloon.getTimingTo());
            etTimingFrom.setText(Utils.currentSaloon.getTimingFrom());
            etNote.setText(Utils.currentSaloon.getNote());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        mdpDayPicker = findViewById(R.id.mdp_days);
        etTimingFrom = findViewById(R.id.et_time_from);
        etTimingTo = findViewById(R.id.et_timing_to);
        etNote = findViewById(R.id.et_note);
    }

    public void back(View view) {
        finish();
    }


    public void showFromTimingsDialog(View view) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(SaloonCalender.this, (timePicker, selectedHour, selectedMinute) -> {
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
            etTimingFrom.setText(timeString);
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void submit(View view) {
        castStrings();
        if (!isValidStrings())
            return;
        initSaloon();
    }

    private void initSaloon() {
        SaloonProfileSettings.saloonTemp.setTimingTo(strEtTimingTo);
        SaloonProfileSettings.saloonTemp.setTimingFrom(strEtTimingFrom);
        SaloonProfileSettings.saloonTemp.setNote(strEtNote);
        SaloonProfileSettings.saloonTemp.setWorkingDays(strWorkingDays);
        Toast.makeText(this, "Press submit to save changes", Toast.LENGTH_SHORT).show();
        Utils.currentSaloon = SaloonProfileSettings.saloonTemp;
        finish();
    }

    private boolean isValidStrings() {
        if (!Utils.validEt(etTimingFrom, strEtTimingFrom))
            return false;

        if (strWorkingDays.isEmpty()) {
            Toast.makeText(this, "Select at least 1 working day", Toast.LENGTH_SHORT).show();
            return false;
        }

        return Utils.validEt(etTimingTo, strEtTimingTo);
    }


    private void castStrings() {
        strEtNote = etNote.getText().toString();
        strEtTimingFrom = etTimingFrom.getText().toString();
        strEtTimingTo = etTimingTo.getText().toString();
        StringBuilder workingDays;
        workingDays = new StringBuilder();
        for (MaterialDayPicker.Weekday weekday : mdpDayPicker.getSelectedDays())
            workingDays.append(weekday.toString()).append(", ");

        strWorkingDays = workingDays.toString();
        if (!strWorkingDays.isEmpty())
            strWorkingDays = strWorkingDays.substring(0, strWorkingDays.length() - 2);

    }

    public void showToTiming(View view) {
        etTimingTo.setOnClickListener(v -> {
            Calendar mCurrentTime = Calendar.getInstance();
            int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mCurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(SaloonCalender.this, (timePicker, selectedHour, selectedMinute) -> {
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
                etTimingTo.setText(timeString);
            }, hour, minute, false);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        });
    }
}