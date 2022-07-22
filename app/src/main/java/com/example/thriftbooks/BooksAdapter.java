package com.example.thriftbooks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thriftbooks.activities.BookDetailsActivity;
import com.example.thriftbooks.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private final Context context;
    private final List<Book> books;

    public BooksAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_search_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDescription;
        private final TextView tvBookAuthor;
        private final TextView tvBookTitle;
        private final TextView tvPageCount;
        private final TextView tvPublisher;
        private final TextView tvPublishedDate;
        private final ImageView ivPosterImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvAboutVolume);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvPageCount = itemView.findViewById(R.id.tvPageCount);
            tvPublishedDate = itemView.findViewById(R.id.tvPublishedDate);
            tvPublisher = itemView.findViewById(R.id.tvPublisher);
            ivPosterImage = itemView.findViewById(R.id.ivBook);

        }

        public void bind(Book book) {
            tvBookTitle.setText("Title: " + book.getSearchTitle());
            tvBookAuthor.setText("Author: " + book.getSearchAuthors());
            tvDescription.setText("Description: " + book.getAboutVolume());
            tvPublisher.setText("Publisher:" + book.getSearchPublisher());
            tvPublishedDate.setText("PublishedDate: " + book.getPublishedDate());
            tvPageCount.setText("Page: " + book.getPageCount());
            tvDescription.setText("Description: " + book.getAboutVolume());
            ivPosterImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, BookDetailsActivity.class);
                    context.startActivity(i);
                }
            });
        }
    }

    public void clear() {
        books.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Book> list) {
        books.addAll(list);
        notifyDataSetChanged();
    }

}
