package com.example.bjtu.puzzle;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by dl188 on 2017/7/16.
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
                int posdtion=holder.getAdapterPosition();
                Img img=Imglist.get(posdtion);
                //处理
                Toast.makeText(view.getContext(),"you clicked image"+String.valueOf(img.getImg()), Toast.LENGTH_SHORT).show();
                MainActivity.setChosenImage(img.getImg());
                //进入第二页面
                Intent intent=new Intent(view.getContext(),Main2Activity.class);
                intent.putExtra("Picture",img.getImg());
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ImgAdapter.ViewHolder holder, int position) {
        Img img=Imglist.get(position);
        holder.ImgImage.setImageResource(img.getImg());
    }
    @Override
    public int getItemCount() {
        return Imglist.size();
    }
}
