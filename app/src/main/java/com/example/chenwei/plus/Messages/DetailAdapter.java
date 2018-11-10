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

public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<String> mPost;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private int TEXT_SEND_TYPE=1;
    private int TEXT_RECEIVE_TYPE=2;


    public DetailAdapter(List<String> mPost, Context mContext) {
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
       // View view = LayoutInflater.from(mContext).inflate(R.layout.message_item,parent,false);
       // return new  DetailAdapter.ViewHolder(view);
       if(viewType==2){
           View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_receive,parent,false);
            return new  FirstViewHolder(view);
        }
        else {
           View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_send,parent,false);
           return new  SecondViewHolder(view);
       }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {

        if(holder.getItemViewType()==2){
            final FirstViewHolder holder1 = (FirstViewHolder) holder;
            if(position%3==0){
                holder1.image.setImageResource(R.mipmap.upload);
            }else if(position%3==1){
                holder1.image.setImageResource(R.mipmap.home_normal);
            }else {
                holder1.image.setImageResource(R.drawable.test);
            }

            holder1.name.setText(mPost.get(position));
            holder1.time.setText("今天");
           // holder1.message.setText("天游流星灵恸"+position);

            if (mItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(holder.itemView, position);

                    }
                });
            }
        }else{
            final SecondViewHolder holder1 = (SecondViewHolder) holder;
            if(position%3==0){
                holder1.image.setImageResource(R.mipmap.upload);
            }else if(position%3==1){
                holder1.image.setImageResource(R.mipmap.home_normal);
            }else {
                holder1.image.setImageResource(R.drawable.test);
            }
            String infor[] =mPost.get(position).split(" ");

            holder1.name.setText(infor[1]);
            holder1.time.setText("今天");
            //holder1.message.setText("天游流星灵恸"+position);

            if (mItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(holder.itemView, position);

                    }
                });
            }
        }




    }

    public int getItemViewType(int position){
        if(mPost.get(position).startsWith("#"))
            return 1;
        else
            return 2;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        //return mPost.size();
        return mPost.size();
    }
    static class FirstViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView time;
        //TextView message;


        public FirstViewHolder(View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.iv_avatar);
            name = itemView.findViewById(R.id.tv_message);
            time=itemView.findViewById(R.id.tv_time);
           // message= itemView.findViewById(R.id.tv_message);
        }
    }
    static class SecondViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView time;

        public SecondViewHolder(View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.iv_avatar);
            name = itemView.findViewById(R.id.tv_message);
            time=itemView.findViewById(R.id.tv_time);
        }
    }
}
