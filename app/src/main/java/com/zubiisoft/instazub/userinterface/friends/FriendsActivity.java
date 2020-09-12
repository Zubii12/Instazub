package com.zubiisoft.instazub.userinterface.friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.R;
import com.zubiisoft.instazub.constants.Constant;
import com.zubiisoft.instazub.database.Database;
import com.zubiisoft.instazub.model.User;
import com.zubiisoft.instazub.userinterface.home.HomeActivity;

import java.util.ArrayList;
import java.util.LinkedList;

public class FriendsActivity extends AppCompatActivity implements
        View.OnClickListener,
        FriendsAdapter.OnFriendCallback {

    private static final String TAG = "FriendsActivity";

    private LinkedList<User> mFriends = new LinkedList<>();

    private Context mContext;
    private FirebaseAuth mAuth;
    private Database mDatabase;
    private View.OnClickListener mListener;

    private RecyclerView mRecyclerView;
    private ImageView mHomeButton;
    private ImageView mFriendsButton;
    private ImageView mProfileButton;
    private ImageView mNotificationButton;

    private FriendsAdapter mFriendsAdapter;
    private FriendsAdapter.OnFriendCallback mFriendCallback;
    private String mUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        initInstances();
        initListeners();
        initRecyclerView();
        setUsersInRecyclerView();
    }

    private void initInstances() {
        mContext = this;
        mFriendCallback = this;
        mListener = this;

        mAuth = InstazubApplication.getFirebaseAuth();
        mDatabase = InstazubApplication.getDatabase();

        mUid = mAuth.getUid();

        mRecyclerView = findViewById(R.id.friends_recyclerView);
        mHomeButton = findViewById(R.id.home_imageView);
        mFriendsButton = findViewById(R.id.friends_imageView);
        mProfileButton = findViewById(R.id.profile_imageView);
        mNotificationButton = findViewById(R.id.notification_imageView);

    }

    private void initListeners() {
        mHomeButton.setOnClickListener(mListener);
    }

    private void initRecyclerView() {
        mFriendsAdapter = new FriendsAdapter(mContext, mFriends, mFriendCallback);
        mRecyclerView.setAdapter(mFriendsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_imageView:
                startActivity(new Intent(mContext, HomeActivity.class));
                break;
        }
    }

    @Override
    public void onFriendCallback(User user) {
        mDatabase.writeFriend(new Database.AnswerWriting() {
            @Override
            public void onAnswer(boolean isWrite) {
                if (isWrite) {
                    Toast.makeText(mContext, " Success ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, " Fail ", Toast.LENGTH_SHORT).show();
                }
            }
        }, user.getUid());
    }

    private void setUsersInRecyclerView() {
        mDatabase.readAllUser(new Database.UsersCallback() {
            @Override
            public void onUsersCallback(ArrayList<User> users) {
                for (User user : users) {
                    // TODO performance update, by give the friends list of the user
                    if (!user.getFriends().contains(mUid)) {
                        Log.d(TAG, "onUsersCallback: mUid: " + mUid + "\n friendsUid: " + user.getFriends());
                        mFriends.addLast(user);
                        mFriendsAdapter.notifyItemInserted(mFriends.size() + 1);
                    }
                }
            }
        });
    }
}