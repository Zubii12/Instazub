package com.zubiisoft.instazub.userinterface.friends;

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
import com.zubiisoft.instazub.model.User;

import java.util.LinkedList;

public class FriendsAdapter extends
        RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {


    public interface OnFriendCallback {
        void onFriendCallback(User user);
    }

    private static final String TAG = "FriendsAdapter";

    private final LayoutInflater mInflater;

    private Context mContext;
    private LinkedList<User> mFriends;
    private FriendsAdapter mAdapter;
    private OnFriendCallback mFriendCallback;

    public FriendsAdapter(Context context, LinkedList<User> friends, OnFriendCallback onFriendCallback) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mFriends = friends;
        mAdapter = this;
        mFriendCallback = onFriendCallback;
    }

    @NonNull
    @Override
    public FriendsAdapter.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        View view = mInflater.inflate(R.layout.friends_item, parent, false);
        return new FriendsAdapter.FriendsViewHolder(view, mAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.FriendsViewHolder holder, final int position) {
        final User currentUser = mFriends.get(position);
        // TODO holder.mAvatar.setImageDrawable(R.drawable.);
        String name = currentUser.getFirstName() + " " + currentUser.getLastName();
        holder.mName.setText(name);

        holder.mFriendsItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFriends.remove(currentUser);
                mAdapter.notifyItemRemoved(position);

                mFriendCallback.onFriendCallback(currentUser);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mAvatar;
        protected TextView mName;
        protected FriendsAdapter mAdapter;
        protected RelativeLayout mFriendsItemView;

        public FriendsViewHolder(@NonNull View itemView, FriendsAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            mAvatar = itemView.findViewById(R.id.avatar_imageView);
            mName = itemView.findViewById(R.id.name_textView);
            mFriendsItemView = itemView.findViewById(R.id.friendsItem_relativeLayout);
        }
    }
}