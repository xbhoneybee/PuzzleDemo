package com.example.bjtu.puzzle;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

public class Puzzle extends AppCompatActivity implements View.OnClickListener {

    private int seconds=-1,steps=0;
    private TextView textsec,textsteps;
    private Button butgiveup,butrestart;
    int picture=R.drawable.image4;
    private Timer timer;
    private TimerTask timerTask;//计时器线程
    private ImageView sucImg;
    private View LinearView;
    private TextView suctext1;

    //重力感应修改开始标志
    private SensorManager mSensorManager = null;
    private Sensor mSensor = null;
    private float x, y, z;

    private static final String TAG = "Puzzle";
    private Bitmap picPuzzle;
    public static float Length;
    public static   int n;
    private  GameRule ruler;
    private GridView gridView;
    private ImagesUtil imagesUtil;

    //只和计时器有关
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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

        LinearView=(View)findViewById(R.id.puzzle_linear);
        sucImg=(ImageView)findViewById(R.id.puzzle_img);
        suctext1=(TextView)findViewById(R.id.puzzle_linear_text1);

        //重力传感器应用
        mSensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    //重力传感器监听
    SensorEventListener lsn = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        int picture =R.drawable.image4;
        picture=intent.getIntExtra("Picture",picture);

        sucImg.setImageResource(picture);
        n=intent.getIntExtra("Difficulty",2);
            String path=intent.getStringExtra("Picturepath");
        if(path==null) {
            Drawable tmpdrawable = ContextCompat.getDrawable(this, picture);
            picPuzzle = MainActivity.DrawableToBitmap(tmpdrawable);
        }else{
            picPuzzle=BitmapFactory.decodeFile(path);
        }

        ruler=new GameRule();
        initView();
    }


    private void initView(){
        DisplayMetrics myDisplayMetrics=ScreenUtil.getScreenSize(this);//获取屏幕参数
        Length= myDisplayMetrics.widthPixels-myDisplayMetrics.densityDpi/160*(50+3*(n-1));
        picPuzzle=ImagesUtil.resizeBitmap(Length,Length,picPuzzle);
        imagesUtil=new ImagesUtil(ruler);
        imagesUtil.createInitBitmaps(n,picPuzzle,this);
        gridView=(GridView)findViewById(R.id.gridView);
        gridView.setVisibility(View.VISIBLE);
        gridView.setNumColumns(n);
        gridView.setColumnWidth((int)Length/n);
        ruler.BoxGenerator();
        final GridAdapter myadapter=new GridAdapter(this,ruler.boxes);
        gridView.setAdapter(myadapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Box from=ruler.boxes.get(position);
                if(ruler.isChange(from,ruler.last))
                {
                    gridView.setBackgroundColor((int)((steps%2)*0xffFCFCFC));
                    steps++;
                    textsteps.setText(String.valueOf(steps));
                    //刷新界面，并判断完成否
                    myadapter.notifyDataSetChanged();//提示数据变化，刷新
                    if(ruler.isCompleted())
                    {
                        /*
                         *当拼图实现后
                         */
                        timer.cancel();
                        timerTask.cancel();
                        suctext1.setText("您用了：  "+String.valueOf(steps)+"步   "+String.valueOf(seconds)+" 秒   完成\n\n"+"我们对您的评价是：\n\n"+(seconds<60?"666666666666666666666666":"您弱的一P,请接受开发人员的嘲讽"));
                        LinearView.setVisibility(View.VISIBLE);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        };
                        handler.postDelayed(runnable, 2000);
                    }
                }
            }
        });

        seconds=-1;
        steps=0;
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
        textsec.setText("0 s");
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
                steps=0;
                textsteps.setText(String.valueOf(steps));
                timer.cancel();
                timerTask.cancel();
                onStart();
                break;
        }
    }
}
