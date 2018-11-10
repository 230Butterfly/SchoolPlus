package com.example.chenwei.plus.Home;

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
import com.example.chenwei.plus.Tool.Near_resource_information;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.chenwei.plus.Upload.utils.FileUtil.fileGetBitmap;

public class ClassifyContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    @NonNull
    private ArrayList<Near_resource_information> mPost;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    public ClassifyContentAdapter(ArrayList<Near_resource_information> mPost, Context mContext) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.classify_content_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder holder1 = (ViewHolder) holder;
//        if(position%3==0){
//            holder1.image.setImageResource(R.mipmap.upload);
//        }else if(position%3==1){
//            holder1.image.setImageResource(R.mipmap.home_normal);
//        }else {
//            holder1.image.setImageResource(R.drawable.test);
//        }
        try {
            holder1.image.setImageBitmap(fileGetBitmap(mContext,mPost.get(position).getFile_url()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder1.description.setText(mPost.get(position).getResource_name());
        holder1.description.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        holder1.description.setSingleLine();
        holder1.mark.setText(mPost.get(position).getResource_grade()+"分");
        holder1.iv_person.setImageResource(R.mipmap.home_active);
        holder1.tv_person.setText(mPost.get(position).getUser_name());

        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(holder.itemView, position);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView description;
        TextView mark;
        ImageView iv_person;
        TextView tv_person;


        public ViewHolder(View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image_content);
            description = itemView.findViewById(R.id.description_content);
            mark=itemView.findViewById(R.id.mark_content);
            iv_person = itemView.findViewById(R.id.iv_person_content);
            tv_person = itemView.findViewById(R.id.tv_person_content);
        }
    }
}
