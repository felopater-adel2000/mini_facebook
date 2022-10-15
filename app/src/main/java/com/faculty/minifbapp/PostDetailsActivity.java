package com.faculty.minifbapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetailsActivity extends AppCompatActivity {
    private TextView tvUserName, tvPostCreatedDate, tvPostBody;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        initViews();
        getIntentExtras();
        populateViews();

    }

    private void populateViews() {
        tvPostBody.setText(post.getBody());
        tvPostCreatedDate.setText(post.getCreatedDate());
        tvUserName.setText(post.getUser().getUserName());
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tv_user_name);
        tvPostCreatedDate = findViewById(R.id.tv_post_created_date);
        tvPostBody = findViewById(R.id.tv_post_body);
    }

    private void getIntentExtras() {
        post = (Post) getIntent().getSerializableExtra("post");
    }
}