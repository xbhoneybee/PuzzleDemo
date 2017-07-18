package com.example.bjtu.puzzle;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
    private ImagesUtil imagesUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        Intent intent=getIntent();
        int picture =R.drawable.image1;
        picture=intent.getIntExtra("Picture",picture);
        n=intent.getIntExtra("Difficulty",2);
        Log.e(TAG, "onCreate: "+n );
        Drawable tmpdrawable= ContextCompat.getDrawable(this,picture);
        picPuzzle=MainActivity.DrawableToBitmap(tmpdrawable);
        ruler=new GameRule();
        initView();
    }

    private void initView(){
        Log.e(TAG,"原图heigt   "+picPuzzle.getHeight());
        Log.e(TAG,"原图width   "+picPuzzle.getWidth());
        DisplayMetrics myDisplayMetrics=ScreenUtil.getScreenSize(this);
        Length=myDisplayMetrics.widthPixels;
        picPuzzle=ImagesUtil.resizeBitmap(Length,Length,picPuzzle);
        Log.e(TAG,"length   "+Length);
        Log.e(TAG,"Screen xdpi   "+myDisplayMetrics.xdpi);
        Log.e(TAG,"Screen  ydpi  "+myDisplayMetrics.ydpi);
        Log.e(TAG,"Screen width   "+myDisplayMetrics.widthPixels);
        Log.e(TAG,"Screen height   "+(int)myDisplayMetrics.heightPixels);
        Log.e(TAG,"Pic width   "+picPuzzle.getWidth());
        imagesUtil=new ImagesUtil(ruler);
        imagesUtil.createInitBitmaps(n,picPuzzle,this);
        Log.e(TAG,"boxes   1 w"+ruler.boxes.get(0).getBitmap().getWidth());
        Log.e(TAG,"boxes   1 h"+ruler.boxes.get(0).getBitmap().getHeight());


//        ImageView image=(ImageView)findViewById(R.id.image);
//        image.setImageBitmap(picPuzzle);
        /*try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
//        image.setVisibility(GONE);
        gridView=(GridView)findViewById(R.id.gridView);
        gridView.setVisibility(View.VISIBLE);
        gridView.setNumColumns(n);
        gridView.setColumnWidth((int)Length/n);
        gridView.setHorizontalSpacing(0);
        gridView.setVerticalSpacing(5);
        ruler.BoxGenerator();
        final GridAdapter myadapter=new GridAdapter(this,ruler.boxes);
        gridView.setAdapter(myadapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Box from=ruler.boxes.get(position);
                if(ruler.isChange(from,ruler.last))
                {
                    //刷新界面，并判断完成否
                    myadapter.notifyDataSetChanged();//提示数据变化，刷新
                    if(ruler.isCompleted())
                    {
                        /*
                         *当拼图实现后
                         */
                        Log.e(TAG, "onItemClick: wcccccccccc" );
                        finish();
                    }
                }
            }
        });
    }

}
