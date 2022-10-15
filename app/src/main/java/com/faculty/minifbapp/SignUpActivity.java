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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSignUp;
    private EditText etName, etEmail, etPassword;
    private TextView tvOpenLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progress_bar);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        tvOpenLogin = findViewById(R.id.tv_open_login);
        tvOpenLogin.setOnClickListener(this);


        btnSignUp = findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(this);
    }

    private void startSignUpProcess() {
        //Check if user name, email not empty
        //Save data into shared pref
        //open main activity
        if (TextUtils.isEmpty(etName.getText())) {
            etName.setError(getString(R.string.invalid_name));
            return;
        }
        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError(getString(R.string.invalid_email));
            return;
        }
        if (TextUtils.isEmpty(etPassword.getText()) || etPassword.getText().length() < 6) {
            etPassword.setError(getString(R.string.invalid_password));
            return;
        }
        showParent(View.GONE);
        Call<LoginSignUpResponse> call = ServiceGenerator.getMiniFBAPI().signUp(new User(etName.getText().toString(), etPassword.getText().toString(), etEmail.getText().toString()));
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
                    Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginSignUpResponse> call, Throwable t) {
                showParent(View.VISIBLE);
                t.printStackTrace();
                Toast.makeText(SignUpActivity.this, getString(R.string.smthing_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startMainActivity() {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }

    private void showParent(int visibility) {
        if (visibility == View.GONE) {
            //Hide all the views except progressbar
            progressBar.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.GONE);
            tvOpenLogin.setVisibility(View.GONE);
        } else {
            //Show all the views except progressbar
            progressBar.setVisibility(View.GONE);
            btnSignUp.setVisibility(View.VISIBLE);
            tvOpenLogin.setVisibility(View.VISIBLE);
        }
    }


    private void saveUserDataIntoCache(String email, String name, int id) {
        PreferenceManager
                .getDefaultSharedPreferences(SignUpActivity.this)
                .edit()
                .putString("email", email)
                .putString("name", name)
                .putInt("id", id)
                .apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_open_login:
                Intent signUpIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(signUpIntent);
                break;
            case R.id.btn_sign_up:
                startSignUpProcess();
                break;
        }
    }
}