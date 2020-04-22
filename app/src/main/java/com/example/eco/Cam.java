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

public class Cam extends AppCompatActivity {

    Button btnCapture, btnAddImage;
    ImageView imageDisplay;
    Intent myIntent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        myIntent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnCapture = findViewById(R.id.button2);
        imageDisplay = findViewById(R.id.imageCapture);

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
        }
    }

}

