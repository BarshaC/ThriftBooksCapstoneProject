package com.example.thriftbooks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thriftbooks.activities.BookDetailsActivity;
import com.example.thriftbooks.models.Book;
import com.example.thriftbooks.models.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private final Context context;
    private final List<Book> books;

    public BooksAdapter(Context context, ArrayList<Book> books){
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
        private TextView tvDescription;
        private TextView tvBookAuthor;
        private TextView tvBookTitle;
        private TextView tvPageCount;
        private TextView tvPublisher;
        private TextView tvPublishedDate;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvAboutVolume);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvPageCount = itemView.findViewById(R.id.tvPageCount);
            tvPublishedDate = itemView.findViewById(R.id.tvPublishedDate);
            tvPublisher = itemView.findViewById(R.id.tvPublisher);

        }
        public void bind(Book book) {
            tvBookTitle.setText("Title: " + book.getSearchTitle());
            tvBookAuthor.setText("Author: " + book.getSearchAuthors());
            tvDescription.setText("Description: " + book.getAboutVolume());
            tvPublisher.setText("Publisher:" + book.getSearchPublisher());
            tvPublishedDate.setText("PublishedDate: " + book.getPublishedDate());
            tvPageCount.setText("Page: " + String.valueOf(book.getPageCount()));
            tvDescription.setText("Description: " + book.getAboutVolume());

            //Use this code later to display picture of the actual book image if not get the picture from Picasso
//            ivImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(context, BookDetailsActivity.class);
//                    i.putExtra("details", Parcels.wrap(post));
//                    context.startActivity(i);
//                }
//           });
        }
    }

    public void clear() {
        books.clear();
        notifyDataSetChanged();
    }
    public void addAll(ArrayList<Book> list){
        books.addAll(list);
        notifyDataSetChanged();
    }

}
