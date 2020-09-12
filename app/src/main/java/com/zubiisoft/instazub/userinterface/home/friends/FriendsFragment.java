package com.zubiisoft.instazub.userinterface.home.friends;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.R;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class FriendsFragment extends Fragment implements
        View.OnClickListener {

    private static final String TAG = "FriendsFragment";

    private View mRootView;
    private Context mContext;

    private String mUid;

    private View.OnClickListener mListener;

    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;

    public FriendsFragment() {
    }

    @NotNull
    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView =  inflater.inflate
                (R.layout.fragment_friends, container, false);

        initInstances();
        initListeners();

        return mRootView;
    }

    private void initInstances() {
        mListener = this;
        mAuth = InstazubApplication.getFirebaseAuth();
        mUid = mAuth.getUid();
    }

    private void initListeners() {

    }

    @Override
    public void onClick(View view) {

    }
}