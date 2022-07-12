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
import com.example.thriftbooks.models.Post;
import com.example.thriftbooks.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

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
        View view = LayoutInflater.from(context).inflate(R.layout.thread_message_item_buyers,parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView username;
        private final TextView lastMessage;
        private final CircleImageView image;
        private final RelativeLayout relativeLayout;
        private final ImageView status, statusSeen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.rlThreadBuyerItem);
            username = itemView.findViewById(R.id.tvBuyerName);
            lastMessage = itemView.findViewById(R.id.tvBuyerLastMessage);
            image = itemView.findViewById(R.id.ciProfileImage);
            status = itemView.findViewById(R.id.ciOnlineStatus);
            statusSeen = itemView.findViewById(R.id.ciOnlineStatus);
        }

        public void bind(MessageThread thread) {
            User buyer = new User();
            try{
                buyer = (User) thread.getBuyerId().fetchIfNeeded();
            } catch(ParseException e) {

            }
            username.setText(buyer.getUsername());
            lastMessage.setText(thread.getMessageStarter());
            ParseFile imageBuyer = buyer.getProfileImage();
            if (imageBuyer != null) {
                Glide.with(context).load(imageBuyer.getUrl()).into(image);
            }
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, MessageActivity.class);
                    context.startActivity(i);

                }
            });
        }
    }

    private void queryThread() {
        ParseQuery<MessageThread> query  = ParseQuery.getQuery(MessageThread.class);
        query.include(Post.KEY_USER);
        query.setLimit(25);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<MessageThread>() {
            @Override
            public void done(List<MessageThread> threads, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting all the posts",e);
                    return;
                } for (MessageThread thread : threads) {
                    Log.i(TAG, "Threads :" + thread.getPostId());
                }
            }
        });
    }
}
