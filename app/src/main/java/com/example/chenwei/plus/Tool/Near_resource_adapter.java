package com.example.chenwei.plus.Tool;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenwei.plus.Near.Near_resource_detail;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Resource.MyAdapter;
import com.example.chenwei.plus.Resource.Reply_list;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chenwei on 2018/8/15.
 */

public class Near_resource_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Near_resource_information> reply_lists;
    private Context mContext;
    private Near_resource_adapter.OnItemClickListener mItemClickListener;

    public Near_resource_adapter(ArrayList<Near_resource_information> reply_lists, Context mContext) {
        this.reply_lists = reply_lists;
        this.mContext = mContext;
    }

    //item的回调接口
    public interface OnItemClickListener{
        void onItemClick(View view, int Position);
    }
    //定义一个设置点击监听器的方法
    public void setOnItemClickListener(Near_resource_adapter.OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.near_re_item,parent,false);
        return new Near_resource_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {
        final Near_resource_adapter.ViewHolder holder1=(Near_resource_adapter.ViewHolder)holder;

        holder1.near_head_view.setBackground(mContext.getResources().getDrawable(R.mipmap.person_active));
        holder1.near_name_text.setText(reply_lists.get(position).getUser_name());
        holder1.near_resource_name.setText(reply_lists.get(position).getResource_name());
        holder1.near_price.setText("积分："+reply_lists.get(position).getPrice()+position);
        holder1.near_evaluate_text.setText("评价："+reply_lists.get(position).getResource_grade());
        holder1.near_distance_text.setText("距离："+reply_lists.get(position).getDistance());
        holder1.near_resource_picture.setBackground(mContext.getResources().getDrawable(R.drawable.not_found));
        if(mItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return reply_lists.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView near_head_view;
        TextView near_name_text;
        TextView near_resource_name;
        TextView near_price;
        TextView near_evaluate_text;
        TextView near_distance_text;
        ImageView near_resource_picture;


        public ViewHolder(View itemView) {
            super(itemView);
            near_head_view=itemView.findViewById(R.id.near_head_view);
            near_name_text=itemView.findViewById(R.id.near_name_text);
            near_resource_name=itemView.findViewById(R.id.near_resource_name);
            near_price=itemView.findViewById(R.id.near_price);
            near_evaluate_text=itemView.findViewById(R.id.near_evaluate_text);
            near_distance_text=itemView.findViewById(R.id.near_distance_text);
            near_resource_picture=itemView.findViewById(R.id.near_resource_picture);
        }
    }
}

