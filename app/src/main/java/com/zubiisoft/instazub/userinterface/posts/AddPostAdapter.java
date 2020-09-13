package com.zubiisoft.instazub.userinterface.posts;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zubiisoft.instazub.R;
import com.zubiisoft.instazub.userinterface.conversation.ConversationAdapter;

import java.util.LinkedList;

public class AddPostAdapter extends
        RecyclerView.Adapter<AddPostAdapter.AddPostViewHolder> {

    public interface ImageCallback {
        void onImageCallback(ImageView image, String path);
    }

    private static final String TAG = "AddPostAdapter";
    private final LayoutInflater mInflater;

    private Context mContext;
    private LinkedList<String> mImages;
    private AddPostAdapter mAdapter;

    private ImageCallback mImageCallback;

    public AddPostAdapter(Context context, LinkedList<String> images, ImageCallback imageCallback) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mImages = images;
        mAdapter = this;
        mImageCallback = imageCallback;
    }

    @NonNull
    @Override
    public AddPostAdapter.AddPostViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate
                (R.layout.image_item, parent, false);
        return new AddPostAdapter.AddPostViewHolder(view, mAdapter);
    }

    @Override
    public void onBindViewHolder
            (@NonNull final AddPostAdapter.AddPostViewHolder holder, int position) {
        final String currentPath = mImages.get(position);

        //Glide.with(mContext).load(mImages.get(position)).placeholder(R.drawable.ic_logo_instazub_24).centerCrop().into(holder.mImage);
        Glide.with(mContext).load(currentPath).into(holder.mImage);


        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageCallback.onImageCallback(holder.mImage, currentPath);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public static class AddPostViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mImage;
        protected AddPostAdapter mAdapter;

        public AddPostViewHolder(@NonNull View itemView, AddPostAdapter adapter) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image_imageView);
            mAdapter = adapter;
        }
    }
}
