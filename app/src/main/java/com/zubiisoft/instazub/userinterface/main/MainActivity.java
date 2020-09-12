package com.zubiisoft.instazub.userinterface.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.R;
import com.zubiisoft.instazub.constants.Constant;
import com.zubiisoft.instazub.userinterface.home.HomeActivity;
import com.zubiisoft.instazub.userinterface.login.LoginActivity;
import com.zubiisoft.instazub.userinterface.register.RegisterActivity;

/**
 *
 */
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{

    private static final String TAG = "MainActivity";

    private Context mContext;

    private FirebaseAuth mAuth;

    private View.OnClickListener mListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances();
        initListeners();
    }

    private void initInstances() {
        mContext = this;
        mListener = this;
        mAuth = InstazubApplication.getFirebaseAuth();
    }

    private void initListeners() {
        findViewById(R.id.login_button).setOnClickListener(mListener);
        findViewById(R.id.register_button).setOnClickListener(mListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            startHomeActivity();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                startLoginActivity();
                break;
            case R.id.register_button:
                startRegisterActivity();
                break;
        }
    }

    private void startLoginActivity() {
        startActivity(new Intent(mContext, LoginActivity.class));
    }

    private void startRegisterActivity() {
        startActivity(new Intent(mContext, RegisterActivity.class));
    }

    private void startHomeActivity() {
        startActivity(new Intent(mContext, HomeActivity.class));
    }
}