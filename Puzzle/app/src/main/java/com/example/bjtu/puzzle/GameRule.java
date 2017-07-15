package com.example.bjtu.puzzle;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XBhoneYbee on 2017/7/15.
 * 实现打乱和判对
 */

public class GameRule {
    public static List<Box> boxes=new ArrayList<Box>();
    public static Box last;
    private int n=MainActivity.getDifficulty();
    /**
     *需要再点击后进行判断是否能移动？
     * 移动后是否正解
     * 编写函数 打乱，判断可移动？，移动，判正解
     */
    public void BoxGenerator(){
        int time=((int)Math.random()*100+10)%50;
        int lastId;
        int fromId;
        for(int i=0;i<time;i++){
            int dxy=((int)Math.random()*5+1)%4;
            switch (dxy){
                case 0:
                    lastId=last.getPosId()-1;
                    fromId=(lastId-1+n*n)%(n*n);
                    isChange(boxes.get(fromId),last);
                    break;
                case 1:
                    lastId=last.getPosId()-1;
                    fromId=(lastId+1+n*n)%(n*n);
                    isChange(boxes.get(fromId),last);
                    break;
                case 2:
                    lastId=last.getPosId()-1;
                    fromId=(lastId-n+n*n)%(n*n);
                    isChange(boxes.get(fromId),last);
                    break;
                case 3:
                    lastId=last.getPosId()-1;
                    fromId=(lastId+n+n*n)%(n*n);
                    isChange(boxes.get(fromId),last);
                    break;
                default:
                    break;
            }
        }
    }
    public void isChange(Box from,Box last){
        if((from.getPosId()-1)/MainActivity.getDifficulty()==
                (last.getPosId()-1)/MainActivity.getDifficulty()){
            //row
            if(Math.abs(from.getPosId()-last.getPosId())==1){
                swapBox(from,last);
            }
        }else if((from.getPosId()-1)%MainActivity.getDifficulty()==
                (last.getPosId()-1)%MainActivity.getDifficulty()){
            //col
            if(Math.abs(from.getPosId()-last.getPosId())==MainActivity.getDifficulty()){
                swapBox(from,last);
            }
        }
    }
    public void swapBox(Box from,Box last){
        int tmp=from.getBitmapId();
        from.setBitmapId(last.getBitmapId());
        last.setBitmapId(tmp);
        Bitmap tmpbitmap=from.getBitmap();
        from.setBitmap(last.getBitmap());
        last.setBitmap(tmpbitmap);
        GameRule.last=from;
    }
    public boolean isCompleted(){
        for(Box box:boxes){
            if(box.getBitmapId()!=box.getPosId())
                return false;
        }
        return true;
    }
}
