package com.example.thriftbooks;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thriftbooks.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Context profileContext;
    private List<Post> posts;

    public ProfileAdapter(Context profileContext, List<Post> posts) {
        this.profileContext = profileContext;
        this.posts = posts;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(profileContext).inflate(R.layout.profile_grid_view, parent, false);
        return new ProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivClickedBookImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivClickedBookImage = itemView.findViewById(R.id.ivGridBookImage);

        }
        public void bind(Post post) {
            ParseFile bookImage = post.getImage();
            if (bookImage != null) {
                Glide.with(profileContext).load(bookImage.getUrl()).into(ivClickedBookImage);
            }

        }
    }

}
