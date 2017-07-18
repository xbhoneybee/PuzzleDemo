package com.example.bjtu.puzzle;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.GONE;

public class Puzzle extends AppCompatActivity implements View.OnClickListener {

    private int seconds=0,steps=0;
    private TextView textsec,textsteps;
    private Button butgiveup,butrestart;
    int picture=R.drawable.image1;
    private Timer timer;
    private TimerTask timerTask;//计时器线程

    private static final String TAG = "Puzzle";
    private Bitmap picPuzzle;
    public static float Length;
    public static   int n;
    private  GameRule ruler;
    private GridView gridView;
    private ImagesUtil imagesUtil;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 更新计时器
                    seconds++;
                    textsec.setText(String.valueOf(seconds)+" s");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        butgiveup=(Button)findViewById(R.id.puzzle_but_1);
        butgiveup.setOnClickListener(this);
        butrestart=(Button)findViewById(R.id.puzzle_but_2);
        butrestart.setOnClickListener(this);
        textsteps=(TextView)findViewById(R.id.puzzle_steps);
        textsec=(TextView)findViewById(R.id.puzzle_time);

        Intent intent=getIntent();
<<<<<<< HEAD
        picture=intent.getIntExtra("Picture",picture);
        n=intent.getIntExtra("Difficulty",2);
        Log.e(TAG, "onCreate: "+n );


    }

    @Override
    protected void onStart() {
        super.onStart();
=======
        int picture =R.drawable.image4;
        picture=intent.getIntExtra("Picture",picture);
        n=intent.getIntExtra("Difficulty",2);
        //Log.e(TAG, "onCreate: "+n );
>>>>>>> f58aaf5185e4bc685d873b88c665333df1618990
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

        gridView=(GridView)findViewById(R.id.gridView);
        gridView.setVisibility(View.VISIBLE);
        gridView.setNumColumns(n);
        gridView.setColumnWidth((int)Length/n);
        gridView.setHorizontalSpacing(3);
        gridView.setVerticalSpacing(3);
        ruler.BoxGenerator();
        final GridAdapter myadapter=new GridAdapter(this,ruler.boxes);
        gridView.setAdapter(myadapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Box from=ruler.boxes.get(position);
                if(ruler.isChange(from,ruler.last))
                {
                    steps++;
                    textsteps.setText(String.valueOf(steps));
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

        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(timerTask, 0, 1000);
        textsec.setText("0s");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.puzzle_but_1:
                //giveup
                finish();
                break;
            case R.id.puzzle_but_2:
                //restart
                onStart();
                break;
        }
    }
}
