package com.example.punchcard.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一些固定参数的类
 */
public class DBUtils {
    public static final String DATABASE_NAME = "PunchCard.db";  //数据库名
    public static final String DATABASE_TABLE = "Record";     //表名
    public static final int DATABASE_VERION = 1;                 //数据库版本

    //数据库表中的列名
    public static final String _ID = "id";               //id
    public static final String _ITEM = "item";          //打卡项目
    public static final String _TIMES = "times";        //打卡次数
    public static final String _ACTIVITY = "activity"; //记录今天有无打卡
    public static final String _DATE = "date";          //最新一次打卡的日期

    //获取当前日期
    public static final String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
