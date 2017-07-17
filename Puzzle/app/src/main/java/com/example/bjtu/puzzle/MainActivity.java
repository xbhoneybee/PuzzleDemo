package com.example.bjtu.puzzle;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Img>  Imglist=new ArrayList<>();
    private static int Difficulty=3;
    private static int chosenImage=R.drawable.image1;
    private RecyclerView recyclerView;
    public static int getChosenImage() {
        return chosenImage;
    }

    public static void setChosenImage(int chosenImage) {
        MainActivity.chosenImage = chosenImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImage();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
//        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ImgAdapter adapter=new ImgAdapter(Imglist);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.id_help:
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("关  于");
                dialog.setMessage("XXX工作室\ncopyright @2017|all right reserved.");
                dialog.setCancelable(true);
                dialog.show();
                break;
            default:
                break;
        }
        return true;
    }
    //初始化图片
    public void initImage(){
        for(int i=0;i<5;i++){
            Img img1=new Img(R.drawable.image1);
            Imglist.add(img1);
            Img img2=new Img(R.drawable.image2);
            Imglist.add(img2);
            Img img3=new Img(R.drawable.image3);
            Imglist.add(img3);
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
