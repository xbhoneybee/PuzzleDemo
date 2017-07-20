package com.example.bjtu.puzzle;

import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XBhoneYbee on 2017/7/15.
 * 实现基本游戏规则
 */

public class GameRule {
    public  List<Box> boxes=new ArrayList<Box>();//小方块list
    public  Box last;//移动标志块
    private int n=Main2Activity.getDifficulty();

    /**
     * 生成开始游戏后的boxes
     */
    public void BoxGenerator(){
        last=boxes.get(boxes.size()-1);
        double times=Math.random()*100+40;
        int time=(int)times;
        int lastId;
        int fromId;
        for(int i=0;i<time;i++){
            double ddxy=Math.random()*4;
            int dxy=((int)ddxy);
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
    public boolean isChange(Box from,Box last){//判断可否交换
        if((from.getPosId()-1)/n==
                (last.getPosId()-1)/n){
            //左右
            if(Math.abs(from.getPosId()-last.getPosId())==1){
                swapBox(from,last);
                return true;
            }
        }else if((from.getPosId()-1)%n==
                (last.getPosId()-1)%n){
            //上下
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
                return false;
            }
        }
        return true;
    }
}
