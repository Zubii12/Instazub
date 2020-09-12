package com.zubiisoft.instazub.userinterface.register;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.R;

import com.zubiisoft.instazub.database.Database;
import com.zubiisoft.instazub.interfaces.LoginListener;
import com.zubiisoft.instazub.interfaces.RegisterListener;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class RegisterFragment extends DialogFragment implements
        View.OnClickListener{

    public static final String TAG = "RegisterFragment";

    private View mRootView;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmailPhone;
    private EditText mPassword;

    private RegisterListener mRegisterListener;
    private View.OnClickListener mListener;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_register, container, false);

        initInstances();
        initListeners();

        smecherie();

        return mRootView;
    }
    private void smecherie() {
        mFirstName.setText("Zubascu");
        mLastName.setText("Ionut");
        mEmailPhone.setText("zubii@yahoo.com");
        mPassword.setText("123456789");
    }
    private void initInstances() {
        mFirstName = mRootView.findViewById(R.id.firstName_editText);
        mLastName = mRootView.findViewById(R.id.lastName_editText);
        mEmailPhone  = mRootView.findViewById(R.id.emailPhone_editText);
        mPassword = mRootView.findViewById(R.id.password_editText);
        mListener = this;
    }

    private void initListeners() {
        mRootView.findViewById(R.id.register_button).setOnClickListener(mListener);
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

        if (context instanceof RegisterListener) {
            mRegisterListener = (RegisterListener) context;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_button:
                createUserWithEmailAndPassword();
                break;
        }
    }

    private void createUserWithEmailAndPassword() {
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String email = mEmailPhone.getText().toString();
        String password = mPassword.getText().toString();

        if (mRegisterListener != null) {
            mRegisterListener.onRegister(firstName, lastName, email, password);
        }

        dismiss();
    }
}