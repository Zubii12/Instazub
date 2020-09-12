package com.zubiisoft.instazub.userinterface.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.R;

import com.zubiisoft.instazub.interfaces.LoginListener;
import com.zubiisoft.instazub.userinterface.home.HomeActivity;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        LoginListener {

    private static final String TAG = "LoginActivity";

    private Context mContext;
    private FirebaseAuth mAuth;
    private View.OnClickListener mListener;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initInstances();
        initListeners();
    }

    private void initInstances() {
        mContext = this;
        mListener = this;
        mActivity = this;
        mAuth = InstazubApplication.getFirebaseAuth();
    }

    private void initListeners() {
        findViewById(R.id.emailLogin_relativeLayout).setOnClickListener(mListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emailLogin_relativeLayout:
                displayLoginFragment();
                break;
            case R.id.googleLogin_relativeLayout:
                // TODO:
                break;
        }
    }

    @Override
    public void onEmailAndPassword(String email, String password) {
        loginWithEmailAndPassword(email, password);
    }

    private void displayLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.show(getSupportFragmentManager(), LoginFragment.TAG);
    }

    private void loginWithEmailAndPassword(final String email,final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            Log.d(TAG, "onComplete: ");
                            startHomeActivity();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: " + "rau");
                        }

                        // ...
                    }
                });
    }

    private void startHomeActivity() {
        startActivity(new Intent(mContext, HomeActivity.class));
    }

}