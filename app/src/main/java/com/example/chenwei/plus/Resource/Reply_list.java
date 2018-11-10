package com.example.chenwei.plus.Resource;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Chenwei on 2018/8/10.
 */

public class Reply_list extends BmobObject{
    private BmobFile head_photo;
    private String name;
    private String reply;
    private int grade;
    private String data;
    public Reply_list(BmobFile head_photo,String name,String reply,int grade,String data){
        this.head_photo=head_photo;
        this.name=name;
        this.reply=reply;
        this.grade=grade;
        this.data=data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setHead_photo(BmobFile head_photo) {
        this.head_photo = head_photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public BmobFile getHead_photo() {
        return head_photo;
    }

    public int getGrade() {
        return grade;
    }

    public String getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getReply() {
        return reply;
    }
}
