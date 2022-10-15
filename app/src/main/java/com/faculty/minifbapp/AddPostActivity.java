package com.faculty.minifbapp;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etPostBody;
    private Button btnPost;
    private int id;
    private String name;
    private Post post;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        initViews();
        getUserDetailsFromCache();

    }

    private void getUserDetailsFromCache() {
        id = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getInt("id", 0);
        name = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString("name", "");
    }


    private void initViews() {
        progressBar = findViewById(R.id.progress_bar);
        etPostBody = findViewById(R.id.et_post_body);
        btnPost = findViewById(R.id.btn_post);
        btnPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_post)
            startPostingProcess();
    }

    private void startPostingProcess() {
        String s = null;
        String f = "";
        if (TextUtils.isEmpty(etPostBody.getText()))
            etPostBody.setError(getString(R.string.empty_post_body));
        else {
            //Add the post
            showParent(View.GONE);
            Call<Post> call = ServiceGenerator.getMiniFBAPI().addNewPost(new Post(etPostBody.getText().toString(), new User(id)));
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    showParent(View.VISIBLE);
                    if (response.isSuccessful()) {
                        //Assign added post returned from REST API to our variable post
                        post = response.body();
                        //Send the posts to our adapter then assign the adapter to our recyclerview
                        addPostSuccessfully();
                    } else {
                        Toast.makeText(AddPostActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    showParent(View.VISIBLE);
                    t.printStackTrace();
                    Toast.makeText(AddPostActivity.this, getString(R.string.smthing_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showParent(int visibility) {
        if (visibility == View.GONE) {
            //Hide all the views except progressbar
            progressBar.setVisibility(View.VISIBLE);
            btnPost.setVisibility(View.GONE);
        } else {
            //Show all the views except progressbar
            progressBar.setVisibility(View.GONE);
            btnPost.setVisibility(View.VISIBLE);
        }
    }

    private void addPostSuccessfully() {
        post.setUser(new User(name));
        Intent intent = new Intent();
        intent.putExtra("post", post);
        setResult(RESULT_OK, intent);
        finish();
    }
}