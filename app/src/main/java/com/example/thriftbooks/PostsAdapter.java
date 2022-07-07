package com.example.thriftbooks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thriftbooks.activities.BookDetailsActivity;
import com.example.thriftbooks.models.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private final Context context;
    private final List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);

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
        private final TextView tvUsername;
        private final ImageView ivImage;
        private final TextView tvDescription;
        private final ImageView displayPicture;
        private final TextView tvBookAuthor, timeAgo;
        private final TextView tvBookTitle;
        private final TextView tvBookType;
        private final TextView tvBookCondition;
        private final ImageButton ibComment;
        private final ImageButton ibMessage; //Gonna use ibMessage later for messaging the book owner

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivPostClickedBook);
            tvDescription = itemView.findViewById(R.id.tvPostDescription);
            displayPicture = itemView.findViewById(R.id.profilePicture);
            tvBookTitle = itemView.findViewById(R.id.tvPostBookTitle);
            tvBookAuthor = itemView.findViewById(R.id.tvPostBookAuthor);
            tvBookType = itemView.findViewById(R.id.tvPostBookType);
            tvBookCondition = itemView.findViewById(R.id.tvPostBookCondition);
            ibComment = itemView.findViewById(R.id.ibComment);
            ibMessage = itemView.findViewById(R.id.ibMessage);
            timeAgo = itemView.findViewById(R.id.timeAgo);
        }
        public void bind(Post post)  {
            Date timeStamp = post.getCreatedAt();
            String time = Post.calculateTimeAgo(timeStamp);
            timeAgo.setText(time + " ago");
            tvUsername.setText(post.getUser().getUsername());
            tvBookTitle.setText("Title: " + post.getBookTitle());
            tvBookAuthor.setText("Author: " + post.getBookAuthor());
            tvDescription.setText("Description: " + post.getDescription());
            tvBookCondition.setText("Condition: "+ post.getBookCondition());
            tvBookType.setText("For: " + post.getBookType());

            ParseFile profileImage = post.getUser().getProfileImage();
            if (profileImage != null) {
                Glide.with(context).load(profileImage.getUrl()).circleCrop().into(displayPicture);
            }
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, BookDetailsActivity.class);
                    i.putExtra("details", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });

        }
    }
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<Post> list){
        posts.addAll(list);
        notifyDataSetChanged();
    }

}