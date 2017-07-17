package com.example.bjtu.puzzle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by XBhoneYbee on 2017/7/17.
 */

public class GridAdapter extends BaseAdapter {

    private List<Box> picList;
    private Context context;
    private  float SQ;
    public GridAdapter(Context context, List<Box> picList) {
        this.context = context;
        this.picList = picList;
        SQ=Puzzle.Length/Puzzle.n;
    }
    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public Object getItem(int position) {
        return picList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView ic_pic_item=null;
        if(convertView==null){
            ic_pic_item=new ImageView(context);
            ic_pic_item.setLayoutParams(new GridView.LayoutParams(picList.get(0).getBitmap().getWidth(),picList.get(0).getBitmap().getHeight()));
            //
            ic_pic_item.setScaleType(ImageView.ScaleType.FIT_XY);
            //
        }else ic_pic_item=(ImageView)convertView;
        ic_pic_item.setImageBitmap(picList.get(position).getBitmap());
        return ic_pic_item;
    }
}
