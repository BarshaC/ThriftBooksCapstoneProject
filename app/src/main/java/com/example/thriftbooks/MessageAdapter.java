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
import com.example.thriftbooks.models.Message;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;
    private static final int MESSAGE_OUT = 123;
    private static final int MESSAGE_IN = 150;

    public MessageAdapter(Context context, String mUserId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = mUserId;
        mContext = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == MESSAGE_IN) {
            View viewInflater = inflater.inflate(R.layout.incoming_messages,parent, false);
            return new IncomingMessageViewHolder(viewInflater);
        } else if (viewType == MESSAGE_OUT) {
            View viewInflater = inflater.inflate(R.layout.outgoing_messages, parent, false);
            return new OutgoingMessageViewHolder(viewInflater);
        } else {
            throw new IllegalArgumentException("Unknown view Type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.bindMessage(message);
    }

    @Override
    public int getItemViewType(int position) {
        if (isMe(position)) {
            return MESSAGE_OUT;
        } else {
            return MESSAGE_IN;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    private boolean isMe(int position) {
        Message message = mMessages.get(position);
        return message.getUserId() != null && message.getUserId().equals(mUserId);
    }

    public abstract class MessageViewHolder extends RecyclerView.ViewHolder {
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindMessage(Message message);
    }

    public class IncomingMessageViewHolder extends MessageViewHolder {
        ImageView imageBuyer;
        TextView body;
        TextView name;


        public IncomingMessageViewHolder(View itemView) {
            super(itemView);
            imageBuyer = (ImageView) itemView.findViewById(R.id.ivProfileOther);
            body = (TextView) itemView.findViewById(R.id.tvBody);
            name = (TextView) itemView.findViewById(R.id.tvName);
        }

        @Override
        public void bindMessage(Message message) {
            Glide.with(mContext).load(getProfileUrl(message.getUserId())).circleCrop().into(imageBuyer);
            body.setText(message.getBody());
            body.setText(message.getUserId());
        }
    }

    public class OutgoingMessageViewHolder extends MessageViewHolder {
        ImageView imageSeller;
        TextView body;

        public OutgoingMessageViewHolder(View itemView) {
            super(itemView);
            imageSeller = (ImageView) itemView.findViewById(R.id.ivProfileMe);
            body = (TextView) itemView.findViewById(R.id.tvBody);
        }

        @Override
        public void bindMessage(Message message) {
            Glide.with(mContext).load(getProfileUrl(message.getUserId())).circleCrop().into(imageSeller);
            body.setText(message.getBody());
        }
    }

    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }
}