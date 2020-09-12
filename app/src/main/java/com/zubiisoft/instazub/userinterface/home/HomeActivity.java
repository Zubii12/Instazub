package com.zubiisoft.instazub.userinterface.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.R;
import com.zubiisoft.instazub.constants.Constant;
import com.zubiisoft.instazub.model.Post;
import com.zubiisoft.instazub.userinterface.conversation.ConversationActivity;
import com.zubiisoft.instazub.userinterface.friends.FriendsActivity;
import com.zubiisoft.instazub.userinterface.main.MainActivity;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements
        View.OnClickListener{

    private static final String TAG = "HomeActivity";

    private ImageView mCameraButton;
    private ImageView mConversationButton;
    private RecyclerView mRecyclerView;
    private ImageView mHomeButton;
    private ImageView mFriendsButton;
    private ImageView mAddPostButton;
    private ImageView mProfileButton;
    private ImageView mNotificationButton;

    private Context mContext;
    private View.OnClickListener mListener;

    private String mUid;

    private LinkedList<Post> mPosts = new LinkedList<>();
    private PostAdapter mPostAdapter;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initInstances();
        initListeners();
        initRecyclerView();

    }

    private void initInstances() {
        mContext = this;
        mListener = this;
        mCameraButton = findViewById(R.id.camera_imageView);
        mConversationButton = findViewById(R.id.conversation_imageView);
        mRecyclerView = findViewById(R.id.post_recyclerView);
        mHomeButton = findViewById(R.id.home_imageView);
        mFriendsButton = findViewById(R.id.friends_imageView);
        mAddPostButton = findViewById(R.id.add_imageView);
        mProfileButton = findViewById(R.id.profile_imageView);
        mNotificationButton = findViewById(R.id.notification_imageView);

        mDatabase = InstazubApplication.getFirebaseFireStore();
        mAuth = InstazubApplication.getFirebaseAuth();

        mUid = mAuth.getUid();
    }

    private void initListeners() {
        mCameraButton.setOnClickListener(mListener);
        mConversationButton.setOnClickListener(mListener);
        mHomeButton.setOnClickListener(mListener);
        mFriendsButton.setOnClickListener(mListener);
        mAddPostButton.setOnClickListener(mListener);
        mProfileButton.setOnClickListener(mListener);
        mNotificationButton.setOnClickListener(mListener);
    }

    private void initRecyclerView() {
        mPostAdapter = new PostAdapter(mContext, mPosts);
        mRecyclerView.setAdapter(mPostAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camera_imageView:
                startCameraActivity();
                break;
            case R.id.conversation_imageView:
                startConversationActivity();
                break;
            case R.id.home_imageView:
                startHomeActivity();
                break;
            case R.id.friends_imageView:
                startFriendsActivity();
                break;
            case R.id.add_imageView:
                startAddPostActivity();
                break;
            case R.id.profile_imageView:
                startProfileActivity();
                break;
            case R.id.notification_imageView:
                startNotificationActivity();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOut_item:
                mAuth.signOut();
                startMainActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    private void startCameraActivity() { }
    private void startConversationActivity() {
        startActivity(new Intent(mContext, ConversationActivity.class));
    }
    private void startHomeActivity() { }
    private void startFriendsActivity() {
        startActivity(new Intent(mContext, FriendsActivity.class));
    }
    private void startAddPostActivity() {

    }
    private void startProfileActivity() { }
    private void startNotificationActivity() { }
    private void startMainActivity() {
        startActivity(new Intent(mContext, MainActivity.class));
    }
}