package com.zubiisoft.instazub.userinterface.conversation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zubiisoft.instazub.R;
import com.zubiisoft.instazub.model.Conversation;


import java.util.LinkedList;

public class ConversationAdapter extends
        RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    public interface ConversationCallback {
        void onConversationCallback(Conversation conversation);
    }

    private ConversationCallback mListener;

    private final LayoutInflater mInflater;

    private Context mContext;
    private LinkedList<Conversation> mConversations;
    private ConversationAdapter mAdapter;

    ConversationAdapter(Context context, LinkedList<Conversation> conversations, ConversationCallback conversationCallback) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mConversations = conversations;
        mAdapter = this;
        mListener = conversationCallback;
    }

    @NonNull
    @Override
    public ConversationAdapter.ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        View view = mInflater.inflate(R.layout.conversation_item, parent, false);
        return new ConversationAdapter.ConversationViewHolder(view, mAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.ConversationViewHolder holder, int position) {
        final Conversation currentConversation = mConversations.get(position);
        // TODO holder.mAvatar.setImageDrawable(R.drawable.);
        holder.mName.setText(currentConversation.getName());
        // TODO holder.mLastMessage.setText(currentConversation.getLastMessage());

        holder.mConversationItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onConversationCallback(currentConversation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mConversations.size();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        protected RelativeLayout mConversationItemView;
        protected ImageView mAvatar;
        protected TextView mName;
        protected TextView mLastMessage;
        protected ConversationAdapter mAdapter;

        public ConversationViewHolder(@NonNull View itemView, ConversationAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            mConversationItemView = itemView.findViewById(R.id.conversationItem_relativeLayout);
            mAvatar = itemView.findViewById(R.id.avatar_imageView);
            mName = itemView.findViewById(R.id.name_textView);
            mLastMessage = itemView.findViewById(R.id.lastMessage_textView);
        }
    }

}
