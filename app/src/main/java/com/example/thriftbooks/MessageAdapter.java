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
import com.example.thriftbooks.models.MessageThread;
import com.example.thriftbooks.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private static final String TAG = "MessageAdapter";
    private List<Message> mMessages;
    private Context mContext;
    private final User mUserId;
    private final User mReceiverId;
    private static final int MESSAGE_OUT = 1;
    private static final int MESSAGE_IN = 2;
    private MessageThread thread;


    public MessageAdapter(Context context, User userId, User otherUserId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
        this.mReceiverId = otherUserId;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (isMe(position)) {
            return MESSAGE_IN;
        } else {
            return MESSAGE_OUT;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.bindMessage(message);
    }

    private boolean isMe(int position) {
        Message message = mMessages.get(position);
        return message.getSenderId() != null && message.getSenderId().getObjectId().equals(ParseUser.getCurrentUser().getObjectId());    }

    public abstract class MessageViewHolder extends RecyclerView.ViewHolder {
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindMessage(Message message);
    }

    public class IncomingMessageViewHolder extends MessageViewHolder {
        ImageView imageOther;
        TextView body;
        TextView name;


        public IncomingMessageViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView) itemView.findViewById(R.id.ivProfileOther);
            body = (TextView) itemView.findViewById(R.id.tvIncomingMessage);
            name = (TextView) itemView.findViewById(R.id.tvName);
        }

        @Override
        public void bindMessage(Message message) {
            User buyer = (User) new User();
            try {
                buyer = (User) message.getSenderId().fetchIfNeeded();

            } catch (ParseException e){

            }
            name.setText(buyer.getUsername());
            ParseFile image = (ParseFile) buyer.getProfileImage();
            Glide.with(mContext).load(image.getUrl()).circleCrop().into(imageOther);
            body.setText(message.getBody());
        }
    }

    public class OutgoingMessageViewHolder extends MessageViewHolder {
        ImageView imageCurrentUser;
        TextView body;

        public OutgoingMessageViewHolder(View itemView) {
            super(itemView);
            imageCurrentUser = (ImageView) itemView.findViewById(R.id.ivProfileMe);
            body = (TextView) itemView.findViewById(R.id.tvOutGoingMessage);
        }

        @Override
        public void bindMessage(Message message) {
            User seller = (User) new User();
            try {
               seller = (User) message.getReceiver().fetchIfNeeded();



            } catch (ParseException e){

            }
            ParseFile image = (ParseFile) seller.getProfileImage();
            if (image != null) {
                Glide.with(mContext).load(image.getUrl()).circleCrop().into(imageCurrentUser);
            } else {
                Glide.with(mContext).load(getProfileUrl(message.getReceiver().toString())).circleCrop().into(imageCurrentUser);
            }
            body.setText(message.getBody());
        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == MESSAGE_IN) {
            View viewInflater = inflater.inflate(R.layout.incoming_messages, parent, false);
            return new IncomingMessageViewHolder(viewInflater);
        } else if (viewType == MESSAGE_OUT) {
            View viewInflater = inflater.inflate(R.layout.outgoing_messages, parent, false);
            return new OutgoingMessageViewHolder(viewInflater);
        } else {
            throw new IllegalArgumentException("Unknown view Type");
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