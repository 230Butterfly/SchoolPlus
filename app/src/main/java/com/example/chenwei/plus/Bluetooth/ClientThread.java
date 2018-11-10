package com.example.chenwei.plus.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Chenwei on 2018/6/19.
 */

public class ClientThread extends Thread {
    public BluetoothSocket socket;
    public BluetoothDevice device;
    public Context context;
    public String path;
    /*public ClientThread(BluetoothDevice device, Context context, String mpath){
        this.device =device;
        this.context=context;
        path=mpath;
    }*/
    public ClientThread(BluetoothDevice device, String mpath){
        this.device =device;
        path=mpath;
    }
    public void run(){
        try {
            Log.v("客户端","开始连接");
            socket =device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket.connect();
            if(socket==null){
                Log.v("客户端","连接失败");
                return;
            }
        } catch (IOException e) {
            try {
                Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                socket = (BluetoothSocket) m.invoke(device, 1);
                socket.connect();
            } catch (Exception e1) {
                Log.e("BLUE",e1.toString());
            }
        }
           // ((talking)context).getHandler().sendEmptyMessage(4);
            Log.v("客户端","连接成功");
            System.out.println("zhoulc client");
            try {
                File file =new File(path);
                if(!file.exists()){
                    Log.v("文件不存在",path);
                   // ((talking)context).getHandler().sendEmptyMessage(3);
                }
                long filelength =file.length();
                //BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //PrintStream writer = new PrintStream(socket.getOutputStream());
                FileInputStream in = new FileInputStream(file);
                OutputStream os = socket.getOutputStream();               //传输资源
                DataOutputStream writer = new DataOutputStream(os);
                DataInputStream reader =new DataInputStream(in);
                PrintStream writer1 = new PrintStream(socket.getOutputStream());
                writer1.println(file.length());
                writer1.flush();
                writer1.println(file.getName());
                writer1.flush();
                byte[] b = new byte[1024];
                long length = 0;
                double progress=0;
                while (true){
                    int read =0;
                    if(reader!=null){
                        read =reader.read(b);
                        length +=read;
                    }
                    if(read==-1){
                        break;
                    }
                    Log.v(String.valueOf(length),String.valueOf(filelength));
                    progress = length*100/filelength;
                    System.out.println("进度：" + progress);
                   // ((talking)context).setProgress(progress);
                   // ((talking)context).getHandler().sendEmptyMessage(6);
                    writer.write(b,0,read);
                    writer.flush();
                    if((long)length==filelength){
                        break;
                    }
                }
              //  ((talking)context).getHandler().sendEmptyMessage(2);
                writer.close();
                os.close();
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }

    }
}
