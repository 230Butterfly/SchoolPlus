package com.example.chenwei.plus.Near;

import android.content.Context;
import android.os.Handler;
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
 * Created by Chenwei on 2018/5/28.
 */

public class search {

    private static LatLonPoint latLonPoint=null;
    private static Handler mHandler = null;
    private static Context mContext = null;
    private static AMapLocation aMapLocation =null;
    private static Customer customer =null;
    private static int m =0;
    public static AMap amap =null;
    private static ArrayList<Customer> customers = new ArrayList<Customer>();
    private static Map_fragment map_fragment;
    public static void init(final Context context, final Customer customer1, AMap map, Map_fragment map_fragment1, final LatLonPoint latLonPoint1) {
        mContext = context;
        customer = customer1;
        amap =map;
        map_fragment =map_fragment1;
        latLonPoint =latLonPoint1;

        mHandler = new Handler() {

            public void handleMessage(android.os.Message msg) {

                switch (msg.what) {
                    case 0:
                        goStartRadar();
                        break;
                    case 1:
                        goUploadInfo();
                        break;
                    case 2:
                        Log.v("位置上传成功","succeed");
                        //Toast.makeText(mContext,"位置上传成功: ",Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Log.v("搜索成功","succeed");
                        map_fragment.setcustomer(customers);
                        addmark();
                        break;
                    case 4:
                        Toast.makeText(mContext,"weizhixiangtong",Toast.LENGTH_SHORT).show();
                        //goDestory();
                        break;
                    case 5:
                        Toast.makeText(mContext,"weizhiweikong",Toast.LENGTH_SHORT).show();
                        //goClear();
                        break;
                    case 6:
                        Toast.makeText(mContext,"weizhibutong",Toast.LENGTH_SHORT).show();
                        //goClear();
                        break;
                    default:
                        break;
                }
            }

           private void addmark() {
                int sign =0;
               for(Customer p:customers){
                   if(p.getObjectId().equals(customer.getObjectId())){
                       sign =1;
                       break;
                   }
               }
               if(sign==0){
                   customers.add(customer);
                   Log.v("添加自己","添加自己");
               }
                for(Customer p:customers){
                    System.out.println(p.getUsername());
                    Addmark addmark=new Addmark(amap,mContext,new LatLng(latLonPoint.getLatitude(),latLonPoint.getLongitude()),p);
                    addmark.execute();
                }
            }

        };
    }

    public static void goRadar(int distance) {
        NearbySearch.NearbyQuery query = new NearbySearch.NearbyQuery();
        //设置搜索的中心点
        query.setCenterPoint(latLonPoint);
        //设置搜索的坐标体系
        query.setCoordType(NearbySearch.AMAP);
        //设置搜索半径
        query.setRadius(distance*10);
        //设置查询的时间
        query.setTimeRange(2000);
        //设置查询的方式驾车还是距离
        query.setType(NearbySearchFunctionType.DRIVING_DISTANCE_SEARCH);
        //调用异步查询接口
        NearbySearch.getInstance(mContext)
                .searchNearbyInfoAsyn(query);
    }

    public static void startRadar() {
        mHandler.sendEmptyMessage(0);
    }


    private static NearbySearch.NearbyListener msearchListener = new NearbySearch.NearbyListener() {
        @Override
        public void onUserInfoCleared(int i) {

        }

        @Override
        public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int i) {
            if(i == 1000){
                clearMarkers();
                customers.clear();
                if (nearbySearchResult != null
                        &&nearbySearchResult.getNearbyInfoList() != null
                        && nearbySearchResult.getNearbyInfoList().size() > 0) {
                    Log.v("搜索到周边的人",String.valueOf(nearbySearchResult.getNearbyInfoList().size()));
                    //清除现有marker

                    final int m = nearbySearchResult.getNearbyInfoList().size();
                    final ArrayList<NearbyInfo> nearbyInfos =new ArrayList<NearbyInfo>();
                    for(int y=0;y<m;y++){
                        final NearbyInfo nearbyInfo = nearbySearchResult.getNearbyInfoList().get(y);
                        nearbyInfos.add(nearbyInfo);
                        BmobQuery<Customer> query = new BmobQuery<Customer>();
                        query.getObject(nearbyInfo.getUserID(), new QueryListener<Customer>() {

                            @Override
                            public void done(Customer object, BmobException e) {
                                if(e==null){
                                    Log.v("zhege",object.getUsername());
                                    object.setLatLng(new LatLng(nearbyInfos.get(customers.size()).getPoint().getLatitude(),nearbyInfos.get(customers.size()).getPoint().getLongitude()));
                                    float distance = AMapUtils.calculateLineDistance(object.getLatLng(),new LatLng(latLonPoint.getLatitude(),latLonPoint.getLongitude()));
                                    if(object.getObjectId().equals(customer.getObjectId())){
                                        distance=0;
                                    }
                                    object.setDistance(distance);
                                    customers.add(object);
                                    if (customers.size()==m){
                                        mHandler.sendEmptyMessage(3);
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }

                        });
                    }

                } else {
                    mHandler.sendEmptyMessage(3);
                    Log.v("搜索成功","周边搜索结果为空");
                }
            }
            else{
                Toast.makeText(mContext,"周边搜索出现异常，异常码为："+i,Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onNearbyInfoUploaded(int i) {
            if (i==1000){
                mHandler.sendEmptyMessage(2);
            }
            else{
               Log.v("上传失败",String.valueOf(i));
            }

        }
    };
    private static void goStartRadar() {

        // 获取本地位置--上传位置信息--等待回调--回调成功--开始查询--等待回调--回调中打印结果
        NearbySearch mNearbySearch = NearbySearch.getInstance(mContext);
        mNearbySearch.setUserID(customer.getObjectId());
        mNearbySearch.addNearbyListener(msearchListener);
        mHandler.sendEmptyMessage(1);
    }


    public static void goUploadInfo() {
//调用异步上传接口
        NearbySearch.getInstance(mContext).startUploadNearbyInfoAuto(new UploadInfoCallback() {
            //设置自动上传数据和上传的间隔时间
            @Override
            public UploadInfo OnUploadInfoCallback() {
                if(!latLonPoint.equals(map_fragment.getLatLonPoint())) {
                    latLonPoint=map_fragment.getLatLonPoint();
                }
                else {
                    //latLonPoint = map_fragment.getanotherPoint();

                }
                map_fragment.setcircle();
                UploadInfo loadInfo = new UploadInfo();
                loadInfo.setCoordType(NearbySearch.AMAP);
                //位置信息
                loadInfo.setPoint(latLonPoint);
                //用户id信息

                loadInfo.setUserID(customer.getObjectId());
                return loadInfo;
            }
        },8000);



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


}

