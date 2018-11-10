package com.example.chenwei.plus.Upload.adapter;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Upload.bean.EventCenter;
import com.example.chenwei.plus.Upload.bean.FileDao;
import com.example.chenwei.plus.Upload.bean.FileInfo;
import com.example.chenwei.plus.Upload.bean.SubItem;
import com.example.chenwei.plus.Upload.utils.FileUtil;
import com.example.chenwei.plus.Upload.view.CheckBox;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by CWJ on 2017/3/22.
 * 参考自https://blog.csdn.net/yulyu/article/details/55056352
 */

public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int HEAD = 0;
    public static final int CONTENT = 1;
    private boolean isPhoto = false;

    public ExpandableItemAdapter(List<MultiItemEntity> data, boolean isPhoto) {
        super(data);
        this.isPhoto = isPhoto;
        addItemType(HEAD, R.layout.item_head);
        if (isPhoto) {
            addItemType(CONTENT, R.layout.item_content_photo);
        } else {
            addItemType(CONTENT, R.layout.item_content);
        }

    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {

        switch (helper.getItemViewType()) {
            case HEAD:
                final SubItem subItem = (SubItem) item;
                if (null == subItem.getSubItems() || subItem.getSubItems().size() == 0) {
                    helper.setText(R.id.tv_count, mContext.getString(R.string.count, "" + 0));
                } else {
                    helper.setText(R.id.tv_count, mContext.getString(R.string.count, "" + subItem.getSubItems().size()));
                }

                helper.setText(R.id.tv_title, subItem.getTitle());
                helper.setImageResource(R.id.expanded_menu, subItem.isExpanded() ? R.drawable.ic_arrow_drop_down_grey_700_24dp : R.drawable.ic_arrow_drop_up_grey_700_24dp);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (subItem.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case CONTENT:
                final FileInfo f = (FileInfo) item;
                helper.setText(R.id.tv_content, f.getFileName())
                        .setText(R.id.tv_size, FileUtil.FormetFileSize(f.getFileSize()))
                        .setText(R.id.tv_time, f.getTime());
                if (isPhoto) {
                    Glide.with(mContext).load(f.getFilePath()).into((ImageView) helper.getView(R.id.iv_cover));
                } else if(FileUtil.getFileTypeImageId(mContext, f.getFilePath())==-1) {//获取视频第一帧为显示图片
                    Glide.with(mContext).load(f.getFilePath()).into((ImageView) helper.getView(R.id.iv_cover));
                }else if(FileUtil.getFileTypeImageId(mContext, f.getFilePath())==-2){//获取app图标
//                    PackageManager pm = mContext.getPackageManager();
//                    PackageInfo info = pm.getPackageArchiveInfo(f.getFilePath(), PackageManager.GET_ACTIVITIES);
//                    Drawable drawable = info.applicationInfo.loadIcon(pm);
//                    Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                    Bitmap bitmap=getBitmap(mContext);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50,baos);
                    String imageBase64 = new String (Base64.encode(baos.toByteArray(), 0));

                    Glide.with(mContext).load(imageBase64).asBitmap().into((ImageView) helper.getView(R.id.iv_cover));
                }else{

                    Glide.with(mContext).load(FileUtil.getFileTypeImageId(mContext, f.getFilePath())).fitCenter().into((ImageView) helper.getView(R.id.iv_cover));
                }
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPhoto) {
                            boolean IsPhoto = f.getIsPhoto();
                            f.setIsPhoto(!IsPhoto);
                        } else {
                            f.setIsPhoto(false);
                        }

                        boolean isCheck = f.getIsCheck();
                        f.setIsCheck(!isCheck);
                        if (f.getIsCheck()) {
                            FileDao.insertFile(f);
                            ((CheckBox) helper.getView(R.id.cb_file)).setChecked(true, true);
                        } else {
                            FileDao.deleteFile(f);
                            ((CheckBox) helper.getView(R.id.cb_file)).setChecked(false, true);
                        }
                        EventBus.getDefault().post(new EventCenter<>(1, isPhoto));
                    }
                });
                break;
        }
    }
    public static synchronized Bitmap getBitmap(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }



}
