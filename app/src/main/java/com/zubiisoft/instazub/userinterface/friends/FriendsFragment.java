package com.zubiisoft.instazub.userinterface.friends;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.R;
import com.zubiisoft.instazub.database.Database;
import com.zubiisoft.instazub.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 */
public class FriendsFragment extends Fragment implements
        FriendsAdapter.OnFriendCallback {

    private static final String TAG = "FriendsFragment";

    private LinkedList<User> mFriends = new LinkedList<>();

    private View mRootView;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private FriendsAdapter mFriendsAdapter;


    private String mUid;

    private FriendsAdapter.OnFriendCallback mFriendCallback;

    private Database mDatabase;
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
        initRecyclerView();
        setUsersInRecyclerView();

        return mRootView;
    }

    private void initInstances() {
        mFriendCallback = this;

        mDatabase = InstazubApplication.getDatabase();
        mAuth = InstazubApplication.getFirebaseAuth();

        mUid = mAuth.getUid();

        mRecyclerView = mRootView.findViewById(R.id.friends_recyclerView);
    }

    private void initListeners() { }

    private void initRecyclerView() {
        mFriendsAdapter = new FriendsAdapter(mContext, mFriends, mFriendCallback);
        mRecyclerView.setAdapter(mFriendsAdapter);
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
    public void onFriendCallback(User user) {
        mDatabase.writeFriend(new Database.AnswerWriting() {
            @Override
            public void onAnswer(boolean isWrite) {
                if (isWrite) {
                    Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
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
                        mFriends.addLast(user);
                        mFriendsAdapter.notifyItemInserted(mFriends.size() + 1);
                    }
                }
                if (mFriends.isEmpty()) {
                    //setSnackbar();
                }
            }
        });
    }

    private void setSnackbar() {
        final Snackbar snackbar = Snackbar.make(mRootView, "Empty", BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setTextColor(mContext.getColor(R.color.colorPalette3));
        snackbar.setActionTextColor(mContext.getColor(R.color.colorPalette3));
        snackbar.setBackgroundTint(mContext.getColor(R.color.colorPalette2));
        snackbar.setAction("Close", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        }).show();
    }
}