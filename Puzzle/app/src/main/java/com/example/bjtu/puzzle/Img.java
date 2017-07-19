package com.example.bjtu.puzzle;

/**
 * Created by dl188 on 2017/7/16.
 */

public class Img {
    private int img;
    private boolean isself;
    private  String path=null;
    public Img(int im,boolean self){
        img=im;isself=self;
    }
    public Img(String pa,boolean self){
        path=pa;isself=self;
    }
    public int getImg() {
        return img;
    }
    public  String getPath(){
        return path;
    }
    public boolean isIsself(){
        return isself;
    }
}
