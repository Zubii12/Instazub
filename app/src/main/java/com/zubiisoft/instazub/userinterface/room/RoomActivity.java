package com.zubiisoft.instazub.userinterface.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.R;
import com.zubiisoft.instazub.constants.Constant;
import com.zubiisoft.instazub.database.Database;
import com.zubiisoft.instazub.model.Room;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RoomActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "RoomActivity";

    private Context mContext;
    private FirebaseAuth mAuth;
    private Database mDatabase;

    private LinkedList<ArrayList<String>> mMessages = new LinkedList<>();

    private RecyclerView mRecyclerView;
    private ImageView mAvatar;
    private TextView mName;
    private ImageView mInfoRoom;
    private EditText mMessage;
    private ImageView mSendMessage;

    private RoomAdapter mRoomAdapter;

    private View.OnClickListener mListener;

    private String mUid;
    private Room mRoom;
    private String mCurrentMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        initInstances();
        initListeners();
        initRecyclerView();
        getDataFromIntent();

        //setMessagesInRecyclerView();

        setMessagesListener();
    }

    private void initInstances() {
        mContext = this;
        mListener = this;
        mAuth = InstazubApplication.getFirebaseAuth();
        mDatabase = InstazubApplication.getDatabase();

        mUid = mAuth.getUid();
        mRecyclerView = findViewById(R.id.message_recyclerView);
        mAvatar = findViewById(R.id.avatar_imageView);
        mName = findViewById(R.id.name_textView);
        mInfoRoom = findViewById(R.id.info_room);
        mMessage = findViewById(R.id.message_editText);
        mSendMessage = findViewById(R.id.sendMessage_imageView);

        mMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCurrentMessage = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void initListeners() {
        mSendMessage.setOnClickListener(mListener);
    }

    private void initRecyclerView() {
        mRoomAdapter = new RoomAdapter(mContext, mMessages, mUid);
        mRecyclerView.setAdapter(mRoomAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // RecyclerView resizing.
        ((LinearLayoutManager)mRecyclerView.getLayoutManager()).setStackFromEnd(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + "Am ajuns");
        mDatabase.removeMessagesListenerRegistration();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendMessage_imageView:
                final ArrayList<String> messageToSent = new ArrayList<>();
                long millis = Calendar.getInstance().getTimeInMillis();

                messageToSent.add(String.valueOf(millis));
                messageToSent.add(mUid);
                messageToSent.add(mCurrentMessage);


                mDatabase.writeMessageAtSpecificRoom(new Database.AnswerWriting() {
                    @Override
                    public void onAnswer(boolean isWrite) {
                        // TODO handle answer.
                    }
                }, mRoom.getIdRoom(), messageToSent);

                mMessage.setText("");
                mCurrentMessage = "";
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        String idRoom = intent.getStringExtra("idRoom");
        String friendUid = intent.getStringExtra("friendUid");
        String avatar = intent.getStringExtra("avatar");
        String name = intent.getStringExtra("name");
        String lastMessage = intent.getStringExtra("lastMessage");

        mName.setText(name);


        ArrayList<String> participants = new ArrayList<>();
        participants.add(mUid);
        participants.add(friendUid);

        mRoom = new Room(idRoom, participants, new ArrayList<Map<String, String>>(),
                new HashMap<String, String>());
    }

    private void setMessagesInRecyclerView() {
        mDatabase.readRoom(new Database.RoomCallback() {
            @Override
            public void onRoomCallback(Room room) {
                for (Map<String, String> message : room.getMessages()) {
                    ArrayList<String> mess = new ArrayList<>();
                    Log.d(TAG, "onRoomCallback: " + message);

                    mess.add(message.get("millis"));
                    mess.add(message.get("sender"));
                    mess.add(message.get("message"));

                    mMessages.addLast(mess);
                    mRoomAdapter.notifyItemInserted(mMessages.size() + 1);
                }
                mMessages.remove(mMessages.size() - 1);
                mRoomAdapter.notifyItemRemoved(mMessages.size() - 1);
            }
        }, mRoom.getIdRoom());
    }

    private void setMessagesListener() {
        mDatabase.setMessagesListenerAtSpecificRoom(new Database.MessagesCallback() {
            @Override
            public void onMessagesCallback(ArrayList<ArrayList<String>> messages) {
                mMessages.clear();
                mRoomAdapter.notifyDataSetChanged();
                int i = 0;
                for (ArrayList<String> message : messages) {
                    mMessages.addLast(message);
                    mRoomAdapter.notifyItemInserted(mMessages.size());
                    i = i + 1;
                    if (i == 2) {
                        //break;
                    }
                }
            }
        }, mRoom.getIdRoom());
    }
}