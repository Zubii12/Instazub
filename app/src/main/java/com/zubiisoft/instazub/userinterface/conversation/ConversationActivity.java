package com.zubiisoft.instazub.userinterface.conversation;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.R;
import com.zubiisoft.instazub.database.Database;
import com.zubiisoft.instazub.interfaces.UserCallback;
import com.zubiisoft.instazub.model.Conversation;
import com.zubiisoft.instazub.model.Room;
import com.zubiisoft.instazub.model.User;
import com.zubiisoft.instazub.userinterface.room.RoomActivity;
import com.zubiisoft.instazub.utils.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ConversationActivity extends AppCompatActivity implements
        View.OnClickListener,
        UserCallback,
        ConversationAdapter.ConversationCallback {

    private static final String TAG = "ConversationActivity";

    private LinkedList<Conversation> mConversations = new LinkedList<>();

    private Context mContext;
    private Database mDatabase;
    private FirebaseAuth mAuth;

    private SearchView mSearchConversation;
    private ImageView mAddConversation;
    private RecyclerView mRecyclerView;

    private String mUid;

    private ConversationAdapter mConversationAdapter;
    private View.OnClickListener mListener;
    private ConversationAdapter.ConversationCallback mConversationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        initInstances();
        initListeners();
        initRecyclerView();

        setConversationsInRecyclerView();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initInstances() {
        mContext = this;
        mListener = this;
        mConversationCallback = this;
        mDatabase = InstazubApplication.getDatabase();
        mAuth = InstazubApplication.getFirebaseAuth();

        mUid = mAuth.getUid();

        mSearchConversation = findViewById(R.id.searchConversation_searchView);
        mAddConversation = findViewById(R.id.addConversation_imageView);
        mRecyclerView = findViewById(R.id.conversation_recyclerView);
    }

    private void initListeners() {
        mAddConversation.setOnClickListener(mListener);
    }

    private void initRecyclerView() {
        mConversationAdapter = new ConversationAdapter(mContext, mConversations, mConversationCallback);
        mRecyclerView.setAdapter(mConversationAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                } else {
                    closeAddConversationFragment();
                }
                break;
        }

        return true;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addConversation_imageView:
                startAddConversationFragment();
                break;
        }
    }

    @Override
    public void onUserCallback(User user) {
        final String idRoom = Util.getRandomUid();
        final Conversation conversationForUser = new Conversation(
                idRoom,
                user.getUid(),
                user.getAvatar(),
                user.getFirstName() + " " + user.getLastName(),
                "");

        mDatabase.readUser(new UserCallback() {
            @Override
            public void onUserCallback(User user) {
                Conversation conversationForFriend = new Conversation(
                        idRoom,
                        user.getUid(),
                        user.getAvatar(),
                        user.getFirstName() + user.getLastName(),
                        "");
                ArrayList<String> participants = new ArrayList<>();
                participants.add(conversationForFriend.getFriendUid());
                participants.add(conversationForUser.getFriendUid());

                Room room = new Room(idRoom, participants, new ArrayList<Map<String, String>>(), new HashMap<String, String>());
                mDatabase.writeRoom(new Database.AnswerWriting() {
                    @Override
                    public void onAnswer(boolean isWrite) {
                        if (isWrite) {
                            Toast.makeText(mContext, " Success ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, " Fail ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, room);
                mDatabase.writeConversation(new Database.AnswerWriting() {
                    @Override
                    public void onAnswer(boolean isWrite) {
                        if (isWrite) {
                            Toast.makeText(mContext, " Success ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, " Fail ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, conversationForUser, conversationForFriend);

            }
        }, mUid);

        updateConversationsInRecyclerView(conversationForUser);
    }

    @Override
    public void onConversationCallback(Conversation conversation) {
        startRoomActivity(conversation);
    }

    private void updateConversationsInRecyclerView(Conversation conversation) {
        mConversations.addLast(conversation);
        mConversationAdapter.notifyItemInserted(mConversations.size() + 1);
    }

    private void setConversationsInRecyclerView() {
        mDatabase.readAllConversationsFromSpecificUser(new Database.ConversationsCallback() {
            @Override
            public void onConversationsCallback(ArrayList<Conversation> conversations) {
                for (Conversation conversation : conversations) {
                    mConversations.addLast(conversation);
                    mConversationAdapter.notifyItemInserted(mConversations.size() + 1);
                }
            }
        }, mUid);
    }

    private void startAddConversationFragment() {
        AddConversationFragment addConversationFragment = AddConversationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.addConversation_frameLayout, addConversationFragment)
                .addToBackStack(null).commit();
    }

    private void closeAddConversationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        AddConversationFragment addConversationFragment =
                (AddConversationFragment) fragmentManager.findFragmentById(R.id.addConversation_frameLayout);

        if (addConversationFragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            getSupportFragmentManager().popBackStack();

            fragmentTransaction.remove(addConversationFragment).commit();
        }

    }

    private void startRoomActivity(@NotNull Conversation conversation) {
        Intent intent = new Intent(mContext, RoomActivity.class);
        intent.putExtra("idRoom", conversation.getIdRoom());
        intent.putExtra("friendUid", conversation.getFriendUid());
        intent.putExtra("avatar", conversation.getFriendUid());
        intent.putExtra("name", conversation.getName());
        intent.putExtra("lastMessage", conversation.getLastMessage());
        startActivity(intent);
    }
}