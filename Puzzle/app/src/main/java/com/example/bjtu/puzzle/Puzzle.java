package com.example.bjtu.puzzle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import static android.view.View.GONE;

public class Puzzle extends AppCompatActivity  {

    private static final String TAG = "Puzzle";
    private Bitmap picPuzzle;
    public static float Length;
    public static   int n;
    private  GameRule ruler;
    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        Intent intent=getIntent();
        int picture =0;
        picture=intent.getIntExtra("Picture",picture);
        n=intent.getIntExtra("Difficulty",2);
        Log.e(TAG, "onCreate: "+n );
        Drawable tmpdrawable= ContextCompat.getDrawable(this,picture);
        picPuzzle=MainActivity.DrawableToBitmap(tmpdrawable);
        initView();
    }

    private void initView(){
        Log.e(TAG,"原heigt   "+picPuzzle.getHeight());
        Log.e(TAG,"原width   "+picPuzzle.getWidth());
        DisplayMetrics myDisplayMetrics=ScreenUtil.getScreenSize(this);
        Length=myDisplayMetrics.xdpi;
        ImagesUtil.resizeBitmap(Length,Length,picPuzzle);
        Log.e(TAG,"length   "+Length);
        ImagesUtil.createInitBitmaps(n,picPuzzle,this);
        //ruler.BoxGenerator();//Runtime Error
        ImageView image=(ImageView)findViewById(R.id.image);
        image.setImageBitmap(picPuzzle);
        /*try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        image.setVisibility(GONE);
        gridView=(GridView)findViewById(R.id.gridView);
        gridView.setVisibility(View.VISIBLE);
        gridView.setNumColumns(n);
        gridView.setColumnWidth((int)Length/n);
        gridView.setHorizontalSpacing(0);
        gridView.setVerticalSpacing(0);
        GridAdapter myadapter=new GridAdapter(this,GameRule.boxes);
        gridView.setAdapter(myadapter);
       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Puzzle.this,"position "+position,Toast.LENGTH_SHORT).show();
            }
        });
    }


}
