package com.example.chenwei.plus.Messages;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.chenwei.plus.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<String> mPost;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public MessageAdapter(List<String> mPost, Context mContext) {
        this.mPost = mPost;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_item,parent,false);
        return new  ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {

        final ViewHolder holder1 = (ViewHolder) holder;
        if(position%3==0){
            holder1.image.setImageResource(R.mipmap.upload);
        }else if(position%3==1){
            holder1.image.setImageResource(R.mipmap.home_normal);
        }else {
            holder1.image.setImageResource(R.drawable.test);
        }

        holder1.name.setText("我是你浩哥");
        holder1.time.setText("11:26");
        holder1.message.setText("天游流星灵恸 "+position);

        if (mItemClickListener != null) {
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
        //return mPost.size();
        return mPost.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView time;
        TextView message;


        public ViewHolder(View itemView) {
            super(itemView);


            image=itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.tv_person);
            time=itemView.findViewById(R.id.tv_time);
            message= itemView.findViewById(R.id.tv_message);
        }
    }
}
