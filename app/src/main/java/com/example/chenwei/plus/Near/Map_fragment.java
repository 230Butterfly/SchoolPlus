package com.example.chenwei.plus.Near;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.example.chenwei.plus.MainActivity;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Share;
import com.kongqw.radarscanviewlibrary.RadarScanView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Map_fragment extends Fragment implements AMapLocationListener, LocationSource, AMap.OnMarkerClickListener, SeekBar.OnSeekBarChangeListener {


    private Context mContext;

    private AMap aMap;
    private MapView mapView;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    private LocationSource.OnLocationChangedListener mListener = null;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private AMapLocation myLocation;
    private LatLng latLng;
    private MyLocationStyle myLocationStyle;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption clientOption;
    private Circle circle;
    private ImageButton distance_radar;
    private TextView distance_number;
    private SeekBar seekBar;
    private RadarScanView radarScanView;
    private RelativeLayout radar_container;
    private View markerView;
    private Customer customer;
    private int search_distance;
    private static ArrayList<Customer> customers = new ArrayList<Customer>();


    @RequiresApi(api = Build.VERSION_CODES.M)

    public Map_fragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public Map_fragment(Context context){
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Log.e("around", "申请权限1");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Log.e("around", "申请权限2");
                //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions((MainActivity)mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            //return;
        }
       // showContacts();
        initview(savedInstanceState,view);
        init_other(view);

        return view;

    }


    private void init_other(View view) {
        markerView = LayoutInflater.from(mContext).inflate(R.layout.radar, null);
        radar_container =view.findViewById(R.id.radar_container);
        distance_number =view.findViewById(R.id.distance_number);
        seekBar =view.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);
        distance_radar=view.findViewById(R.id.distance_radar);
        distance_radar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_radar();
                search.goRadar(search_distance);
            }


        });
    }

    private void initview( Bundle savedInstanceState,View view){
        //获取地图控件引用
        mapView = (MapView) view.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {

            aMap = mapView.getMap();
            //设置显示定位按钮 并且可以点击
            aMap.setLocationSource(this);//设置了定位的监听
            // 是否显示定位按钮
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
            MyLocationStyle m= aMap.getMyLocationStyle();
            m.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
            m.strokeColor(Color.argb(0, 0, 0, 0));// 设置精度圈边框的颜色
            aMap.setMyLocationStyle(m);
            //marker点击事件
            aMap.setOnMarkerClickListener(this);
        }
        location();
    }

    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(7000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }
    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener=null;
        if(locationClient!=null){
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient=null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                myLocation =aMapLocation;
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码
                latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    //添加图钉
                    //  aMap.addMarker(getMarkerOptions(amapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getCity() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getDistrict() + ""
                            + aMapLocation.getStreet() + ""
                            + aMapLocation.getStreetNum());
                    Toast.makeText(mContext, buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                    circle = aMap.addCircle(new CircleOptions().
                            center(latLng).
                            radius(100).
                            fillColor(Color.argb(20, 0, 0, 150)).
                            strokeColor(Color.argb(30, 0, 0, 250)).
                            strokeWidth(5));

                    //初始化雷达
                    customer= BmobUser.getCurrentUser(Customer.class);
                    search.init(mContext,customer,aMap,this,new LatLonPoint(myLocation.getLatitude(), myLocation.getLongitude()));
                    search.startRadar();

                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                //Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    /*private void showContacts() {
        if (ActivityCompat.checkSelfPermission((MainActivity)mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Log.e("around", "申请权限1");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Log.e("around", "申请权限2");
                //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions((MainActivity)mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            //return;
        }
    }*/
    /**
     * 必须重写以下方法
     */
    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(locationClient!=null){
            locationClient.onDestroy();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.isInfoWindowShown()){
            marker.hideInfoWindow();
        }else {
            marker.showInfoWindow();
        }
        return true;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(circle!=null) {
            search_distance = progress*2;
            distance_number.setText(String.valueOf(progress * 10));
            circle.setRadius(progress * 10);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void start_radar(){
        Message message = Message.obtain();
        message.what = 1;
        mHandler.sendMessage(message);
        TimerTask task =new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 2;
                mHandler.sendMessage(message);
            }
        };
        Timer timer =new Timer();
        timer.schedule(task,3000);
    }
    public void after_radar(){
        radarScanView.stopScan();
        aMap.getUiSettings().setAllGesturesEnabled(true);
        radar_container.removeAllViews();

        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //    设置Title的图标
        builder.setIcon(R.mipmap.share);
        //    设置Title的内容
        builder.setTitle("附近扫描完毕！");
        //    设置Content来显示一个信息
        builder.setMessage("        本次扫描完成，本次共扫描到"+(customers.size()-1) +"个人");
        //    设置一个PositiveButton
        builder.setPositiveButton("好的", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        //    设置一个NegativeButton
        builder.setNeutralButton("重新扫描", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                start_radar();
            }
        });
        //    设置一个NeutralButton
        builder.show();
    }
    Handler mHandler = new Handler(){
        /**
         * handleMessage接收消息后进行相应的处理
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                radar_container.addView(markerView);
                radarScanView =markerView.findViewById(R.id.radarScanView);
                radarScanView.startScan();
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                aMap.getUiSettings().setAllGesturesEnabled(false);
            }else{
                after_radar();
            }
        }
    };

    public void setcustomer(ArrayList<Customer> customers1){
        customers =customers1;
        ((MainActivity)mContext).setcustomer(customers1);
        ((Share)getApplicationContext()).getSfragment().show();
    }
    public LatLonPoint getLatLonPoint(){
        return new LatLonPoint(myLocation.getLatitude(), myLocation.getLongitude());
    }
    public void setcircle(){
        circle.setCenter(latLng);
    }

    public LatLonPoint getanotherPoint(){
        return new LatLonPoint(myLocation.getLatitude()+0.00000001, myLocation.getLongitude());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        search.destory();
        mapView.onDestroy();
        mLocationClient.stopLocation();//停止定位
        mLocationClient.onDestroy();//销毁定位客户端。
    }
}
