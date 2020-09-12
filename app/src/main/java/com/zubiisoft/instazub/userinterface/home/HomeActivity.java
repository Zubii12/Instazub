package com.zubiisoft.instazub.userinterface.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.zubiisoft.instazub.userinterface.home.friends.FriendsFragment;
import com.zubiisoft.instazub.userinterface.home.home.HomeFragment;
import com.zubiisoft.instazub.userinterface.home.notifications.NotificationFragment;
import com.zubiisoft.instazub.userinterface.home.posts.AddPostFragment;
import com.zubiisoft.instazub.userinterface.home.profile.ProfileFragment;
import com.zubiisoft.instazub.userinterface.main.MainActivity;

public class HomeActivity extends AppCompatActivity implements
        View.OnClickListener{

    private static final String TAG = "HomeActivity";

    private ImageView mHomeButton;
    private ImageView mFriendsButton;
    private ImageView mAddPostButton;
    private ImageView mProfileButton;
    private ImageView mNotificationButton;

    private Context mContext;
    private View.OnClickListener mListener;

    private String mUid;

    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;


    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initInstances();
        initListeners();
        initRecyclerView();

        startHomeFragment();
    }

    private void initInstances() {
        mContext = this;
        mListener = this;
        mHomeButton = findViewById(R.id.home_imageView);
        mFriendsButton = findViewById(R.id.friends_imageView);
        mAddPostButton = findViewById(R.id.add_imageView);
        mProfileButton = findViewById(R.id.profile_imageView);
        mNotificationButton = findViewById(R.id.notification_imageView);

        mDatabase = InstazubApplication.getFirebaseFireStore();
        mAuth = InstazubApplication.getFirebaseAuth();

        mUid = mAuth.getUid();

        mFragmentManager = getSupportFragmentManager();
    }

    private void initListeners() {
        mHomeButton.setOnClickListener(mListener);
        mFriendsButton.setOnClickListener(mListener);
        mAddPostButton.setOnClickListener(mListener);
        mProfileButton.setOnClickListener(mListener);
        mNotificationButton.setOnClickListener(mListener);
    }

    private void initRecyclerView() { }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_imageView:
                startHomeFragment();
                break;
            case R.id.friends_imageView:
                startFriendsFragment();
                break;
            case R.id.add_imageView:
                startAddPostFragment();
                break;
            case R.id.profile_imageView:
                startProfileFragment();
                break;
            case R.id.notification_imageView:
                startNotificationFragment();
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

    private void startHomeFragment() {
        HomeFragment homeFragment = HomeFragment.newInstance();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (mFragmentManager.getBackStackEntryCount() == 0) {
            fragmentTransaction.add(R.id.homeFragmentContainer_frameLayout, homeFragment)
                    .addToBackStack(null).commit();
        } else {
            mFragmentManager.popBackStack();

            fragmentTransaction.replace(R.id.homeFragmentContainer_frameLayout, homeFragment);
            fragmentTransaction.addToBackStack(null).commit();

        }
    }

    private void startFriendsFragment() {
        FriendsFragment friendsFragment = FriendsFragment.newInstance();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentManager.popBackStack();
        fragmentTransaction.replace(R.id.homeFragmentContainer_frameLayout, friendsFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    private void startAddPostFragment() {
        AddPostFragment addPostFragment = AddPostFragment.newInstance();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentManager.popBackStack();
        fragmentTransaction.replace(R.id.homeFragmentContainer_frameLayout, addPostFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    private void startProfileFragment() {
        ProfileFragment profileFragment = ProfileFragment.newInstance();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentManager.popBackStack();
        fragmentTransaction.replace(R.id.homeFragmentContainer_frameLayout, profileFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    private void startNotificationFragment() {
        NotificationFragment notificationFragment = NotificationFragment.newInstance();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentManager.popBackStack();
        fragmentTransaction.replace(R.id.homeFragmentContainer_frameLayout, notificationFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    private void startMainActivity() {
        startActivity(new Intent(mContext, MainActivity.class));
    }
}