package com.example.bjtu.puzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XBhoneYbee on 2017/7/15.
 * 将一张图片分割成N*N的Box
 * 参考CSDN 专家 eclipse_xu   blog
 */

public class ImagesUtil {
    public static Box box;
    public static void createInitBitmaps(int n, Bitmap picSelected,Context context){
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
                    bitmap=Bitmap.createBitmap(resizeBitmap(itemWidth,itemHeight,picSelected));
                    GameRule.last=new Box(n*n,n*n,bitmap);
                    /**
                     * 这个还需要将picSeleced缩小到n倍,当做起始操作点
                     */
                }
                box=new Box((i-1)*n+j,(i-1)*n+j,bitmap);
                GameRule.boxes.add(box);
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
}
