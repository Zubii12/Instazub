package com.zubiisoft.instazub.userinterface.room;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zubiisoft.instazub.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;


public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private static final String TAG = "RoomAdapter";

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private final LayoutInflater mInflater;

    private Context mContext;
    private LinkedList<ArrayList<String>> mMessages;
    private RoomAdapter mAdapter;
    private String mUid;

    public RoomAdapter(Context context, LinkedList<ArrayList<String>> messages, String uid) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mMessages = messages;
        mAdapter = this;
        mUid = uid;
    }

    @NonNull
    @Override
    public RoomAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = mInflater.inflate
                    (R.layout.right_message_item, parent, false);

            return new RoomViewHolder(view, this);
        } else  {
            View view = mInflater.inflate
                    (R.layout.left_message_item, parent, false);

            return new RoomViewHolder(view, this);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.RoomViewHolder holder, int position) {
        ArrayList<String> current = mMessages.get(position);
        final int NEXT_ITEM = position - 1;
        final int PREVIOUS_ITEM = position + 1;

        try {
            if (getItemViewType(position) == MSG_TYPE_LEFT) {
                if (NEXT_ITEM < 0) {
                    holder.mAvatar.setImageDrawable(null);
                } else if (getItemViewType(PREVIOUS_ITEM) == MSG_TYPE_RIGHT && getItemViewType(NEXT_ITEM) == MSG_TYPE_LEFT) {
                    holder.mAvatar.setVisibility(View.VISIBLE);
                } else if (getItemViewType(PREVIOUS_ITEM) == MSG_TYPE_LEFT && getItemViewType(NEXT_ITEM) == MSG_TYPE_LEFT) {
                    holder.mAvatar.setImageDrawable(null);
                } else if (getItemViewType(PREVIOUS_ITEM) == MSG_TYPE_LEFT && getItemViewType(NEXT_ITEM) == MSG_TYPE_RIGHT) {
                    holder.mAvatar.setImageDrawable(null);
                }
            }
        } catch (IndexOutOfBoundsException | NullPointerException ignored) { }

        // TODO holder.mAvatar.setImageDrawable();
        holder.mMessage.setText(current.get(2));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!(mMessages == null)) {
            if (mUid.equals(mMessages.get(position).get(1))) {
                return MSG_TYPE_RIGHT;
            } else {
                return MSG_TYPE_LEFT;
            }
        } else {
            return position;
        }
    }


    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        protected View mItemView;
        protected ImageView mAvatar;
        protected TextView mMessage;
        protected RoomAdapter mAdapter;

        RoomViewHolder(@NotNull View itemView, RoomAdapter adapter) {
            super(itemView);
            mItemView = itemView;
            mAvatar = itemView.findViewById(R.id.avatar_imageView);
            mMessage = itemView.findViewById(R.id.message_textView);
            mAdapter = adapter;
        }
    }
}
