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
import com.example.thriftbooks.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private final Context context;
    private final List<Post> posts;
    private static final String TAG = "SearchAdapter";

    public SearchAdapter(Context context, List<Post> posts) {
        this.context  = context;
        this.posts = posts;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewSearch = LayoutInflater.from(context).inflate(R.layout.book_search_item,parent, false);
        return new ViewHolder(viewSearch);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAboutVolume;
        private final TextView tvBookAuthor;
        private final TextView tvBookTitle;
        private final TextView tvPublishedDate;
        private final TextView tvPublisher,tvGenre;
        private final ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAboutVolume = itemView.findViewById(R.id.tvAboutVolume);
            tvPublisher = itemView.findViewById(R.id.tvPublisher);
            tvPublishedDate = itemView.findViewById(R.id.tvPublishedDate);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            ivImage = itemView.findViewById(R.id.ivBook);
            tvGenre = itemView.findViewById(R.id.tvGenre);

        }

        public void bind(Post post) {
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).centerInside().into(ivImage);
            }
            tvAboutVolume.setText("Description: " + post.getBookAuthor());
            tvBookAuthor.setText("Author: " + post.getBookAuthor());
            tvBookTitle.setText("Title: " + post.getBookTitle());
            tvPublisher.setText("Publisher: ");
            tvGenre.setText("Genre: " );
            tvPublishedDate.setText("PublishedDate: ");
        }
    }
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }
}
