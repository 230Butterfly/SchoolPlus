package com.example.chenwei.plus.Tool;

import java.io.Serializable;

/**
 * Created by Chenwei on 2018/8/15.
 */

public class Near_resource_information implements Serializable{
    private String user_name;
    private String user_id;
    private String resource_name;
    private int resource_grade;
    private int price;
    private int distance;
    private String path;
    private String label;
    private String time;
    private String size;
    private String introduction;
    private String file_url;



    public Near_resource_information(){

    }
    public Near_resource_information(String person_name,String resource_name,int resource_grade,int price,int distance,String label,String time,String size,String introduction,String path,String user_name,String url){
        this.user_id=person_name;
        this.resource_name=resource_name;
        this.resource_grade=resource_grade;
        this.price=price;
        this.distance=distance;
        this.label=label;
        this.time =time;
        this.size=size;
        this.introduction =introduction;
        this.path =path;
        this.user_name=user_name;
        this.file_url=url;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSize() {
        return size;
    }

    public String getTime() {
        return time;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setUser_id(String person_name) {
        this.user_id = person_name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setResource_grade(int resource_grade) {
        this.resource_grade = resource_grade;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        return price;
    }

    public int getResource_grade() {
        return resource_grade;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getResource_name() {
        return resource_name;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }
}
