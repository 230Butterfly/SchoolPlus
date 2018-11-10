package com.example.chenwei.plus.Upload.bean;

/**
 * Created by Chenwei on 2018/9/19.
 */

public class Filehere {

    public String name;
    /**
     * 歌手
     */
    public String singer;
    /**
     * 歌曲名
     */
    public String song;
    /**
     * 歌曲的地址
     */
    public String path;
    /**
     * 歌曲长度
     */
    public int duration;
    /**
     * 歌曲的大小
     */
    public long size;

    public String state;

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getDuration() {
        return duration;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public String getSinger() {
        return singer;
    }

    public String getSong() {
        return song;
    }

    public String getState() {
        return state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
