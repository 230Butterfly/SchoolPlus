package com.example.chenwei.plus.Near;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Tool.CircleImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import cn.bmob.v3.BmobUser;

/**
 * Created by Chenwei on 2018/8/17.
 */

public class Addmark extends AsyncTask {

    private AMap amap;
    private Context mContext;
    private LatLng latLng;
    private Customer customer;
    private View markerView;
    private CircleImageView icon;

    public Addmark(AMap amap, Context context, LatLng latLng,Customer customer1) {
        customer =customer1;
        markerView = LayoutInflater.from(context).inflate(R.layout.mark,null);
        icon = markerView.findViewById(R.id.mark_icon);
        this.amap = amap;
        mContext=context;
        this.latLng=latLng;
    }


    protected Object doInBackground(Object[] params) {
        try {
            URL url3 = new URL(customer.getIcon().getUrl());
            InputStream inputStream = url3.openStream();
            // 用inputStream来初始化一个Bitmap 虽然此处是Bitmap，但是URL不一定非得是Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            // 关闭 InputStream
            inputStream.close();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.not_found);
        return bitmap1;
    }

    public static Bitmap convertViewToBitmap(View view) {

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();

        return bitmap;

    }

    protected void onPostExecute(Object o) {
        // 此处的形参o，是doInBackground的返回值
        Bitmap bitmap = (Bitmap)o;
        icon.setImageBitmap(bitmap);
        Bitmap viewToBitmap =(convertViewToBitmap(markerView));
        int width = viewToBitmap.getWidth();
        int height = viewToBitmap.getHeight();

        //设置想要的大小
        int newWidth=120;
        int newHeight=120;

        float scaleWidth=((float)newWidth)/width;
        float scaleHeight=((float)newHeight)/height;

        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);

        //获取新的bitmap
        viewToBitmap=Bitmap.createBitmap(viewToBitmap,0,0,width,height,matrix,true);
        viewToBitmap.getWidth();
        viewToBitmap.getHeight();;
        if(o!=null){
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(latLng);
            markerOption.title("你好").snippet(customer.getUsername());
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(viewToBitmap));
            markerOption.draggable(false);//设置Marker可拖动
            markerOption.setFlat(true);
            markerOption.infoWindowEnable(true);
            amap.addMarker(markerOption);
        }else {
            System.out.println("网络图片下载失败");
        }
    }
}

