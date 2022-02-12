package com.app.beauty.utils;

import android.widget.EditText;

import com.app.beauty.models.Saloon;
import com.app.beauty.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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


    public static String getCurrentUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return "no_id_found";
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
