package com.example.bjtu.puzzle;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XBhoneYbee on 2017/7/15.
 * 实现打乱和判对
 */

public class GameRule {
    public  List<Box> boxes=new ArrayList<Box>();
    public  Box last;
    private int n=Main2Activity.getDifficulty();
    /**
     *需要再点击后进行判断是否能移动？
     * 移动后是否正解
     * 编写函数 打乱，判断可移动？，移动，判正解
     */
    private static final String TAG = "GameRule";
    public void BoxGenerator(){
        last=boxes.get(boxes.size()-1);
        double times=Math.random()*100+40;
        int time=(int)times;
        int lastId;
        int fromId;
        //Log.e(TAG, "BoxGenerator: "+time );
        for(int i=0;i<time;i++){
            double ddxy=Math.random()*4;

            int dxy=((int)ddxy);
            //Log.e(TAG, "BoxGenerator:dxy  "+dxy );
            //Log.e(TAG, "BoxGenerator: ddxy  "+ddxy );
            switch (dxy){
                case 0://左边
                    lastId=last.getPosId()-1;
                    fromId=(lastId-1+n*n)%(n*n);
                    isChange(boxes.get(fromId),last);
                    break;
                case 1://右边
                    lastId=last.getPosId()-1;
                    fromId=(lastId+1+n*n)%(n*n);
                    isChange(boxes.get(fromId),last);
                    break;
                case 2://上边
                    lastId=last.getPosId()-1;
                    fromId=(lastId-n+n*n)%(n*n);
                    isChange(boxes.get(fromId),last);
                    break;
                case 3://下边
                    lastId=last.getPosId()-1;
                    fromId=(lastId+n+n*n)%(n*n);
                    isChange(boxes.get(fromId),last);
                    break;
                default:
                    break;
            }
        }
    }
    public boolean isChange(Box from,Box last){
        if((from.getPosId()-1)/n==
                (last.getPosId()-1)/n){
            //row
            if(Math.abs(from.getPosId()-last.getPosId())==1){
                swapBox(from,last);
                return true;
            }
        }else if((from.getPosId()-1)%n==
                (last.getPosId()-1)%n){
            //col
            if(Math.abs(from.getPosId()-last.getPosId())==n){
                swapBox(from,last);
                return true;
            }
        }
        return false;
    }
    public void swapBox(Box from,Box last){
        int tmp=from.getBitmapId();
        from.setBitmapId(last.getBitmapId());
        last.setBitmapId(tmp);
        Bitmap tmpbitmap=from.getBitmap();
        from.setBitmap(last.getBitmap());
        last.setBitmap(tmpbitmap);
        this.last=from;
    }
    public boolean isCompleted(){
        for(Box box:boxes){
            if(box.getBitmapId()!=box.getPosId())
            {
                Log.e(TAG, "isCompleted: No beacuse bitmap id"+box.getBitmapId()+"pos id"+box.getPosId() );
                return false;
            }
        }
        return true;
    }
}
