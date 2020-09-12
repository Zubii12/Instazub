package com.zubiisoft.instazub.userinterface.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zubiisoft.instazub.R;
import com.zubiisoft.instazub.model.Post;

import java.util.LinkedList;

public class PostAdapter extends
        RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private static final String TAG = "PostAdapter";

    private final LayoutInflater mInflater;

    private Context mContext;
    private LinkedList<Post> mPosts;
    private PostAdapter mAdapter;

    public PostAdapter(Context context, LinkedList<Post> posts) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mPosts = posts;
        mAdapter = this;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        View view = mInflater.inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view, mAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        Post currentPost = mPosts.get(position);
        // TODO holder.mAvatar.setImageDrawable(R.drawable.);
        holder.mName.setText(currentPost.getName());
        // TODO holder.mTime.setText(currentPost.getTime());
        // TODO holder.mContent.setImageDrawable(R.drawable.);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mAvatar;
        protected TextView mName;
        protected TextView mTime;
        protected ImageView mContent;
        protected ImageView mLikeImageView;
        protected TextView mLikeTextView;
        protected ImageView mCommentImageView;
        protected TextView mCommentTextView;
        protected PostAdapter mAdapter;

        public PostViewHolder(@NonNull View itemView, PostAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            mAvatar = itemView.findViewById(R.id.avatar_imageView);
            mName = itemView.findViewById(R.id.name_textView);
            mTime = itemView.findViewById(R.id.time_textView);
            mContent = itemView.findViewById(R.id.content_imageView);
            mLikeImageView = itemView.findViewById(R.id.like_imageView);
            mLikeTextView = itemView.findViewById(R.id.like_textView);
            mCommentImageView = itemView.findViewById(R.id.comment_imageView);
            mCommentTextView = itemView.findViewById(R.id.comment_textView);
        }
    }
}
