package com.example.chenwei.plus.Login;
//import com.amap.api.maps.model.LatLng;

import cn.bmob.v3.BmobUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Chenwei on 2018/6/1.
 */

public class Customer extends BmobUser {
    /*private String name;
    private String password;*/
    private String phone_number;
    // private String email;
    private String integral;
    private BmobFile head_portrait;
    private BmobFile icon;
 //   private LatLng latLng;
    private int distance;
    private int file_number;
    private int goal;

    public void setFile_number(int file_number) {
        this.file_number = file_number;
    }

    public int getFile_number() {
        return file_number;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

//    public void setLatLng(LatLng latLng) {
//        this.latLng = latLng;
//    }
//
//    public LatLng getLatLng() {
//        return latLng;
//    }

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

    /*public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }*/

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

   /* public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }*/

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public BmobFile getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(BmobFile head_portrait) {
        this.head_portrait = head_portrait;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }
}
