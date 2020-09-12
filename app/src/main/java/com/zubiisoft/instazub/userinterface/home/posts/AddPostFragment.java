package com.zubiisoft.instazub.userinterface.home.posts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zubiisoft.instazub.R;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class AddPostFragment extends Fragment {

    public AddPostFragment() { }

    @NonNull
    public static AddPostFragment newInstance() {
        return new AddPostFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_post, container, false);
    }
}