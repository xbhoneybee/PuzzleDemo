package com.example.bjtu.puzzle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by dl188 on 2017/7/16.
 */

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ViewHolder>{
    private List<Img> Imglist;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ImgImage;
        public ViewHolder(View itemView) {
            super(itemView);
            ImgImage=(ImageView) itemView.findViewById(R.id.recycler_item_img);

        }
    }

    public ImgAdapter(List<Img> imglist){
        Imglist=imglist;
    }

    @Override
    public ImgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
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
