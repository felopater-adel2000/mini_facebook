package com.faculty.minifbapp;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private EditText etEmail, etPassword;
    private TextView tvOpenSignUp;
    private String email = "mmoniemfahmy@gmail.com", password = "123456";
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progress_bar);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        tvOpenSignUp = findViewById(R.id.tv_open_sign_up);
        tvOpenSignUp.setOnClickListener(this);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //Start login process
                startLoginProcess();
                break;
            case R.id.tv_open_sign_up:
                //Open Sign Up Activity
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
        }
    }

    private void startLoginProcess() {
        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError(getString(R.string.invalid_email));
            return;
        }
        if (TextUtils.isEmpty(etPassword.getText()) || etPassword.getText().length() < 6) {
            etPassword.setError(getString(R.string.invalid_password));
            return;
        }

        showParent(View.GONE);
        Call<LoginSignUpResponse> call = ServiceGenerator.getMiniFBAPI().login(new User(etPassword.getText().toString(), etEmail.getText().toString()));
        call.enqueue(new Callback<LoginSignUpResponse>() {
            @Override
            public void onResponse(Call<LoginSignUpResponse> call, Response<LoginSignUpResponse> response) {
                showParent(View.VISIBLE);
                if (response.isSuccessful() && response.body() != null && response.body().getUser() != null) {
                    //Save data of user into our cache & open main activity
                    saveUserDataIntoCache(response.body().getUser().getEmail(), response.body().getUser().getUserName(),
                            response.body().getUser().getId());
                    startMainActivity();
                } else {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginSignUpResponse> call, Throwable t) {
                showParent(View.VISIBLE);
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, getString(R.string.smthing_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showParent(int visibility) {
        if (visibility == View.GONE) {
            //Hide all the views except progressbar
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            tvOpenSignUp.setVisibility(View.GONE);
        } else {
            //Show all the views except progressbar
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            tvOpenSignUp.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (isUserLoggedIn()) {
            startMainActivity();
        }
    }

    private boolean isUserLoggedIn() {
        if (PreferenceManager.getDefaultSharedPreferences(this).getString("email", "").equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void saveUserDataIntoCache(String email, String name, int userId) {
        PreferenceManager
                .getDefaultSharedPreferences(LoginActivity.this)
                .edit()
                .putString("email", email)
                .putString("name", name)
                .putInt("id", userId)
                .apply();
    }
}