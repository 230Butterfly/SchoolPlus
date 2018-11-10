package com.example.chenwei.plus.Near;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbyInfo;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchFunctionType;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.amap.api.services.nearby.UploadInfoCallback;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by Chenwei on 2018/8/17.
 */

public class Radar {
    private static LatLonPoint latLonPoint=null;
    private static LatLonPoint latLonPointnow =null;
    private static Handler mHandler = null;
    private static Context mContext = null;
    private static AMapLocation aMapLocation =null;
    private static BmobUser customer =null;
    private static int m =0;
    public static AMap amap =null;

    public static void init(Context context, final BmobUser customer1, AMap map) {
        mContext = context;
        customer = customer1;
        amap =map;
    }
    public static void goRadar(LatLonPoint latLonPoint) {
        NearbySearch.NearbyQuery query = new NearbySearch.NearbyQuery();
        //设置搜索的中心点
        query.setCenterPoint(latLonPoint);
        //设置搜索的坐标体系
        query.setCoordType(NearbySearch.AMAP);
        //设置搜索半径
        query.setRadius(10000);
        //设置查询的时间
        query.setTimeRange(100);
        //设置查询的方式驾车还是距离
        query.setType(NearbySearchFunctionType.DRIVING_DISTANCE_SEARCH);
        //调用异步查询接口
        NearbySearch.getInstance(mContext)
                .searchNearbyInfoAsyn(query);
    }

    public static void goRadar(LatLonPoint latLonPoint,int i) {
        NearbySearch.NearbyQuery query = new NearbySearch.NearbyQuery();
        //设置搜索的中心点
        query.setCenterPoint(latLonPoint);
        //设置搜索的坐标体系
        query.setCoordType(NearbySearch.AMAP);
        //设置搜索半径
        query.setRadius(10000);
        //设置查询的时间
        query.setTimeRange(100);
        //设置查询的方式驾车还是距离
        query.setType(NearbySearchFunctionType.DRIVING_DISTANCE_SEARCH);
        //调用异步查询接口
        NearbySearch.getInstance(mContext)
                .searchNearbyInfoAsyn(query);
    }

    public static void startRadar(LatLonPoint latLng) {
        latLonPoint =latLng;
        goStartRadar();
    }


    private static NearbySearch.NearbyListener msearchListener = new NearbySearch.NearbyListener() {
        @Override
        public void onUserInfoCleared(int i) {
        }

        @Override
        public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int i) {
            if(i == 1000){
                if (nearbySearchResult != null
                        &&nearbySearchResult.getNearbyInfoList() != null
                        && nearbySearchResult.getNearbyInfoList().size() > 0) {
                    final int m = nearbySearchResult.getNearbyInfoList().size();
                    for(int y=0;y<m;y++){
                        final NearbyInfo nearbyInfo = nearbySearchResult.getNearbyInfoList().get(y);
                    }
                } else {
                    Toast.makeText(mContext,"周边搜索结果为空",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(mContext,"周边搜索出现异常，异常码为："+i,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onNearbyInfoUploaded(int i) {
            if (i==1000){

            }
            else{
                if(i==2204||i==1100){

                }else{
                    Toast.makeText(mContext,"上传失败: " + i,Toast.LENGTH_LONG).show();
                }
            }

        }
    };
    private static void goStartRadar() {

        // 获取本地位置--上传位置信息--等待回调--回调成功--开始查询--等待回调--回调中打印结果
        NearbySearch mNearbySearch = NearbySearch.getInstance(mContext);
        mNearbySearch.setUserID("123");
        mNearbySearch.addNearbyListener(msearchListener);
        goUploadInfo();
    }

    public static void locationchanging(double Latitude,double Longitude){
        LatLonPoint latLonPoint1= new LatLonPoint(Latitude,Longitude);
        latLonPointnow =new LatLonPoint(Latitude,Longitude);
        if(!latLonPoint.equals(latLonPoint1)){
            latLonPoint = latLonPoint1;
        }else {
            Latitude = Longitude+0.0000001;
            latLonPoint1= new LatLonPoint(Latitude,Longitude);
            latLonPoint = latLonPoint1;
        }
    }

    public static void goUploadInfo() {
//调用异步上传接口
        NearbySearch.getInstance(mContext).startUploadNearbyInfoAuto(new UploadInfoCallback() {
            //设置自动上传数据和上传的间隔时间
            @Override
            public UploadInfo OnUploadInfoCallback() {
                UploadInfo loadInfo = new UploadInfo();
                loadInfo.setCoordType(NearbySearch.AMAP);
                //位置信息
                loadInfo.setPoint(latLonPoint);
                //用户id信息

                loadInfo.setUserID(customer.getObjectId());
                return loadInfo;
            }
        },10000);

    }

    public static void destory(){
        NearbySearch.getInstance(mContext).setUserID(customer.getObjectId());
//调用异步清除用户接口
        NearbySearch.getInstance(mContext)
                .clearUserInfoAsyn();
    }


    private static void  clearMarkers(){
        List<Marker> mapScreenMarkers = amap.getMapScreenMarkers();
        for (int i = 0; i < mapScreenMarkers.size(); i++) {
            Marker marker = mapScreenMarkers.get(i);
            marker.remove();//移除当前Marker
        }
    }

    Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }

    };
}
