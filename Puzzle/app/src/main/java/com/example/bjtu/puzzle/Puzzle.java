package com.example.bjtu.puzzle;

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Puzzle extends AppCompatActivity implements View.OnClickListener,SensorEventListener  {

    private int seconds=-1,steps=0;
    private TextView textsec,textsteps;
    private Button butgiveup,butrestart,butgravity;
    private Timer timer;
    private TimerTask timerTask;//计时器线程
    private ImageView sucImg;
    private View LinearView;
    private TextView suctext1;
    private int accu=6;
    private ProgressBar probar;
    //重力感应修改开始标志
    boolean graviton=false;
    private SensorManager mSensorManager = null;
    private Sensor mSensor = null;
    private float x, y, z;
    private long time;
    private int gravityclickedtimes=0;
    private TextView sensivity;
    private float x1=0,x2=0,y1=0,y2=0;
    private static final String TAG = "Puzzle";
    private Bitmap picPuzzle;
    public static float Length;
    public static   int n;
    private  GameRule ruler;
    private GridView gridView;
    private ImagesUtil imagesUtil;
    private  GridAdapter myadapter;
    private int screanhight;
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
        butgravity=(Button)findViewById(R.id.puzzle_but_3);
        butgravity.setOnClickListener(this);
        textsteps=(TextView)findViewById(R.id.puzzle_steps);
        textsec=(TextView)findViewById(R.id.puzzle_time);
        probar=(ProgressBar)findViewById(R.id.puzzle_probar) ;
        probar.setMax(10);
        probar.setProgress(accu);
        sensivity=(TextView)findViewById(R.id.puzzle_sensivity);
        sensivity.setText("左右滑动下方空白区域，调节灵敏度:"+String.valueOf(accu));

        LinearView=(View)findViewById(R.id.puzzle_linear);
        sucImg=(ImageView)findViewById(R.id.puzzle_img);
        suctext1=(TextView)findViewById(R.id.puzzle_linear_text1);
        //重力传感器应用
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(null==mSensorManager){
            Log.e(TAG, "onStart: device no support" );
        }
        Intent intent=getIntent();
        int picture =R.drawable.image0;
        picture=intent.getIntExtra("Picture",picture);
        n=intent.getIntExtra("Difficulty",2);
        sucImg.setImageResource(picture);
        String path=intent.getStringExtra("Picturepath");
        if(path==null) {
            Drawable tmpdrawable = ContextCompat.getDrawable(this, picture);
            picPuzzle = MainActivity.DrawableToBitmap(tmpdrawable);
        }else{
            picPuzzle=BitmapFactory.decodeFile(path);
        }
    }

    //重力传感器监听
    @Override
    public void onSensorChanged(SensorEvent sensor) {
        float nx = sensor.values[0];
        float ny = sensor.values[1];
        float nz = sensor.values[2];

        //计算出 X Y Z的数值下面可以根据这个数值来计算摇晃的速度了
        //速度 = 路程/时间
        //X轴的速度
        long newTime=System.currentTimeMillis();
        float speedX = (nx - x) / (newTime - time);
        //y轴的速度
        float speedY = (ny - y) / (newTime - time);
        //z轴的速度
        float speedZ = (nz - z) / (newTime - time);
        //这样简单的速度就可以计算出来，如果你想计算加速度也可以，在运动学里，加速度a与速度，
        //位移都有关系：Vt=V0+at，S=V0*t+1/2at^2， S=（Vt^2-V0^2）/(2a),根据这些信息也可以求解a
        x = nx;
        y = ny;
        z = nz;

            time = System.currentTimeMillis();
            int nowpos = ruler.last.getPosId()-1;
            if (x > 7 && speedX > 3e-2 ) {//点击左边
                int from = (nowpos - 1 + n * n) % (n * n);
                SimulationClick(from);
            } else if (x < -6 && speedX < -(4e-2) ) {//点击右边
                int from = (nowpos + 1) % (n * n);
                SimulationClick(from);
            } else if (y > 6 && speedY > 2e-2 ) {//点击下边
                int from = (nowpos + n ) % (n * n);
                SimulationClick(from);
            } else if (y < -5 && speedY < -(2e-2)) {//点击上边
                int from = (nowpos - n+n*n) % (n * n);
                SimulationClick(from);
            }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        ruler=new GameRule();
        initView();
    }

    private void initView(){
        DisplayMetrics myDisplayMetrics=ScreenUtil.getScreenSize(this);//获取屏幕参数
        Length= myDisplayMetrics.widthPixels-myDisplayMetrics.densityDpi/160*(50+3*(n-1));
        screanhight=myDisplayMetrics.heightPixels;
        picPuzzle=ImagesUtil.resizeBitmap(Length,Length,picPuzzle);
        imagesUtil=new ImagesUtil(ruler);
        imagesUtil.createInitBitmaps(n,picPuzzle,this);
        gridView=(GridView)findViewById(R.id.gridView);
        gridView.setVisibility(View.VISIBLE);
        gridView.setNumColumns(n);
        gridView.setColumnWidth((int)Length/n);
        ruler.BoxGenerator();
        myadapter=new GridAdapter(this,ruler.boxes);
        gridView.setAdapter(myadapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Box from=ruler.boxes.get(position);
                Log.e(TAG, "onItemClick: postion "+position );
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
                        suctext1.setText("您用了：  "+String.valueOf(steps)+"步   "+String.valueOf(seconds)+" 秒   完成\n\n"+"我们对您的评价是：\n\n"+(seconds<n*n*20?"666666666666666666666666":"您弱的一P,请接受开发人员的嘲讽"));
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
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            x2 = event.getX();
            y2 = event.getY();

            if( graviton&&y1>screanhight*0.88&&x2-x1>300){
                if(accu<10){
                    accu++;
                    probar.setProgress(accu);
                    sensivity.setText("灵敏度:"+String.valueOf(accu));
                    mSensorManager.unregisterListener(this,mSensor);
                    mSensorManager.registerListener( this,mSensor,(10-accu)*(5000)*SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
            if(graviton&&y1>screanhight*0.88&&x1-x2>300){
                if(accu>0){
                    accu--;
                    sensivity.setText("灵敏度:"+String.valueOf(accu));
                    probar.setProgress(accu);
                    mSensorManager.unregisterListener(this,mSensor);
                    mSensorManager.registerListener( this,mSensor,(10-accu)*(5000)*SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
            int nowpos = ruler.last.getPosId()-1;
            if((!graviton||(graviton&&y1<screanhight*0.86)) && x2-x1>100){//右
                int from = (nowpos + 1) % (n * n);
                SimulationClick(from);
            }else if((!graviton||(graviton&&y1<screanhight*0.86)) && x1-x2>100){//左
                int from = (nowpos - 1 + n * n) % (n * n);
                SimulationClick(from);
            }else if(y1-y2>100){//上
                int from = (nowpos - n+n*n) % (n * n);
                SimulationClick(from);
            }else if(y2-y1>100){//下
                int from = (nowpos + n ) % (n * n);
                SimulationClick(from);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.puzzle_but_1:
                //giveup
                Toast.makeText(this, "您放弃了游戏，请接受开发人员的嘲讽...", Toast.LENGTH_SHORT).show();
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
            case R.id.puzzle_but_3:
                if(gravityclickedtimes%2==0){
                    graviton=true;
                    mSensorManager.registerListener( this,mSensor,accu*(5000)*SensorManager.SENSOR_DELAY_NORMAL);
                    accu=6;
                    sensivity.setVisibility(View.VISIBLE);
                    probar.setVisibility(View.VISIBLE);
                    probar.setProgress(accu);
                    sensivity.setText("灵敏度:"+String.valueOf(accu));
                    Toast.makeText(this, "重力感应已开启，请将手机放平开始游戏", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "滑动下方控制条控制灵敏度", Toast.LENGTH_SHORT).show();
                }else{
                    graviton=false;
                    mSensorManager.unregisterListener(this);
                    probar.setVisibility(View.GONE);
                    sensivity.setVisibility(View.GONE);
                    Toast.makeText(this, "重力感应已关闭", Toast.LENGTH_SHORT).show();
                }
                gravityclickedtimes++;
                break;
        }
    }
    private  void SimulationClick(int frompos) {
        Box from = ruler.boxes.get(frompos);
        if (ruler.isChange(from, ruler.last)) {
            gridView.setBackgroundColor((int) ((steps % 2) * 0xffFCFCFC));
            steps++;
            textsteps.setText(String.valueOf(steps));
            //刷新界面，并判断完成否
            myadapter.notifyDataSetChanged();//提示数据变化，刷新
            if (ruler.isCompleted()) {
                        /*
                         *当拼图实现后
                         */
                timer.cancel();
                timerTask.cancel();
                suctext1.setText("您用了：  " + String.valueOf(steps) + "步   " + String.valueOf(seconds) + " 秒   完成\n\n" + "我们对您的评价是：\n\n" + (seconds < n*n*15 && steps< n*n*5 ? "666666666666666666666666" : "您弱的一P,请接受开发人员的嘲讽"));
                LinearView.setVisibility(View.VISIBLE);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                };
                handler.postDelayed(runnable, 3000);
            }
        }
    }
}
