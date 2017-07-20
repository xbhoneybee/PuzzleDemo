package com.example.bjtu.puzzle;

import android.graphics.Bitmap;

/**
 * Created by XBhoneYbee on 2017/7/15.
 */

/**
 * 小可移动块类
 */
public class Box {
    private int posId;
    private int bitmapId;
    private Bitmap bitmap;

    public int getPosId(){
        return posId;
    }
    public int getBitmapId(){
        return bitmapId;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBitmapId(int bitmapId){
        this.bitmapId=bitmapId;
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap=bitmap;
    }
    public Box(){

    }
    public Box(int posId,int bitmapId,Bitmap bitmap){
        this.posId=posId;
        this.bitmapId=bitmapId;
        this.bitmap=bitmap;
    }
}
