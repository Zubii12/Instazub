package com.zubiisoft.instazub.userinterface.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.R;
import com.zubiisoft.instazub.model.Post;
import com.zubiisoft.instazub.userinterface.conversation.ConversationActivity;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 *
 */
public class HomeFragment extends Fragment implements
        View.OnClickListener {

    private static final String TAG = "HomeFragment";

    private View mRootView;
    private Context mContext;
    private ImageView mCameraButton;
    private ImageView mConversationButton;
    private RecyclerView mRecyclerView;

    private String mUid;

    private LinkedList<Post> mPosts = new LinkedList<>();
    private PostAdapter mPostAdapter;

    private View.OnClickListener mListener;

    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;

    public HomeFragment() { }

    @NotNull
    // TODO    @Contract(" -> new")
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate
                (R.layout.fragment_home, container, false);

        initInstances();
        initListeners();
        initRecyclerView();

        return mRootView;
    }

    private void initInstances() {
        mListener = this;
        mCameraButton = mRootView.findViewById(R.id.camera_imageView);
        mConversationButton = mRootView.findViewById(R.id.conversation_imageView);
        mRecyclerView = mRootView.findViewById(R.id.post_recyclerView);

        mDatabase = InstazubApplication.getFirebaseFirestore();
        mAuth = InstazubApplication.getFirebaseAuth();

        mUid = mAuth.getUid();

    }

    private void initListeners() {
        mCameraButton.setOnClickListener(mListener);
        mConversationButton.setOnClickListener(mListener);

    }

    private void initRecyclerView() {
        mPostAdapter = new PostAdapter(mContext, mPosts);
        mRecyclerView.setAdapter(mPostAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onClick(@NotNull View view) {
        switch (view.getId()) {
            case R.id.camera_imageView:
                startCameraActivity();
                break;
            case R.id.conversation_imageView:
                startConversationActivity();
                break;
        }
    }

    private void startCameraActivity() { }
    private void startConversationActivity() {
        startActivity(new Intent(mContext, ConversationActivity.class));
    }

}