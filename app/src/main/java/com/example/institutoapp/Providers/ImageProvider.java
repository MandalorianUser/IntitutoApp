package com.example.institutoapp.Providers;

import android.content.Context;

import com.example.institutoapp.Utils.CompressorBitmapImage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class ImageProvider {
    StorageReference mStorage;

public ImageProvider() {
        mStorage = FirebaseStorage.getInstance().getReference();
        }
public UploadTask save(Context context, File file, String id){
        byte[] imageByte = CompressorBitmapImage.getImage(context,file.getPath(),300,300);
        StorageReference storage = mStorage.child(id+"jpg");
        mStorage = storage;
        UploadTask task = storage.putBytes(imageByte);
        return task;
        }

public StorageReference getStorage(){
        return mStorage;
        }


        }
