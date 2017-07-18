package com.example.bjtu.puzzle;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {
    private Button leftbut;
    private Button rightbut;
    public static List<Img>  Imglist=new ArrayList<>();
    private static int Difficulty=3;
    private static final int RESULT_CAMERA=100;
    private static final int RESULT_GALLERY=200;
    public static  String TEMP_IMAGE_PATH;
    public static  final String IMAGE_TYPE="image/*";
    private static int chosenImage=R.drawable.image4;
    private RecyclerView recyclerView;
    private File imageFile=null;
    public static int getChosenImage() {
        return chosenImage;
    }

    public static void setChosenImage(int chosenImage) {
        MainActivity.chosenImage = chosenImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                showDialogItem();
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
private static final String TAG = "MainActivity";
    private void showDialogItem(){
        /*
         *选择使用相机还是使用本地图库
         * 使用AlertDialog比较合适
         */
        TEMP_IMAGE_PATH= Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/";
        Log.e(TAG, "showDialogItem: "+TEMP_IMAGE_PATH );
        AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("请选择");
        dialog.setItems(new String[]{"使用相机", "本地图库"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //camera
                        long  time = Calendar.getInstance().getTimeInMillis();
                        Intent intent0=new Intent(("android.media.action.IMAGE_CAPTURE"));

                        File out =new File(TEMP_IMAGE_PATH);
                        if(!out.exists()){
                            out.mkdirs();
                        }
                        out=new File (TEMP_IMAGE_PATH,time+".jpg");
                        TEMP_IMAGE_PATH+=time+".jpg";
                        intent0.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(out));
                        Log.e(TAG, "onClick: PHOTO name "+ TEMP_IMAGE_PATH);
                        startActivityForResult(intent0,RESULT_CAMERA);
                        break;
                    case 1:
                        //gallery
                        if(ContextCompat.checkSelfPermission(MainActivity.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MainActivity.this,new
                            String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }else{
                            openAlbum();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        dialog.create().show();
    }
    private void openAlbum(){
        Intent intent1 =new Intent("android.intent.action.GET_CONTENT");
        intent1.setType(IMAGE_TYPE);
        startActivityForResult(intent1,RESULT_GALLERY);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1){
            if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openAlbum();
            }else{
               Toast.makeText(this,"你不允许调用相册",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("filePath", TEMP_IMAGE_PATH);
        Log.d(TAG, "onSaveInstanceState");
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (TextUtils.isEmpty(TEMP_IMAGE_PATH)) {
            TEMP_IMAGE_PATH= savedInstanceState.getString("filePath");
        }
        Log.d(TAG, "onRestoreInstanceState");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case RESULT_CAMERA:
                if(resultCode==RESULT_OK) {
                    imageFile = new File(TEMP_IMAGE_PATH);

                    Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                    intent.putExtra("Difficulty",this.Difficulty);

                    intent.putExtra("Picturepath",TEMP_IMAGE_PATH);

                    Log.e(TAG, "onActivityResult:Photo  name  "+ TEMP_IMAGE_PATH);
                    startActivity(intent);
                }
                break;
            case RESULT_GALLERY:
                if(resultCode==RESULT_OK) {
                    String imagePath =null;
                    Uri uri=data.getData();
                    if(DocumentsContract.isDocumentUri(this,uri)){
                        //如果是document类型的Uri，则通过document id 处理
                        String docId=DocumentsContract.getDocumentId(uri);
                        if("com.android.provider.media.documents".equalsIgnoreCase(uri.getAuthority())){
                            String id=docId.split(":")[1];
                            String selection=MediaStore.Images.Media._ID+"="+id;
                            imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
                        }else{

                        }
                    }else if("content".equalsIgnoreCase(uri.getScheme())){
                        //如果是content类型的Uri，则通过普通方式 处理
                        imagePath=getImagePath(uri,null);
                    }else if("file".equalsIgnoreCase(uri.getScheme())){
                        //如果是file类型的Uri，直接获取路径
                        imagePath=uri.getPath();
                    }
                    Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                    intent.putExtra("Difficulty",this.Difficulty);
                    intent.putExtra("Picturepath",imagePath);
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
    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
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
