package com.example.eco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MarkerData extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mIntent = getIntent();
        setContentView(R.layout.markerdata);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width* 0.65) ,(int) (height * 0.65));
        TextView tv1 = (TextView) findViewById(R.id.t1);
        String str = "Created by: ";
        str = str + mIntent.getStringExtra("name") + "\nRating = " + mIntent.getIntExtra("rating",0);
        tv1.setText(str);
    }
}
