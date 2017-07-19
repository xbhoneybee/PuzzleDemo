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
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private Button leftbut;
    private Button rightbut;
    public static List<Img>  Imglist=new ArrayList<>();
    private static int Difficulty=3;
    private static final int RESULT_CAMERA=100;
    private static final int RESULT_GALLERY=200;
    private static final int RESULT_SCREENSHOT=300;
    private static String ScreenShotPath;
    public static  String CAMERA_IMAGE_PATH;
    private static final int output_X=600;
    private static final int output_Y=600;
    public static  final String IMAGE_TYPE="image/*";
    private static int chosenImage=R.drawable.image4;
    private RecyclerView recyclerView;
    private File GalleryimageFile=null;
    public static File PathFile;

    public MainActivity() throws FileNotFoundException {
    }

    public static void setChosenImage(int chosenImage) {
        MainActivity.chosenImage = chosenImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PathFile=new File(Environment.getExternalStorageDirectory()+"/PuzzleScreenShot");
        if(!PathFile.exists())
            PathFile.mkdirs();
                PathFile=new File(PathFile.getPath()+"/allSelectedPicPath.txt");
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
                dialog.setMessage("十石石石工作室\ncopyright @2017|all right reserved.");
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
    protected void onStop(){
        super.onStop();
        Imglist.clear();
    }
    @Override
    protected void onStart() {
        super.onStart();
        try {
            initImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ImgAdapter adapter=new ImgAdapter(Imglist);
        recyclerView.setAdapter(adapter);
    }
    private void showDialogItem(){
        /*
         *选择使用相机还是使用本地图库
         * 使用AlertDialog比较合适
         */
        CAMERA_IMAGE_PATH= Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/";
        AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("请选择");
        dialog.setItems(new String[]{"使用相机", "本地图库"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //camera
                        long  time = Calendar.getInstance().getTimeInMillis();
                        File out =new File(CAMERA_IMAGE_PATH);
                        if(!out.exists()){
                            out.mkdirs();
                        }
                        out=new File (CAMERA_IMAGE_PATH,time+".jpg");
                        CAMERA_IMAGE_PATH+=time+".jpg";
                        Intent intent0=new Intent(("android.media.action.IMAGE_CAPTURE"));
                        intent0.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(out));
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

    private static final String TAG = "MainActivity";
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("filePath",CAMERA_IMAGE_PATH);
        Log.d(TAG, "onSaveInstanceState:");
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (TextUtils.isEmpty(CAMERA_IMAGE_PATH)) {
            CAMERA_IMAGE_PATH= savedInstanceState.getString("filePath");
        }
        Log.d(TAG, "onRestoreInstanceState");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case RESULT_CAMERA:
                if(resultCode==RESULT_OK) {
                    GalleryimageFile = new File(CAMERA_IMAGE_PATH);
                    cropRawPhoto(Uri.fromFile(GalleryimageFile));
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
                    cropRawPhoto((Uri.fromFile(new File(imagePath))));
                }
                break;
            case RESULT_SCREENSHOT:
                if(resultCode==RESULT_OK){
                        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                        intent.putExtra("Difficulty",this.Difficulty);
                        intent.putExtra("Picturepath",ScreenShotPath);
                        try {
                            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
                            FileWriter writer = new FileWriter(PathFile, true);
                            writer.write(ScreenShotPath+"\n");
                            writer.close();
                        } catch (IOException e) {
                        e.printStackTrace();
                        }

                        startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
    /**
     * 拍照或选择照片后截图
     * 传入图片Uri
     */
    public void cropRawPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //把裁剪的数据填入里面
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        if(android.os.Build.MODEL.contains("HUAWEI"))
        {//华为特殊处理 不然会显示圆
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        }
        else
        {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        File nf = new File(Environment.getExternalStorageDirectory()+"/PuzzleScreenShot");
        if(!nf.exists())
        {nf.mkdirs();}
        //在根目录下面的文件夹下 创建 文件
        String npname=Calendar.getInstance().getTimeInMillis()+".jpg";
        File f = new File(Environment.getExternalStorageDirectory()+"/PuzzleScreenShot/", npname);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));//// 输出文件
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 输出格式
        intent.putExtra("scale", true);// 缩放
        intent.putExtra("scaleUpIfNeeded", true);// 如果小于要求输出大小，就放大
        intent.putExtra("return-data", false);// 不返回缩略图
        intent.putExtra("noFaceDetection", true);// 关闭人脸识别
        ScreenShotPath=f.getPath();

        startActivityForResult(intent,RESULT_SCREENSHOT);
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
    //初始化图片
    public void initImage() throws IOException {
        for(int i=0;i<1;i++){
            Img img4=new Img(R.drawable.image4,true);
            Imglist.add(img4);
            Img img5=new Img(R.drawable.image5,true);
            Imglist.add(img5);
            Img img6=new Img(R.drawable.image6,true);
            Imglist.add(img6);
            Img img7=new Img(R.drawable.image7,true);
            Imglist.add(img7);
            Img img8=new Img(R.drawable.image8,true);
            Imglist.add(img8);
            Img img9=new Img(R.drawable.image9,true);
            Imglist.add(img9);
        }
        FileReader fr=new FileReader(PathFile);
        BufferedReader br=new BufferedReader(fr);
        String temp=null;
        String s="";
        while((temp=br.readLine())!=null){
            s+=temp+"\n";
        }
        String [] ss=s.split("\n");
        for (int i = 0; i < ss.length; i++) {
           Img imgg=new Img(ss[i],false);
            Imglist.add(imgg);
        }
    }
}
