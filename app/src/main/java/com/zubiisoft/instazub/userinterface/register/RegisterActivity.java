package com.zubiisoft.instazub.userinterface.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.R;
import com.zubiisoft.instazub.constants.Constant;
import com.zubiisoft.instazub.database.Database;
import com.zubiisoft.instazub.interfaces.RegisterListener;
import com.zubiisoft.instazub.model.Conversation;
import com.zubiisoft.instazub.model.User;
import com.zubiisoft.instazub.userinterface.home.HomeActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements
        View.OnClickListener,
        RegisterListener {

    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private View.OnClickListener mListener;
    private FirebaseAuth mAuth;
    private Activity mActivity;
    private Database mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initInstances();
        initListeners();
    }

    private void initInstances() {
        mContext = this;
        mListener = this;
        mActivity = this;
        mAuth = InstazubApplication.getFirebaseAuth();
        mDatabase = InstazubApplication.getDatabase();
    }

    private void initListeners() {
        findViewById(R.id.emailRegister_relativeLayout).setOnClickListener(mListener);
        findViewById(R.id.googleRegister_relativeLayout).setOnClickListener(mListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emailRegister_relativeLayout:
                displayEmailRegisterFragment();
                break;
            case R.id.googleRegister_relativeLayout:
                //TODO :
                break;
        }
    }

    @Override
    public void onRegister(String firstName, String lastName, String email, String password) {
        createUserWithEmailAndPassword(firstName, lastName, email, password);
    }

    private void displayEmailRegisterFragment() {
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.show(getSupportFragmentManager(), RegisterFragment.TAG);
    }

    private void createUserWithEmailAndPassword(final String firstName, final String lastName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(mContext, "Authentication successful",
                                    Toast.LENGTH_SHORT).show();

                            writeUserInDatabase(firstName, lastName, user.getUid(), user.getEmail());
                            startHomeActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void startHomeActivity() {
        startActivity(new Intent(mContext, HomeActivity.class));
    }

    private void writeUserInDatabase(String firstName, String lastName, String uid, String email) {
        Date currentTime = Calendar.getInstance().getTime();
        ArrayList<String> friends = new ArrayList<>();
        ArrayList<Conversation> conversations = new ArrayList<>();

        User user = new User(uid,
                firstName,
                lastName,
                "",
                currentTime.toString(),
                email,
                "",
                friends,
                conversations);

        mDatabase.writeUser(new Database.AnswerWriting() {
            @Override
            public void onAnswer(boolean isWrite) {
                if (isWrite) {
                    Toast.makeText(mContext, "Registration successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, user);
    }
}