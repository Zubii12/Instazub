package com.zubiisoft.instazub.userinterface.conversation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.R;
import com.zubiisoft.instazub.database.Database;
import com.zubiisoft.instazub.interfaces.UserCallback;
import com.zubiisoft.instazub.model.Conversation;
import com.zubiisoft.instazub.model.User;
import com.zubiisoft.instazub.userinterface.home.friends.FriendsAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 */
public class AddConversationFragment extends Fragment implements
        FriendsAdapter.OnFriendCallback {

    private View mRootView;
    private RecyclerView mRecyclerView;
    private FriendsAdapter mAdapter;
    private Context mContext;
    private LinkedList<User> mFriends = new LinkedList<>();

    private FriendsAdapter.OnFriendCallback mFriendCallback;

    private UserCallback mUserCallback;

    private Database mDatabase;
    private FirebaseAuth mAuth;
    private String mUid;
    public AddConversationFragment() {
        // Required empty public constructor
    }

    @NotNull
    public static AddConversationFragment newInstance(){
        return new AddConversationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_add_conversation, container, false);

        initInstances();
        initListeners();
        initRecyclerView();

        setFriendsInRecyclerView();

        return mRootView;
    }

    private void initInstances() {
        mRecyclerView = mRootView.findViewById(R.id.addConversation_recyclerView);
        mFriendCallback = this;
        mDatabase = InstazubApplication.getDatabase();
        mAuth = InstazubApplication.getFirebaseAuth();
        mUid = mAuth.getUid();
    }

    private void initListeners() {

    }

    private void initRecyclerView() {
        mAdapter = new FriendsAdapter(mContext , mFriends, mFriendCallback);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof UserCallback) {
            mUserCallback = (UserCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onFriendCallback(User user) {
        mUserCallback.onUserCallback(user);
    }

    private void setFriendsInRecyclerView() {
        mDatabase.readAllFriendsUidFromSpecificUser(new Database.UsersUidCallback() {
            @Override
            public void onUsersUidCallback(ArrayList<String> uidList) {
               for (String friendUid : uidList) {
                   mDatabase.readUser(new UserCallback() {
                       @Override
                       public void onUserCallback(User user) {
                           if (user.getConversations().isEmpty()) {
                               mFriends.addLast(user);
                               mAdapter.notifyItemInserted(mFriends.size());
                           } else {
                               for (Conversation conversation : user.getConversations()) {
                                   if (!conversation.getFriendUid().equals(mUid)) {
                                       mFriends.addLast(user);
                                       mAdapter.notifyItemInserted(mFriends.size());
                                   }
                               }
                           }
                       }
                   }, friendUid);
               }
            }
        }, mUid);
    }
}