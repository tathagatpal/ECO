package com.example.eco;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Cam extends AppCompatActivity {

    Button btnCapture, btnAddImage;
    ImageView imageDisplay;
    Intent myIntent;
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    private DatabaseReference database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        myIntent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        database = FirebaseDatabase.getInstance().getReference();
        btnCapture = findViewById(R.id.button2);
        imageDisplay = findViewById(R.id.imageCapture);
        btnAddImage = findViewById(R.id.button4);
        if(ContextCompat.checkSelfPermission(Cam.this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Cam.this,
                    new String[]{Manifest.permission.CAMERA}, 0);
        }

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Bitmap img = (Bitmap)data.getExtras().get("data");
            imageDisplay.setImageBitmap(img);
            ByteArrayOutputStream tempBaos = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 100, tempBaos);
            final byte[] imgFile = tempBaos.toByteArray();
            btnAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MarkerInfo mInfo = new MarkerInfo(myIntent.getDoubleExtra("lat", 0), myIntent.getDoubleExtra("lon", 0), myIntent.getStringExtra("uid"), myIntent.getStringExtra("name"),0,0);
                    DatabaseReference dbRef= database.push();
                    dbRef.setValue(mInfo);
                    String fName=dbRef.getKey() + ".jpg";
                    storage = storage.child(fName);
                    UploadTask imgUpload = storage.putBytes(imgFile);
                    imgUpload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            finish();
                        }
                    });
                }
            });
        }
    }

}

