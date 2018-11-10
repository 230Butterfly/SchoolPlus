package com.example.chenwei.plus;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.admom.mygreendaotest.DaoMaster;
import com.admom.mygreendaotest.DaoSession;
import com.example.chenwei.plus.Bluetooth.ClientThread;
import com.example.chenwei.plus.Bluetooth.ServerThread;
import com.example.chenwei.plus.Messages.MessageDetails;
import com.example.chenwei.plus.Near.Customer;
import com.example.chenwei.plus.Near.Near_resource_detail;
import com.example.chenwei.plus.Near.Near_resource_fragment;
import com.example.chenwei.plus.Tool.Near_resource_information;
import net.vidageek.mirror.dsl
        .Mirror;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import okhttp3.Address;

import static android.content.Context.NOTIFICATION_SERVICE;
import static cn.bmob.v3.Bmob.getApplicationContext;
import static cn.bmob.v3.BmobRealTimeData.TAG;

/**
 * Created by Chenwei on 2018/10/9.
 */

public class Share extends Application {

    private static DaoSession daoSession ;
    private Customer me= null;
    private Share share;
    private String useid;
    private ArrayList<Near_resource_information> reply_lists =new ArrayList<>();
    private static ArrayList<Customer> customers = new ArrayList<Customer>();
    private Activity sActivity=null;
    private Context sContext=null;
    private Near_resource_fragment sfragment;
    private String path;

    private Near_resource_detail near_resource_detail;

    public void setNear_resource_detail(Near_resource_detail near_resource_detail) {
        this.near_resource_detail = near_resource_detail;
    }

    public Near_resource_detail getNear_resource_detail() {
        return near_resource_detail;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static void setCustomers(ArrayList<Customer> customers) {
        Share.customers = customers;
    }

    public static ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setReply_lists(ArrayList<Near_resource_information> reply_lists) {
        this.reply_lists = reply_lists;
    }

    public ArrayList<Near_resource_information> getReply_lists() {
        return reply_lists;
    }

    public void onCreate() {
        // 程序开始的时候执行
        super.onCreate();
        share = this;
        if(bluetoothAdapter==null){
            Toast.makeText(getApplicationContext(),"蓝牙设备不可用",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled()){
            Intent enableIntent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            Toast.makeText(getApplicationContext(),"蓝牙设备未开启",Toast.LENGTH_SHORT).show();
        }
        IntentFilter intent =new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(searchReceiver,intent);
        Bmob.initialize(this, "0fe7c7cb6eff2cf5a1f4d40288246b42");
        setupDatabase();
        //连接服务器
    }

    public void onTerminate() {
        // 程序终止的时候执行
        Log.d(TAG, "onTerminate");
        super.onTerminate();
        unregisterReceiver(searchReceiver);
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }

    public void setSfragment(Near_resource_fragment sfragment) {
        this.sfragment = sfragment;
    }

    public Near_resource_fragment getSfragment() {
        return sfragment;
    }

    public void setsActivity(Activity sActivity) {
        this.sActivity = sActivity;
    }

    public Activity getsActivity() {
        return sActivity;
    }

    public void setsContext(Context sContext) {
        this.sContext = sContext;
    }

    public Context getsContext() {
        return sContext;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    private void setupDatabase() {
        //创建数据库shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "file.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
        Log.e("base",daoSession.toString());
    }

    public static DaoSession getDaoInstant() {
        Log.e("base",daoSession.toString());
        return daoSession;
    }
    //蓝牙传输
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private int sign = 1;
    //对方蓝牙地址
    private String address;
    //对方蓝牙设备
    private BluetoothDevice mdevice;

    public ArrayList<String> list = new ArrayList<String>();
    Set<BluetoothDevice> bondDevice;
    public ArrayAdapter<String> adapter;

    //接受
    public void request_resource(String id ,String username,final String path) {
        Log.v("发起请求","1111111111111");
        Log.v(id,username);
        sendmessage(id,username,path,0);
    }

    private void discover() {
        Intent discoverIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverIntent);
    }

    public void ready_recive(String id,String name1) {
        Log.v("开启服务端","22222222");
        Toast.makeText(getApplicationContext(),"准备接受",Toast.LENGTH_SHORT).show();
        ServerThread serverThread = new ServerThread(bluetoothAdapter,getApplicationContext());
        //开启服务端，等待接受文件
        serverThread.start();
        discover();
        //发送接受准备和本机蓝牙地址
        Log.v("发送自己地址","222233333");
        Log.v(getBtAddressByReflection(),"蓝牙地址");
        ((Share)getApplicationContext()).sendmessage(id,name1,getBtAddressByReflection(),2);

    }
    public void startblusearch(String address1) {
        address =address1;
        System.out.println(address);
        bluesearch();
    }

    //获取本机蓝牙地址
    public String getBtAddressByReflection() {
        return android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), "bluetooth_address");

        /*BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
        return null;*/
    }


    //发送
    //蓝牙搜索
    private void bluesearch() {
        discover();
        bluetoothAdapter.enable();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();

        }
        list.clear();
        bondDevice = bluetoothAdapter.getBondedDevices();

        for (BluetoothDevice device : bondDevice) {
            String str = " 已配对完成  " + device.getName() + " " + device.getAddress();
            list.add(str);
           // adapter.notifyDataSetChanged();
        }
        Log.v("搜索周边蓝牙","111");
        bluetoothAdapter.startDiscovery();

    }

    //蓝牙搜索广播
    private final BroadcastReceiver searchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = null;
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.v("蓝牙地址:",device.getAddress());
                if (device.getAddress().equals("04:B1:67:5F:CC:D9")){
                    mdevice = device;
                    Log.v("搜索成功","开启客户端");
                    Toast.makeText(getApplicationContext(),"准备传输",Toast.LENGTH_SHORT).show();
                    ClientThread clientThread = new ClientThread(mdevice, path);
                    clientThread.start();
                }
               /* if (address != null && address.equals(device.getAddress())) {
                    mdevice = device;
                    Log.v("搜索成功","开启客户端");
                    Toast.makeText(getApplicationContext(),"准备传输",Toast.LENGTH_SHORT).show();
                    //ClientThread clientThread = new ClientThread(mdevice, path);
                    //clientThread.start();
                }*/
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

    //信息发送
    //连接服务器
    public void Bmobconnect() {
        me = BmobUser.getCurrentUser(Customer.class);
        Bmob.initialize(this, "0fe7c7cb6eff2cf5a1f4d40288246b42");
        BmobIM.init(this);
        BmobIM.registerDefaultMessageHandler(new IMMessageHandler());
        BmobIM.connect(me.getObjectId(), new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    //isConnect = true;
                } else {
                    Log.i("TAG", e.getMessage() + "  " + e.getErrorCode());
                }
            }


        });
    }

    //发送信息
    public void sendmessage(final String userid, final String name, final String message, final int i) {

        BmobIMUserInfo info = new BmobIMUserInfo();
        //info.setAvatar("填写接收者的头像");
        info.setUserId(userid);
        info.setName(name);
        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {

            @Override
            public void done(final BmobIMConversation c, BmobException e) {
                if (e == null) {
                    boolean isOpenConversation = true;
                    //在此跳转到聊天页面或者直接转化
                    BmobIMConversation mBmobIMConversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
                    //tv_message.append("发送者："+et_message.getText().toString()+"\n");
                    BmobIMTextMessage msg = new BmobIMTextMessage();
                    msg.setContent(i + "#" + me.getUsername() + "#" + me.getIcon().getUrl() + "#" + message);
                    mBmobIMConversation.sendMessage(msg, new MessageSendListener() {
                        @Override
                        public void done(BmobIMMessage msg, BmobException e) {
                            if (e == null) {
                                Log.v("发送成功","111");
                                if (i == 0) {
                                    Toast.makeText(getApplicationContext(), "等待对方确认", Toast.LENGTH_LONG).show();

                                } else if (i == 1) {
                                                    /*Intent i =new Intent(getApplicationContext(),talking.class);
                                                   i.putExtra("I",1);
                                                    startActivity(i);*/

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "发送失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "开启会话出错" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
class IMMessageHandler extends BmobIMMessageHandler {

    private String id;
    public void onMessageReceive(MessageEvent messageEvent) {
        super.onMessageReceive(messageEvent);
        //在线消息
        String mes[] = messageEvent.getMessage().getContent().split("#", 4);
        id = messageEvent.getFromUserInfo().getUserId();
        Log.v("收到信息"+messageEvent.getFromUserInfo().getUserId(), messageEvent.getFromUserInfo().getName()+" " +mes[0]);
        if (mes[0].equals("0")) {
            Log.v("收到请求","1111111");
            ((Share)getApplicationContext()).setPath(mes[3]);
            Log.v(mes[3],"文件地址");
            ((Share) getApplicationContext()).sendmessage(messageEvent.getFromUserInfo().getUserId(), mes[1], "789", 1);
        } else if (mes[0].equals("1")) {
            Log.v("收到回复","1111111");
            ((Share) getApplicationContext()).ready_recive(messageEvent.getFromUserInfo().getUserId(),mes[1]);
        } else if (mes[0].equals("2")) {
            Log.v("收到地址","进行蓝牙搜索");
            ((Share) getApplicationContext()).startblusearch(mes[3]);
        } else {
            if (((Share) getApplicationContext()).getsActivity()!=null&&((Share) getApplicationContext()).getsActivity().getLocalClassName().equals("Messages.MessageDetails")) {
                ((MessageDetails)((Share)getApplicationContext()).getsContext()).recieve_information(mes[3]);
            } else {
                final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                final String channelId = "channelId";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelName = "下载资源";
                    NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                }
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
                builder.setSmallIcon(R.mipmap.share);
                builder.setContentTitle("您有一条新信息");
                builder.setContentText("   from：" + messageEvent.getFromUserInfo().getName());
                Intent intent = new Intent(getApplicationContext(),
                        MessageDetails.class);
                intent.putExtra("date",messageEvent.getFromUserInfo().getUserId());
                intent.putExtra("name",mes[1]);
                intent.putExtra("information",mes[3]);
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        getApplicationContext(), 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(pendingIntent);
                // 发送通知
                Notification notification = builder.build();
                notificationManager.notify(1, notification);
            }
        }
    }


    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        super.onOfflineReceive(offlineMessageEvent);
        //离线消息
    }

}


    /*public class NotificationBroadcast extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();//动作
            collapseStatusBar(context);//收起通知栏
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//必须添加，避免重复打开
            if (action.equals("action.view")) {
                startActivity(i);
            }else  if(action.equals("action")){
                startActivity(i);
            }
        }

        public void collapseStatusBar(Context context) {
            try {
                @SuppressLint("WrongConstant") Object statusBarManager = context.getSystemService("statusbar");
                Method collapse;
                if (Build.VERSION.SDK_INT <= 16) {
                    collapse = statusBarManager.getClass().getMethod("collapse");
                } else {
                    collapse = statusBarManager.getClass().getMethod("collapsePanels");
                }
                collapse.invoke(statusBarManager);
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
    }*/

