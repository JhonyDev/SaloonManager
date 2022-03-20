package com.app.beauty.activities.authentication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.activities.dashboards.CustomerDashboard;
import com.app.beauty.activities.dashboards.SaloonManagerDashboard;
import com.app.beauty.models.UserModel;
import com.app.beauty.utils.DialogUtils;
import com.app.beauty.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * FIREBASE CONFIGURATION ACCOUNT
 * EMAIL = finalyearprojectcuiatd@gmail.com
 * PASSWORD = fypfinal
 */

public class Login extends AppCompatActivity implements Info {

    public static Activity context;
    EditText etEmail;
    EditText etPassword;
    String strEtEmail;
    String strEtPassword;
    boolean isPassVisible = false;
    String text;
    boolean isThreadRunning = false;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_pass);

        loadingDialog = new Dialog(this);
        DialogUtils.initLoadingDialog(loadingDialog);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            loadingDialog.show();
            parseUserData();
        }


    }


    private void parseUserData() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseDatabase.getInstance().getReference()
                .child(NODE_USER)
                .child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingDialog.dismiss();
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        if (userModel != null) {
                            Utils.userModel = userModel;
                            initDashCheck();
                        } else
                            Toast.makeText(Login.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void initDashCheck() {
        if (Utils.userModel.getType().equals(SALOON_MANAGER))
            startActivity(new Intent(this, SaloonManagerDashboard.class));
        else
            startActivity(new Intent(this, CustomerDashboard.class));
        finish();

    }

    public void signUp(View view) {
        startActivity(new Intent(this, Registration.class));
    }

    public void showPassword(View view) {
        if (!isPassVisible) {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isPassVisible = true;
        } else {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isPassVisible = false;
        }

    }

    public void ForgotPassword(View view) {

    }

    private void castStrings() {
        strEtEmail = "abc" + etEmail.getText().toString().replace("+", "") + "@email.com";
        strEtPassword = etPassword.getText().toString();
    }

    private boolean isEverythingValid() {
        if (!Utils.validEt(etEmail, strEtEmail))
            return false;
        return Utils.validEt(etPassword, strEtPassword);
    }

    public void Login(View view) {
        castStrings();
        if (!isEverythingValid())
            return;
        loadingDialog.show();
        initSignIn();
    }

    private void initSignIn() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(strEtEmail, strEtPassword)
                .addOnCompleteListener(task -> {
                    loadingDialog.dismiss();
                    if (task.isSuccessful()) {
                        initUserData();
                    } else
                        Objects.requireNonNull(task.getException()).printStackTrace();
                });
    }

    private void initUserData() {
        loadingDialog.show();
        FirebaseDatabase.getInstance().getReference()
                .child(NODE_USER)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingDialog.dismiss();
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        if (userModel == null)
                            return;

                        if (userModel.getType().equals(CUSTOMER))
                            startActivity(new Intent(Login.this, CustomerDashboard.class));
                        else
                            startActivity(new Intent(Login.this, SaloonManagerDashboard.class));

                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}