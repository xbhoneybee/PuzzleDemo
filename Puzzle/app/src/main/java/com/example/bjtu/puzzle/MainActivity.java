package com.example.bjtu.puzzle;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button leftbut;
    private Button rightbut;
    public static List<Img>  Imglist=new ArrayList<>();
    private static int Difficulty=3;
    private static final int RESULT_CAMERA=100;
    private static final int RESULT_GALLERY=200;
    public static  String TEMP_IMAGE_PATH;
    public static  final String IMAGE_TYPE="image/*";
    private static int chosenImage=R.drawable.image1;
    private RecyclerView recyclerView;
    public static int getChosenImage() {
        return chosenImage;
    }

    public static void setChosenImage(int chosenImage) {
        MainActivity.chosenImage = chosenImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImage();
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        leftbut=(Button)findViewById(R.id.head_button_left);
        rightbut=(Button)findViewById(R.id.head_button_right);
        leftbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("关  于");
                dialog.setMessage("XXX工作室\ncopyright @2017|all right reserved.");
                dialog.setCancelable(true);
                dialog.show();
            }
        });

        rightbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //此处可以处理自选图片

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
//        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ImgAdapter adapter=new ImgAdapter(Imglist);
        recyclerView.setAdapter(adapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu,menu);
//        return true;
//    }

//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.id_help:
//                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
//                dialog.setTitle("关  于");
//                dialog.setMessage("XXX工作室\ncopyright @2017|all right reserved.");
//                dialog.setCancelable(true);
//                dialog.show();
//                break;
//            default:
//                break;
//        }
//        return true;
//    }
    //添加几个方法
    private void showDialogItem(){
        /*
         *选择使用相机还是使用本地图库
         * 使用AlertDialog比较合适
         */
        AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("请选择");
        dialog.setItems(new String[]{"使用相机", "本地图库"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //camera
                        Intent intent0 =new Intent("MediaStroe.IMAGE_CAPTURE");
                        long  time = Calendar.getInstance().getTimeInMillis();

                        intent0.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(TEMP_IMAGE_PATH+time+".jpg")));
                        startActivityForResult(intent0,RESULT_CAMERA);
                        break;
                    case 1:
                        //gallery
                        Intent intent1 =new Intent(Intent.CATEGORY_OPENABLE);
                        intent1.setType(IMAGE_TYPE);
                        startActivityForResult(intent1,RESULT_GALLERY);
                        break;
                    default:
                        break;
                }
            }
        });
        dialog.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case RESULT_CAMERA:
                if(resultCode==RESULT_OK) {
                    Intent intent=new Intent(MainActivity.this,Puzzle.class);
                    intent.putExtra("Difficulty",this.Difficulty);
                    intent.putExtra("Picture",TEMP_IMAGE_PATH);
                    startActivity(intent);
                }
                break;
            case RESULT_GALLERY:
                if(resultCode==RESULT_OK) {
                    Cursor cursor = this.getContentResolver().query(data.getData(), null, null, null, null);
                    cursor.moveToFirst();
                    String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                    Intent intent=new Intent(MainActivity.this,Puzzle.class);
                    intent.putExtra("Difficulty",this.Difficulty);
                    intent.putExtra("Picture",imagePath);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Bitmap 和Drawable 的转化
     */
    private Bitmap PathToBitmap(String path)  {
        Uri imguri=Uri.fromFile(new File(path));
        Bitmap bitmap= null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imguri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    private   Bitmap DrawableIdToBitmap(int id) {
        Drawable drawable= ContextCompat.getDrawable(this,id);
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public static Bitmap DrawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    //加图
//    public void add(ImageView img){
//        Imglist.add(0,img);
//
//    }


    //初始化图片
    public void initImage(){
        for(int i=0;i<1;i++){
            Img img1=new Img(R.drawable.image1);
            Imglist.add(img1);
            Img img2=new Img(R.drawable.image2);
            Imglist.add(img2);
            Img img3=new Img(R.drawable.image3);
            Imglist.add(img3);
            Img img4=new Img(R.drawable.image4);
            Imglist.add(img4);
            Img img5=new Img(R.drawable.image5);
            Imglist.add(img5);
            Img img6=new Img(R.drawable.image6);
            Imglist.add(img6);
            Img img7=new Img(R.drawable.image7);
            Imglist.add(img7);
            Img img8=new Img(R.drawable.image8);
            Imglist.add(img8);
            Img img9=new Img(R.drawable.image9);
            Imglist.add(img9);
        }
    }
}
