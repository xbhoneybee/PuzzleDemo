package com.example.bjtu.puzzle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by dl188
 * 实现选图后跳转
 */

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private static int Difficulty;
    private int imgid;
    private ImageView img;
    private Button but1;
    private Button but2;
    private Button but3;
    private String path;

    public static int getDifficulty(){
        return Difficulty;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dif);

        Intent intent=getIntent();
        imgid=intent.getIntExtra("Picture",R.drawable.image0);
        path=intent.getStringExtra("Picturepath");
        img = (ImageView) findViewById(R.id.dif_imageView);
        if(path==null) {
            img.setImageResource(imgid);
        }else{
            Bitmap tmpbitmap=BitmapFactory.decodeFile(path);
            img.setImageBitmap(tmpbitmap);
        }
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

    private void gotoPuzzle() {
        Intent intent=new Intent(Main2Activity.this,Puzzle.class);
        intent.putExtra("Difficulty",Difficulty);
        intent.putExtra("Picture",imgid);
        intent.putExtra("Picturepath",path);
        startActivity(intent);
    }
}
