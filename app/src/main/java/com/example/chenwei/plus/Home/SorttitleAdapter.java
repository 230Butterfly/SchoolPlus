package com.example.chenwei.plus.Home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.chenwei.plus.R;

import java.util.ArrayList;
import java.util.List;

public class SorttitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull
    private List<String> mTitle;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private List<Integer> positionList = new ArrayList<Integer>();
    private int selected =-1;
    public SorttitleAdapter(List<String> mTitle, Context mContext, int position) {
        this.mTitle = mTitle;
        this.mContext = mContext;
        positionList.add(position);
    }
    //item的回调接口
    public interface OnItemClickListener{
        void onItemClick(View view, int Position);
    }
    //定义一个设置点击监听器的方法
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setSelected(int position){
        this.selected=position;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sorttitle_item,parent,false);
        return new  ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder holder1 = (ViewHolder) holder;
        //holder1.iv_signal.setImageResource(R.drawable.signal);
        holder1.iv_signal.setBackgroundColor(mContext.getResources().getColor(R.color.text_yellow));
        holder1.tv_title.setText(mTitle.get(position));

        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position= holder1.getPosition();
                    mItemClickListener.onItemClick(holder.itemView, position);
                    positionList.clear();
                    positionList.add(position);
                }
            });
        }
        if(positionList.contains(position)){
            holder1.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.bg_gray));
            holder1.tv_title.setTextColor(mContext.getResources().getColor(R.color.text_selected));
            holder1.iv_signal.setVisibility(View.VISIBLE);
            holder1.tv_title.setTextSize(18);
        }else {
            holder1.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.div_white));
            holder1.tv_title.setTextColor(mContext.getResources().getColor(R.color.text_normal));
            holder1.tv_title.setTextSize(15);
            holder1.iv_signal.setVisibility(View.INVISIBLE);
        }
    }
    /*
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sorttitle_item,parent,false);
        return new  ViewHolder(view);    }*/

    @Override
    public int getItemCount() {
        return mTitle.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_signal;
        TextView tv_title;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_signal= itemView.findViewById(R.id.img_signal);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }

}
