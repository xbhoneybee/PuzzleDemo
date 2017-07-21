package com.example.bjtu.puzzle;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by dl188 on 2017/7/16.
 * RecyclerView Adapter
 */

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ViewHolder>{
    private List<Img> Imglist;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View aview;
        ImageView ImgImage;
        public ViewHolder(View itemView) {
            super(itemView);
            aview=itemView;
            ImgImage=(ImageView) itemView.findViewById(R.id.recycler_item_img);
        }
    }
    public ImgAdapter(List<Img> imglist){
        Imglist=imglist;
    }

    @Override
    public ImgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.ImgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Img img=Imglist.get(position);
                MainActivity.setChosenImage(img.getImg());
                //进入第二页面
                Intent intent=new Intent(view.getContext(),Main2Activity.class);
                intent.putExtra("Picture",img.getImg());
                intent.putExtra("Picturepath",img.getPath());
                view.getContext().startActivity(intent);
            }
        });
        holder.ImgImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(view.getContext());
                dialog.setTitle("确定删除此图？"+"\n Delete this picture?" );
                dialog.setCancelable(false);
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int posdtion=holder.getAdapterPosition();
                        Img img=Imglist.get(posdtion);
                        if(img.isIsself()){
                            Toast.makeText(view.getContext(),"你不能删除默认图片\nYou can not delete the default picture",Toast.LENGTH_LONG).show();
                        }else {
                            String delPath=img.getPath();
                            /**
                             * 操作，全部读出后写入
                             */
                            FileReader fr= null;
                            String s="";
                            try {
                                fr = new FileReader(MainActivity.PathFile);
                                BufferedReader br=new BufferedReader(fr);
                                String temp=null;

                                while((temp=br.readLine())!=null){
                                    s+=temp+"\n";
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String [] ss=s.split("\n");
                            FileWriter writer = null;
                            try {
                                writer = new FileWriter(MainActivity.PathFile);
                                for (int id = 0; id < ss.length; id++) {
                                    if(ss[id].equals(delPath)==false)
                                        writer.write(ss[id]+"\n");
                                }
                                writer.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            File todelfile=new File(delPath);
                            todelfile.delete();
                            MainActivity.Imglist.remove(img);
                            notifyItemRemoved(posdtion);
                            notifyItemRangeChanged(posdtion, MainActivity.Imglist.size() - posdtion);
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ImgAdapter.ViewHolder holder, int position) {
        Img img=Imglist.get(position);
        if(img.isIsself())
        holder.ImgImage.setImageResource(img.getImg());
        else {
            holder.ImgImage.setImageBitmap(BitmapFactory.decodeFile(img.getPath()));
        }
    }
    @Override
    public int getItemCount() {
        return Imglist.size();
    }
}
