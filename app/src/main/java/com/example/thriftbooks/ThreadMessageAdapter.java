package com.example.thriftbooks;

import static com.example.thriftbooks.models.MessageThread.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thriftbooks.activities.MessageActivity;
import com.example.thriftbooks.models.MessageThread;
import com.example.thriftbooks.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThreadMessageAdapter extends RecyclerView.Adapter<ThreadMessageAdapter.ViewHolder> {
    private final Context context;
    private final List<MessageThread> threads;

    public ThreadMessageAdapter(Context context, List<MessageThread> threads) {
        this.context = context;
        this.threads = threads;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.thread_message_item_buyers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageThread thread = threads.get(position);
        holder.bind(thread);

    }

    @Override
    public int getItemCount() {
        return threads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView username;
        private final TextView lastMessage;
        private final CircleImageView image;
        private final RelativeLayout relativeLayout;
        private final TextView bookName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.rlThreadBuyerItem);
            username = itemView.findViewById(R.id.tvBuyerName);
            lastMessage = itemView.findViewById(R.id.tvBuyerLastMessage);
            image = itemView.findViewById(R.id.ciProfileImage);
            ImageView status = itemView.findViewById(R.id.ciOnlineStatus);
            ImageView statusSeen = itemView.findViewById(R.id.ciOnlineStatus);
            bookName = itemView.findViewById(R.id.tvBookTitle);
        }

        public void bind(MessageThread thread) {
            User buyer = new User();
            try {
                buyer = (User) thread.getBuyerId().fetchIfNeeded();
            } catch (ParseException e) {
                Log.e(TAG, "Error" + e);
            }
            //lastMessage.setText(thread.getLatestMessage());
            username.setText(buyer.getUsername());
            bookName.setText(thread.getPostId().getBookTitle());
            ParseFile imageBuyer = buyer.getProfileImage();
            if (imageBuyer != null) {
                Glide.with(context).load(imageBuyer.getUrl()).into(image);
            }
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, MessageActivity.class);
                    i.putExtra("messageThreadInfo", Parcels.wrap(thread));
                    context.startActivity(i);
                }
            });
        }
    }
}
