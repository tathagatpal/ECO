package com.example.eco;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MarkerData extends Activity {


    StorageReference storage = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mIntent = getIntent();
        setContentView(R.layout.markerdata);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        final ImageView imgv = (ImageView) findViewById(R.id.img);
        getWindow().setLayout((int) (width* 0.65) ,(int) (height * 0.65));
        TextView tv1 = (TextView) findViewById(R.id.t1);
        String str = "Created by: ";
        str = str + mIntent.getStringExtra("name") + "\nRating = " + mIntent.getIntExtra("rating",0);
        tv1.setText(str);
        storage = storage.child(mIntent.getStringExtra("fName"));
        storage.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                imgv.setImageBitmap(bmp);
            }
        });
    }
}
