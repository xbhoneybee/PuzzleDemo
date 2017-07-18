package com.example.bjtu.puzzle;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private static int Difficulty;
    private int imgid;
    private View view;
    private ImageView img;
    private Button but1;
    private Button but2;
    private Button but3;

    public static int getDifficulty(){
        return Difficulty;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dif);

        Intent intent=getIntent();
        imgid=intent.getIntExtra("Picture",R.drawable.image4);
        img=(ImageView)findViewById(R.id.dif_imageView);
        img.setImageResource(imgid);

        but1=(Button)findViewById(R.id.dif_button_1);
        but2=(Button)findViewById(R.id.dif_button_2);
        but3=(Button)findViewById(R.id.dif_button_3);
        img.setOnClickListener(this);
        but1.setOnClickListener(this);
        but2.setOnClickListener(this);
        but3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dif_imageView:
                finish();
                break;
            case R.id.dif_button_1:
                Difficulty=3;
                gotoPuzzle();
                break;
            case R.id.dif_button_2:
                Difficulty=4;
                gotoPuzzle();
                break;
            case R.id.dif_button_3:
                Difficulty=5;
                gotoPuzzle();
                break;
            default:
                break;
        }
    }

        public void gotoPuzzle() {
        Intent intent=new Intent(Main2Activity.this,Puzzle.class);
        intent.putExtra("Difficulty",Difficulty);
        intent.putExtra("Picture",imgid);
        startActivity(intent);
    }
}
