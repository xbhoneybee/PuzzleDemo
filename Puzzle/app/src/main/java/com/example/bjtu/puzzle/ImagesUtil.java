package com.example.bjtu.puzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XBhoneYbee on 2017/7/15.
 * 将一张图片分割成N*N的Box
 * 参考CSDN 专家 eclipse_xu   blog
 */

public class ImagesUtil {
    public  Box box;
    private  GameRule ruler;
    public ImagesUtil(GameRule ri){
        ruler=ri;
    }
    public  void createInitBitmaps(int n, Bitmap picSelected,Context context){
        /**
         *传入难度 n 选择的图片 和上下文
         */
        Bitmap bitmap=null;
        List<Bitmap> bitmapItems= new ArrayList<Bitmap>();
        int itemWidth=picSelected.getWidth()/n;
        int itemHeight=picSelected.getHeight()/n;
        for(int i=1;i<=n;i++)
            for(int j=1;j<=n;j++)
            {
                bitmap=Bitmap.createBitmap(picSelected,(j-1)*itemWidth,(i-1)*itemHeight,itemWidth,itemHeight);
                bitmapItems.add(bitmap);
                if(i==n&&j==n){
                    bitmap=DrawableIdToBitmap(context,R.drawable.icon1);
                    bitmap=resizeBitmap(itemWidth,itemHeight,bitmap);
                    ruler.last=new Box(n*n,n*n,bitmap);
                }
                box=new Box((i-1)*n+j,(i-1)*n+j,bitmap);
                ruler.boxes.add(box);
            }
    }
    public static Bitmap resizeBitmap(float newWidth,float newHeight,Bitmap bitmap){
        Matrix matrix=new Matrix();
        matrix.postScale(newWidth/bitmap.getWidth(),newHeight/bitmap.getHeight());
        Bitmap newBitmap =Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        /**
          * 设置矩阵，对Bitmap 进行放缩
          */
        return newBitmap;
    }
    public  Bitmap DrawableIdToBitmap(Context contxet , int id) {
        Drawable drawable= ContextCompat.getDrawable(contxet,id);
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
