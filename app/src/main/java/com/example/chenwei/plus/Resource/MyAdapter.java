package com.example.chenwei.plus.Resource;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenwei.plus.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Reply_list> reply_lists;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public MyAdapter(List<Reply_list> reply_lists, Context mContext) {
        this.reply_lists = reply_lists;
        this.mContext = mContext;
    }

    //item的回调接口
    public interface OnItemClickListener{
        void onItemClick(View view, int Position);
    }
    //定义一个设置点击监听器的方法
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.reply_item,parent,false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {
        final ViewHolder holder1=(ViewHolder)holder;
        holder1.time_text.setText(reply_lists.get(position).getData());
        holder1.evaluate_text.setText("评价："+reply_lists.get(position).getGrade());
        holder1.name_text.setText("        "+reply_lists.get(position).getName()+position);
        holder1.name_text.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        holder1.name_text.setSingleLine();
        holder1.reply_text.setText(reply_lists.get(position).getReply());
        holder1.head_view.setBackground(mContext.getResources().getDrawable(R.mipmap.person_normal));
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
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView head_view;
        TextView name_text;
        TextView reply_text;
        TextView evaluate_text;
        TextView time_text;


        public ViewHolder(View itemView) {
            super(itemView);
            head_view=itemView.findViewById(R.id.head_view);
            name_text=itemView.findViewById(R.id.name_text);
            reply_text=itemView.findViewById(R.id.reply_text);
            evaluate_text=itemView.findViewById(R.id.evaluate_text);
            time_text=itemView.findViewById(R.id.time_text);
        }
    }
}
