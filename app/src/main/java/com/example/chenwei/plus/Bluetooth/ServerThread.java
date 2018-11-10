package com.example.chenwei.plus.Bluetooth;

/**
 * Created by Chenwei on 2018/6/25.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.chenwei.plus.Share;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import static com.example.chenwei.plus.Bluetooth.Bluetooth.PROTOCOL_SCHEME_RFCOMM;

/**
 * Created by Chenwei on 2018/6/19.
 */

public class ServerThread extends Thread {
    public BluetoothServerSocket mserverSocket;
    public BluetoothAdapter bluetoothAdapter;
    public BluetoothSocket socket;
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    public Context context;
    public ServerThread(BluetoothAdapter bluetoothAdapter, Context context){
        this.bluetoothAdapter = bluetoothAdapter;
        this.context =context;
    }
    public ServerThread(BluetoothAdapter bluetoothAdapter){
        this.bluetoothAdapter = bluetoothAdapter;
    }
    public void run(){
        try {
            Log.v("服务端","开启");
            mserverSocket =bluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM, UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket =mserverSocket.accept();
            Log.v("服务器","连接成功");
            //((talking)context).getHandler().sendEmptyMessage(4);
            InputStream in = socket.getInputStream();
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(in)); //读取服务器的相应头部
            String firstLineOfResponse = reader1.readLine();
            long filelength =Integer.parseInt(firstLineOfResponse);
            String filename = reader1.readLine();
            Log.v(String.valueOf(filename),String.valueOf(filelength));
            File file1 =new File(Environment.getExternalStorageDirectory().getPath() + "/" + filename);
            //判断文件是否存在，存在就删除
            if (file1.exists()){
                file1.delete();
            }
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File file2 =new File(Environment.getExternalStorageDirectory().getPath() + "/" + filename);
            if(file2.exists()){
            }else {
                //((talking)context).getHandler().sendEmptyMessage(5);
            }
            FileOutputStream out = new FileOutputStream(file2);
            DataInputStream dis = new DataInputStream(in);
            byte[] b = new byte[1024];                          //如果没出错  接受相应数据文件
            int len = 0;
            int transLen =0;
            double progress =0;
            while ((len=dis.read(b))!=-1){
                transLen +=len;
                Log.v(String.valueOf(transLen),String.valueOf(filelength));
                progress = transLen*100/filelength;
                System.out.println("进度：" + progress);
                int   s1   =   (int)progress;
                ((Share)context).getNear_resource_detail().update(s1);
               // ((talking)context).setProgress(progress);
               // ((talking)context).getHandler().sendEmptyMessage(6);

                out.write(b,0,len);
                out.flush();
                if(transLen==filelength){
                    break;
                }
            }
           // ((talking)context).getHandler().sendEmptyMessage(1);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
