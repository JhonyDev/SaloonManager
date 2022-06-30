package com.app.beauty.utils;

import android.app.Activity;
import android.widget.EditText;

import com.app.beauty.models.Saloon;
import com.app.beauty.models.UserModel;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class Utils {
    public static UserModel userModel;
    public static Saloon currentSaloon;

    public static boolean validEt(EditText etUserName, String strEtUserName) {
        if (strEtUserName.isEmpty()) {
            etUserName.setError("Field Empty");
            return false;
        }
        return true;
    }

    public static DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static List<Integer> getIntBinaryList(String string) {
        String[] strings = string.split(",");
        List<Integer> integers = new ArrayList<>();
        for (String str : strings)
            integers.add(Integer.parseInt(str));
        return integers;
    }

    public static String getCurrentUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return "no_id_found";
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public static List<MaterialCardView> initCvSlotViews(Activity context) {
        List<MaterialCardView> cvSlots = new ArrayList<>();
        for (int i = 5; ; i++)
            try {
                MaterialCardView materialCardView = context.findViewById(context.getResources()
                        .getIdentifier("cv_" + i, "id", context.getPackageName()));
                if (materialCardView == null)
                    break;
                cvSlots.add(materialCardView);
            } catch (Exception e) {
                break;
            }
        return cvSlots;
    }
}
