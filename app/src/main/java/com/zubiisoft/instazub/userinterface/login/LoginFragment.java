package com.zubiisoft.instazub.userinterface.login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zubiisoft.instazub.R;
import com.zubiisoft.instazub.interfaces.LoginListener;

/**
 *
 */
public class LoginFragment extends DialogFragment implements
        View.OnClickListener {

    public static final String TAG = "LoginFragment";

    private EditText mEmail;
    private EditText mPassword;
    private View mRootView;

    private LoginListener mEmailAndPasswordListener;
    private View.OnClickListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_login, container, false);

        initInstances();
        initListeners();

        // for tests
        mEmail.setText("zubii0@yahoo.com");
        mPassword.setText("123456789");

        return mRootView;
    }

    private void initInstances() {
        mListener = this;
        mEmail = mRootView.findViewById(R.id.emailPhone_editText);
        mPassword = mRootView.findViewById(R.id.password_editText);
    }

    private void initListeners() {
        mRootView.findViewById(R.id.login_button).setOnClickListener(mListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginListener) {
            mEmailAndPasswordListener = (LoginListener) context;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                loginWithEmailAndPassword();
                break;
        }
    }

    private void loginWithEmailAndPassword() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if (mEmailAndPasswordListener != null) {
            mEmailAndPasswordListener.onEmailAndPassword(email, password);
        }

        dismiss();
    }
}