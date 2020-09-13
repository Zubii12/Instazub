package com.zubiisoft.instazub.userinterface.posts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zubiisoft.instazub.InstazubApplication;
import com.zubiisoft.instazub.R;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import static android.app.Activity.RESULT_OK;

/**
 *
 */
public class AddPostFragment extends Fragment implements
        View.OnClickListener,
        AddPostAdapter.ImageCallback {

    private static final String TAG = "AddPostFragment";
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 1;

    private View mRootView;
    private Context mContext;
    private Activity mActivity;

    private RecyclerView mRecyclerView;
    private TextView mGallery;
    private TextView mImage;
    private TextView mVideo;

    private AddPostAdapter mAdapter;
    private LinkedList<String> mImages = new LinkedList<>();

    private View.OnClickListener mListener;

    private boolean mCanReadExternalStorage;

    private AddPostAdapter.ImageCallback mImageCallback;

    public AddPostFragment() { }

    @NonNull
    public static AddPostFragment newInstance() {
        return new AddPostFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_add_post, container, false);

        initInstances();
        initListeners();
        initRecyclerView();

        return mRootView;
    }

    private void initInstances() {
        mListener = this;
        mGallery = mRootView.findViewById(R.id.gallery_textView);
        mImage = mRootView.findViewById(R.id.image_textView);
        mVideo = mRootView.findViewById(R.id.video_textView);

        mRecyclerView = mRootView.findViewById(R.id.newPost_recyclerView);

        mImageCallback = this;
    }

    private void initListeners() {
        mGallery.setOnClickListener(mListener);
        mImage.setOnClickListener(mListener);
        mVideo.setOnClickListener(mListener);
    }

    private void initRecyclerView() {
        mAdapter = new AddPostAdapter(mContext, mImages, mImageCallback);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        mActivity = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        getReadExternalStoragePermission();
        if (mCanReadExternalStorage) {
            mImages.addAll(getAllShownImagesPath(mActivity));
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    mCanReadExternalStorage = true;
                } else {
                    mCanReadExternalStorage = false;
                    Toast.makeText(mContext, "Permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gallery_textView:
                setImagesInRecyclerView();
                break;
            case R.id.image_textView:
                openImageCamera();
                break;
            case R.id.video_textView:
                openVideoCamera();
                break;
        }
    }

    @Override
    public void onImageCallback(ImageView image, String path) {
        // TODO mDatabase.writePost
        try {
            storageFirebase(image, path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void storageFirebase(@NotNull ImageView imageView, String path) throws FileNotFoundException {
        FirebaseStorage storage = InstazubApplication.getFirebaseStorage();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child("mountains.jpg");

        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

        // ---------------------------------------------------------------------------------

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO
            }
        });

        // ---------------------------------------------------------------------------------

        //InputStream stream = new FileInputStream(new File("path/to/images/rivers.jpg"));

        InputStream stream = new FileInputStream(new File(path));

        uploadTask = mountainsRef.putStream(stream);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO
            }
        });

        // ---------------------------------------------------------------------------------]

        Uri file = Uri.fromFile(new File(path));
        StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());
        uploadTask = riversRef.putFile(file);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void setImagesInRecyclerView() {
        if (mCanReadExternalStorage) {
            mImages.addAll(getAllShownImagesPath(mActivity));
            mAdapter.notifyDataSetChanged();
        }
    }

    private void openImageCamera() {

    }

    private void openVideoCamera() {

    }

    @NotNull
    private LinkedList<String> getAllShownImagesPath(@NotNull Activity activity) {
        Uri uri;
        Cursor cursor;
        int columnIndexData = 0, columnIndexFolderName;
        LinkedList<String> listOfAllImages = new LinkedList<>();
        String absolutePathOfImage;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String data = MediaStore.MediaColumns.DATA;
        String name = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            name = MediaStore.Images.Media.BUCKET_DISPLAY_NAME;
        } else {
            name = MediaStore.Images.Media.DISPLAY_NAME;
        }

        String[] projection = { data, name };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);


        if (cursor != null) {
            columnIndexData = cursor.getColumnIndexOrThrow(data);
            columnIndexFolderName = cursor.getColumnIndexOrThrow(name);
        }
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    absolutePathOfImage = cursor.getString(columnIndexData);
                    listOfAllImages.addLast(absolutePathOfImage);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listOfAllImages;
    }

    private void getReadExternalStoragePermission() {
        if (ActivityCompat.checkSelfPermission
                (mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
        } else {
            mCanReadExternalStorage = true;
        }
    }
}