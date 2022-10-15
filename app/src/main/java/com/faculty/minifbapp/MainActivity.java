package com.faculty.minifbapp;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ADD_POST_CODE = 2;
    private TextView tvWelcome;
    private String email, name;
    private Button btnLogout;
    private RecyclerView rvPosts;
    private List<Post> posts = new ArrayList<>();
    private PostsAdapter postsAdapter;
    private FloatingActionButton fabAddNewPost;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        getUserDetailsFromCache();
        getAllPosts();
    }

    private void getAllPosts() {
        //Fetch all posts from our REST API
        showParent(View.GONE);
        Call<List<Post>> call = ServiceGenerator.getMiniFBAPI().getAllPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                showParent(View.VISIBLE);
                if (response.isSuccessful()) {
                    //Assign posts returned from REST API to our variable posts
                    posts = response.body();
                    //Send the posts to our adapter then assign the adapter to our recyclerview
                    populateViews();
                } else {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                showParent(View.VISIBLE);
                t.printStackTrace();
                Toast.makeText(MainActivity.this, getString(R.string.smthing_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateViews() {
        tvWelcome.setText(String.format(Locale.getDefault(), "%s %s", "Welcome", name));
        postsAdapter.setPosts(posts);
        rvPosts.setAdapter(postsAdapter);
    }

    private void getUserDetailsFromCache() {
        email = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString("email", "");
        name = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString("name", "");
    }

    private void initViews() {
        progressBar = findViewById(R.id.progress_bar);
        tvWelcome = findViewById(R.id.tv_welcome);
        fabAddNewPost = findViewById(R.id.fb_add_post);
        fabAddNewPost.setOnClickListener(this);
        rvPosts = findViewById(R.id.rv_posts);
        postsAdapter = new PostsAdapter(posts);
    }

    private void openLoginActivity() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    private void clearCache() {
        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().clear().apply();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fb_add_post) {
            startActivityForResult(new Intent(MainActivity.this, AddPostActivity.class), ADD_POST_CODE);
        }
    }


    //Inflation of menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //Click listener of menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            //Clear Shared Prefs
            // Open LoginActivity
            //Finish MainActivity
            clearCache();
            openLoginActivity();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == ADD_POST_CODE) {
            posts.add((Post) intent.getSerializableExtra("post"));
            postsAdapter.setPosts(posts);
        }
    }


    private void showParent(int visibility) {
        if (visibility == View.GONE) {
            //Hide all the views except progressbar
            progressBar.setVisibility(View.VISIBLE);
            rvPosts.setVisibility(View.GONE);
            fabAddNewPost.setVisibility(View.GONE);
            tvWelcome.setVisibility(View.GONE);
        } else {
            //Show all the views except progressbar
            progressBar.setVisibility(View.GONE);
            rvPosts.setVisibility(View.VISIBLE);
            fabAddNewPost.setVisibility(View.VISIBLE);
            tvWelcome.setVisibility(View.VISIBLE);
        }
    }

}


