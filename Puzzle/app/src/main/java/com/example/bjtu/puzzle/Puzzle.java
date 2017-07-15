package com.example.bjtu.puzzle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class Puzzle extends AppCompatActivity {

    private static final String TAG = "Puzzle";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        Intent intent=getIntent();
        int picture =0,n=2;
        picture=intent.getIntExtra("Picture",picture);
        n=intent.getIntExtra("Difficulty",n);

        Log.e(TAG, "onCreate: "+n );

    }
}
