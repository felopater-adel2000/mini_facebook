package com.faculty.minifbapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Post> posts;

    public PostsAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //4awrt 3la el post item
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        //Population ll data in l post item
        ((CustomViewHolder) holder).tvPostBody.setText(posts.get(position).getBody());
        ((CustomViewHolder) holder).tvPostCreatedDate.setText(posts.get(position).getCreatedDate());
        ((CustomViewHolder) holder).tvUserName.setText(posts.get(position).getUser().getUserName());
        ((CustomViewHolder) holder).itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), PostDetailsActivity.class);
            intent.putExtra("post", posts.get(position));
            holder.itemView.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        //Find views of our post item
        private TextView tvUserName, tvPostCreatedDate, tvPostBody;

        public CustomViewHolder(View view) {
            super(view);
            tvUserName = view.findViewById(R.id.tv_user_name);
            tvPostCreatedDate = view.findViewById(R.id.tv_post_created_date);
            tvPostBody = view.findViewById(R.id.tv_post_body);
        }
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }
}
