package com.example.chenwei.plus.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Chenwei on 2018/9/25.
 */

public class Bluetooth {
    public static final String PORTOCOL_SCHEME_L2CAP ="bt12cap";
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    public static final String PROTOCOL_SCHEME_BT_OBEX ="btgoep";
    public static final String PROTOCOL_SCHEME_TCP_OBEX ="tcpobex";
    private BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    Set<BluetoothDevice> bondDevice;
    private String address;
    public ArrayAdapter<String> adapter;
    public ArrayList<String> list =new ArrayList<String>();
    public Context mContext;
    private  BluetoothDevice mdevice=null;
    public Bluetooth(Context context){
        mContext =context;
    }
    //使蓝牙可见300秒
    private void discover() {
        Intent discoverIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        mContext.startActivity(discoverIntent);
    }

    //开始搜索附近蓝牙
    private void bluesearch() {
        bluetoothAdapter.enable();
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();

        }
        list.clear();
        bondDevice =bluetoothAdapter.getBondedDevices();

        for (BluetoothDevice device:bondDevice){
            String str =" 已配对完成  "+device.getName() +" "+device.getAddress();
            list.add(str);
            adapter.notifyDataSetChanged();
        }
        bluetoothAdapter.startDiscovery();
    }
    /*protected void onStart() {
        super.onStart();
        if(bluetoothAdapter==null){
            Toast.makeText(context,"蓝牙设备不可用",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled()){
            Intent enableIntent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,3);
        }
    }
    protected void onResume() {
        super.onResume();
        IntentFilter intent =new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(searchReceiver,intent);*/
       /* broadcastReceiver = new MyBroadcastReceiver(((share_plus_app)getApplicationContext()).getmManager(), ((share_plus_app)getApplicationContext()).getmChannel(),this);
        registerReceiver(broadcastReceiver, ((share_plus_app)getApplicationContext()).getIntentFilter());*/
  /*  }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(searchReceiver);
        //unregisterReceiver(broadcastReceiver);
    }*/


    //蓝牙搜索广播
    private final BroadcastReceiver searchReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action =intent.getAction();
            BluetoothDevice device=null;
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(address!=null&&address.equals(device.getAddress())){
                    mdevice =device;
                    Toast.makeText(context,"蓝牙连接成功",Toast.LENGTH_SHORT).show();
                }
               /* if(device.getBondState()==BluetoothDevice.BOND_NONE){
                    Toast.makeText(context,device.getName()+"",Toast.LENGTH_SHORT).show();
                    String str ="未配对完成 " +device.getName() +" " +device.getAddress();
                    if(list.indexOf(str) ==-1){
                        list.add(str);
                    }
                    adapter.notifyDataSetChanged();
                }*/
            }
        }
    };


    //获取本机蓝牙地址
    public static String getBtAddressByReflection() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Field field = null;
        try {
            field = BluetoothAdapter.class.getDeclaredField("mService");
            field.setAccessible(true);
            Object bluetoothManagerService = field.get(bluetoothAdapter);
            if (bluetoothManagerService == null) {
                return null;
            }
            Method method = bluetoothManagerService.getClass().getMethod("getAddress");
            if(method != null) {
                Object obj = method.invoke(bluetoothManagerService);
                if(obj != null) {
                    return obj.toString();
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    //开启蓝牙搜索，寻找目标蓝牙
    public void startbluesearch(String address1){
        address =address1;
        System.out.println(address);
        Toast.makeText(mContext,"进行蓝牙连接",Toast.LENGTH_SHORT).show();
        bluesearch();
    }
}
