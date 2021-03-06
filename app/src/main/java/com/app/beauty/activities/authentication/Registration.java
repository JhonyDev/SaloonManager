package com.app.beauty.activities.authentication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.beauty.Info.Info;
import com.app.beauty.R;
import com.app.beauty.models.UserModel;
import com.app.beauty.utils.DialogUtils;
import com.app.beauty.utils.Utils;
import com.hbb20.CountryCodePicker;

public class Registration extends AppCompatActivity implements Info {

    public static String verId;
    public static UserModel userModel;
    public static Activity context;
    public static String strEtPassword;
    boolean isPassVisible = false;
    EditText etUserName;
    EditText etPhone;
    EditText etPassword;
    EditText etConfirmPassword;
    EditText etFirstName;
    EditText etLastName;
    String strEtFirstName;
    String strEtLastName;
    String strEtUserName;
    String strEtEmail;
    String strEtPhone;
    String strEtConfirmPassword;

    CountryCodePicker cpp;

    RadioButton rbSalonManager;
    RadioButton rbCustomer;
    Dialog dgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = this;
        initViews();
        dgLoading = new Dialog(this);
        DialogUtils.initLoadingDialog(dgLoading);
    }

    public void showPassword(View view) {
        if (!isPassVisible) {
            etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isPassVisible = true;
        } else {
            etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isPassVisible = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void castStrings() {
        strEtFirstName = etFirstName.getText().toString();
        strEtLastName = etLastName.getText().toString();
        strEtUserName = etUserName.getText().toString();
        strEtPhone = etPhone.getText().toString();
        strEtPassword = etPassword.getText().toString();
        strEtConfirmPassword = etConfirmPassword.getText().toString();
    }

    private void initViews() {
        etUserName = findViewById(R.id.et_user_name);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_pass);
        etConfirmPassword = findViewById(R.id.et_confirm_pass);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        cpp = findViewById(R.id.ccp);
        rbSalonManager = findViewById(R.id.rb_salon_manager);
        rbCustomer = findViewById(R.id.rb_customer);
        rbCustomer.setChecked(true);
    }

    public void back(View view) {
        finish();
    }

    public void SignUpWithPhone(View view) {
        castStrings();
        if (!Utils.validEt(etFirstName, strEtFirstName))
            return;

        if (!Utils.validEt(etLastName, strEtLastName))
            return;

        if (!Utils.validEt(etUserName, strEtUserName))
            return;

        if (!Utils.validEt(etPhone, strEtPhone))
            return;

        if (!strEtPassword.equals(strEtConfirmPassword))
            return;

        if (strEtPassword.length() < 6) {
            etPassword.setError("Password must be greater than 6 characters");
            Toast.makeText(this, "Password must be greater than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }


        strEtEmail = "abc" + cpp.getSelectedCountryCode() + strEtPhone + "@email.com";
        String type = CUSTOMER;
        if (rbSalonManager.isChecked())
            type = SALOON_MANAGER;

        Registration.userModel = new UserModel(strEtFirstName, strEtLastName, strEtUserName,
                strEtEmail,
                "+" + cpp.getSelectedCountryCode() + strEtPhone,
                type);

        startActivity(new Intent(this, PostVerificationCode.class));
    }


}