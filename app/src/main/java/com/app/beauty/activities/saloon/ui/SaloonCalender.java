package com.app.beauty.activities.saloon.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.utils.Utils;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class SaloonCalender extends AppCompatActivity implements Info {

    MaterialDayPicker mdpDayPicker;

    EditText etNote;

    String strEtTimingFrom;
    String strEtTimingTo;
    String strEtNote;
    String strWorkingDays;

    List<MaterialCardView> cvSlots;
    List<Integer> cvSlotsBinary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_calender);
        cvSlots = new ArrayList<>();
        cvSlotsBinary = new ArrayList<>();
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
            etNote.setText(Utils.currentSaloon.getNote());
            List<Integer> slotBinaries = Utils.getIntBinaryList(Utils.currentSaloon.getTimeSlotBinary());
            Log.i(TAG, "initCurrentSaloon: " + slotBinaries);
            for (int i = 0; i < cvSlots.size(); i++)
                if (slotBinaries.get(i) == 1) {
                    cvSlotsBinary.set(i, 1);
                    cvSlots.get(i).setCardBackgroundColor(getColor(R.color.yellow));
                    TextView nextChild = (TextView) cvSlots.get(i).getChildAt(0);
                    nextChild.setTextColor(getColor(R.color.black));
                } else {
                    cvSlotsBinary.set(i, 0);
                    cvSlots.get(i).setCardBackgroundColor(getColor(R.color.light_gray));
                    TextView nextChild = (TextView) cvSlots.get(i).getChildAt(0);
                    nextChild.setTextColor(getColor(R.color.white));
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        mdpDayPicker = findViewById(R.id.mdp_days);
        etNote = findViewById(R.id.et_note);
        cvSlots = Utils.initCvSlotViews(this);
        for (int i = 0; i < cvSlots.size(); i++)
            cvSlotsBinary.add(0);

        for (MaterialCardView cardView : cvSlots)
            cardView.setOnClickListener(this::slotClicked);
    }

    public void back(View view) {
        finish();
    }

    public void slotClicked(View view) {
        int index = 0;
        for (MaterialCardView cardView : cvSlots) {
            if (view.equals(cardView)) {
                if (cardView.getCardBackgroundColor().getDefaultColor() == getColor(R.color.yellow)) {
                    cvSlotsBinary.set(index, 0);
                    cardView.setCardBackgroundColor(getColor(R.color.light_gray));
                    TextView nextChild = (TextView) cardView.getChildAt(0);
                    nextChild.setTextColor(getColor(R.color.white));
                } else {
                    cvSlotsBinary.set(index, 1);
                    cardView.setCardBackgroundColor(getColor(R.color.yellow));
                    TextView nextChild = (TextView) cardView.getChildAt(0);
                    nextChild.setTextColor(getColor(R.color.black));
                }
            }
            index++;
        }
    }


    public void submit(View view) {
        castStrings();
        if (!isValidStrings())
            return;
        initSaloon();
    }

    private void initSaloon() {
        String strBinary = getStringBinary();
        SaloonProfileSettings.saloonTemp.setTimingTo(strEtTimingTo);
        SaloonProfileSettings.saloonTemp.setTimingFrom(strEtTimingFrom);
        SaloonProfileSettings.saloonTemp.setNote(strEtNote);
        SaloonProfileSettings.saloonTemp.setWorkingDays(strWorkingDays);
        SaloonProfileSettings.saloonTemp.setTimeSlotBinary(strBinary);
        Toast.makeText(this, "Press submit to save changes", Toast.LENGTH_SHORT).show();
        Utils.currentSaloon = SaloonProfileSettings.saloonTemp;
        finish();
    }

    private String getStringBinary() {
        StringBuilder slotsBinary = new StringBuilder();
        for (int x : cvSlotsBinary)
            slotsBinary.append(x).append(",");
        slotsBinary.deleteCharAt(slotsBinary.length() - 1);
        return slotsBinary.toString();
    }

    private boolean isValidStrings() {
        if (strWorkingDays.isEmpty()) {
            Toast.makeText(this, "Select at least 1 working day", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void castStrings() {
        strEtNote = etNote.getText().toString();

        StringBuilder workingDays;
        workingDays = new StringBuilder();
        for (MaterialDayPicker.Weekday weekday : mdpDayPicker.getSelectedDays())
            workingDays.append(weekday.toString()).append(", ");

        strWorkingDays = workingDays.toString();
        if (!strWorkingDays.isEmpty())
            strWorkingDays = strWorkingDays.substring(0, strWorkingDays.length() - 2);

    }

}