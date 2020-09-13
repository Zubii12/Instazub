package com.zubiisoft.instazub.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.interfaces.UserCallback;
import com.zubiisoft.instazub.model.Conversation;
import com.zubiisoft.instazub.model.Room;
import com.zubiisoft.instazub.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private static final String TAG = "Database";

    // Collections
    private static final String USERS = "users";
    private static final String ROOMS = "rooms";

    // Fields in Document "users"
    private static final String FRIENDS = "friends";
    private static final String CONVERSATIONS = "conversations";

    // Fields in Document "rooms"
    private static final String MESSAGES = "messages";


    private static final boolean TRUE = true;
    private static final boolean FALSE = false;

    private static ListenerRegistration mListenerRegistration;

    private static Database sInstance;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private String mUid;

    private Database() {
        mDatabase = InstazubApplication.getFirebaseFirestore();
        mAuth = InstazubApplication.getFirebaseAuth();
        mUid = mAuth.getUid();
    }

    public static Database getInstance() {
        if (sInstance == null) {
            synchronized (Database.class) {
                if (sInstance == null) {
                    sInstance = new Database();
                }
            }
        }
        return sInstance;
    }

    public interface AnswerWriting {
        void onAnswer(boolean isWrite);
    }

    public interface UsersCallback {
        void onUsersCallback(ArrayList<User> users);
    }

    public interface UsersUidCallback {
        void onUsersUidCallback(ArrayList<String> uidList);
    }

    public interface ConversationsCallback {
        void onConversationsCallback(ArrayList<Conversation> conversations);
    }

    public interface RoomCallback {
        void onRoomCallback(Room room);
    }

    public interface MessageCallback {
        void onMessageCallback(ArrayList<String> message);
    }

    public interface MessagesCallback {
        void onMessagesCallback(ArrayList<ArrayList<String>> messages);
    }

    public void writeUser(final AnswerWriting answerWriting,@NotNull User user) {
        mDatabase.collection(USERS).document(user.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                answerWriting.onAnswer(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                answerWriting.onAnswer(false);
            }
        });
    }

    public void writeRoom(final AnswerWriting answerWriting, @NotNull Room room) {
        mDatabase.collection(ROOMS).document(room.getIdRoom()).set(room).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                answerWriting.onAnswer(TRUE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                answerWriting.onAnswer(FALSE);
            }
        });
    }

    public void writeFriend(final AnswerWriting answerWriting, String friendUid) {

        WriteBatch batch = mDatabase.batch();

        DocumentReference userReference = mDatabase.collection(USERS).document(mUid);
        batch.update(userReference, FRIENDS, FieldValue.arrayUnion(friendUid));

        DocumentReference friendReference = mDatabase.collection(USERS).document(friendUid);
        batch.update(friendReference, FRIENDS, FieldValue.arrayUnion(mUid));

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                answerWriting.onAnswer(TRUE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                answerWriting.onAnswer(FALSE);
            }
        });
    }

    public void writeConversation(final AnswerWriting answerWriting, Conversation conversationForUser, @NotNull Conversation conversationForFriend) {
        WriteBatch batch = mDatabase.batch();

        DocumentReference userReference = mDatabase.collection(USERS).document(conversationForFriend.getFriendUid());
        batch.update(userReference, CONVERSATIONS, FieldValue.arrayUnion(conversationForUser));

        DocumentReference friendReference = mDatabase.collection(USERS).document(conversationForUser.getFriendUid());
        batch.update(friendReference, CONVERSATIONS, FieldValue.arrayUnion(conversationForFriend));

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                answerWriting.onAnswer(TRUE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                answerWriting.onAnswer(FALSE);
            }
        });
    }

    public void writeMessageAtSpecificRoom(final AnswerWriting answerWriting, String idRoom, @NotNull ArrayList<String> message) {

        Map<String, String> map = new HashMap<>();
        map.put("millis", message.get(0));
        map.put("sender", message.get(1));
        map.put("message", message.get(2));

        mDatabase.collection(ROOMS).document(idRoom).update(MESSAGES, FieldValue.arrayUnion(map)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                answerWriting.onAnswer(TRUE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                answerWriting.onAnswer(FALSE);
            }
        });
    }

    public void setMessagesListenerAtSpecificRoom(final MessagesCallback messagesCallback, final String idRoom) {
        mListenerRegistration = mDatabase.collection(ROOMS).document(idRoom).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value != null && value.exists()) {
                    Room room = value.toObject(Room.class);
                    ArrayList<ArrayList<String>> mess = new ArrayList<>();

                    for (Map<String, String> map : room.getMessages()) {
                        ArrayList<String> _mess = new ArrayList<>();
                        _mess.add(map.get("millis"));
                        _mess.add(map.get("sender"));
                        _mess.add(map.get("message"));
                        mess.add(_mess);
                    }
                    messagesCallback.onMessagesCallback(mess);
                }
            }
        });
        //registration.remove();
    }

    public void removeMessagesListenerRegistration() {
        mListenerRegistration.remove();
        mListenerRegistration = null;
    }

    public void readUser(final UserCallback userCallback, final String uid) {
        mDatabase.collection(USERS).document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    User user = document.toObject(User.class);
                    userCallback.onUserCallback(user);
                } else {
                    // TODO
                }
            }
        });
    }

    public void readRoom(final RoomCallback roomCallback, final String idRoom) {
        mDatabase.collection(ROOMS).document(idRoom).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Room room = document.toObject(Room.class);
                    roomCallback.onRoomCallback(room);
                } else {
                    // TODO
                }
            }
        });
    }

    public void readAllUser(final UsersCallback usersCallback) {
        mDatabase.collection(USERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<User> users = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        if (!mUid.equals(user.getUid())) {
                            users.add(user);
                        }
                    }
                    Log.d(TAG, "onComplete: " + users);
                    usersCallback.onUsersCallback(users);

                } else {
                    // TODO
                }
            }
        });
    }

    public void readAllFriendsUidFromSpecificUser(final UsersUidCallback usersUidCallback, final String uid) {
        mDatabase.collection(USERS).document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> uidList = new ArrayList<>();
                    DocumentSnapshot document = task.getResult();
                    User user = document.toObject(User.class);

                    uidList.addAll(user.getFriends());

                    usersUidCallback.onUsersUidCallback(uidList);
                } else {
                    // TODO
                }
            }
        });

    }

    public void readAllConversationsFromSpecificUser(final ConversationsCallback conversationsCallback, final String uid) {
        mDatabase.collection(USERS).document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Conversation> conversations = new ArrayList<>();
                    DocumentSnapshot document = task.getResult();
                    User user = document.toObject(User.class);

                    if (user.getConversations() != null) {
                        conversations.addAll(user.getConversations());
                    }

                    conversationsCallback.onConversationsCallback(conversations);
                } else {
                    // TODO
                }
            }
        });
    }
}
