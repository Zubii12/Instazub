package com.zubiisoft.instazub.userinterface.notifications;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zubiisoft.instazub.R;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class NotificationFragment extends Fragment {

    public NotificationFragment() {
    }


    @NotNull
    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }
}