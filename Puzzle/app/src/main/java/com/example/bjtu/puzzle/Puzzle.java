package com.example.bjtu.puzzle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class Puzzle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        Intent intent=getIntent();
        int picture =0,n=0;
        picture=intent.getIntExtra("Picture",picture);
        n=intent.getIntExtra("Difficulty",n);
        ImageView imageView=(ImageView) findViewById(R.id.pictureView);
        imageView.setImageResource(picture);
    }
}
