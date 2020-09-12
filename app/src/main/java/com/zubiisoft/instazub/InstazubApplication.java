package com.zubiisoft.instazub;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zubiisoft.instazub.database.Database;

import org.jetbrains.annotations.NotNull;

/**
 * Android Application class. Used for accessing singletons.
 */
public class InstazubApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @NotNull
    public static FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @NonNull
    public static FirebaseFirestore getFirebaseFireStore() {
        return FirebaseFirestore.getInstance();
    }

    @NonNull
    public static Database getDatabase() {
        return Database.getInstance();
    }
}
