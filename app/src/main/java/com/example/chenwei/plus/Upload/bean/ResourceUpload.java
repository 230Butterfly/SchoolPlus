package com.example.chenwei.plus.Upload.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;


public class ResourceUpload extends BmobObject {
    private String user;
    private BmobFile file;
    private String path;
    private String name;
    private String introduction;
    private int interger;
    private int comment;
    private String label;
    private String size;
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public ResourceUpload() {
        this.setTableName("ResourceUpload");
    }

    public ResourceUpload(String user,String path,String name,String introduction,int interger,int comment,String label,String size,String username){
        this.user=user;
        this.path=path;
        this.name=name;
        this.introduction=introduction;
        this.interger=interger;
        this.comment=comment;
        this.label=label;
        this.size=size;
        this.username=username;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public int getComment() {
        return comment;
    }

    public int getInterger() {
        return interger;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public void setInterger(int interger) {
        this.interger = interger;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}