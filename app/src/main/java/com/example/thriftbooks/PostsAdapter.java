package com.example.thriftbooks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thriftbooks.activities.BookDetailsActivity;
import com.example.thriftbooks.models.MessageThread;
import com.example.thriftbooks.models.Post;
import com.example.thriftbooks.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private final Context context;
    private final List<Post> posts;
    private static final String TAG = "PostsAdapter";

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
        private final TextView tvBookType, tvMoreAboutPost;
        private final TextView tvBookCondition;
        private final ImageButton ibComment;
        private final Button btnSend;
        private final EditText etSendMessage;

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
            timeAgo = itemView.findViewById(R.id.timeAgo);
            tvMoreAboutPost = itemView.findViewById(R.id.tvMoreDetail);
            btnSend = itemView.findViewById(R.id.btnInterestedInBook);
            etSendMessage = itemView.findViewById(R.id.etStartBuying);
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
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = etSendMessage.getText().toString();
                    Log.d(TAG, "here is the msg: " + message);
                    if (message != null) {
                        MessageThread newMessageThread = new MessageThread();
                        newMessageThread.setSellerId(post.getUser());
                        newMessageThread.setBuyerId((User) ParseUser.getCurrentUser());
                        newMessageThread.setMessageStarter(message);
                        newMessageThread.setPostId(post);
                        newMessageThread.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.d(TAG, "Issue with sending messages" + e.toString());
                                } for (Post post: posts) {
                                        Log.i(TAG, "Posts : " + post.getDescription() + ", " + post.getUser().getUsername());
                                    }
                                etSendMessage.setText("");
                                }
                        });

                    } else {
                        Log.e(TAG, "Not able to send message!");
                    }
                }
            });

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
                    i.putExtra("PostDetails", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });
            tvMoreAboutPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, BookDetailsActivity.class);
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
