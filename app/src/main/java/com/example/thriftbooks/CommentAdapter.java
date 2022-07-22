package com.example.thriftbooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thriftbooks.models.Comment;
import com.parse.ParseFile;

import java.util.Date;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private final Context context;
    private final List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewComments = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(viewComments);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageCommentator;
        private final TextView actualComment;
        private final TextView timeCommented;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCommentator = itemView.findViewById(R.id.ivCommentator);
            actualComment = itemView.findViewById(R.id.tvActualComment);
            timeCommented = itemView.findViewById(R.id.tvCommentTimeStamp);
        }

        public void bind(Comment comment) {
            Date timeStamp = comment.getCreatedAt();
            String time = Comment.calculateTimeAgo(timeStamp);
            timeCommented.setText(time);
            actualComment.setText(comment.getActualComment());
            try {
                ParseFile image = comment.getCommentator().getProfileImage();
                if (image != null) {
                    Glide.with(context).load(image.getUrl()).circleCrop().into(imageCommentator);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
