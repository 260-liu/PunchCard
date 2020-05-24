package com.example.punchcard.bean;

/**
 * 基础表的bean类
 */
public class PunchCard {
    private String id;
    private String item;        //打卡项目
    private Integer times;      //打卡次数
    private Integer activity;  //0：未打卡，1：已经打卡
    private String date;       //保存记录的时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getActivity() {
        return activity;
    }

    public void setActivity(Integer activity) {
        this.activity = activity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
