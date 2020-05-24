package com.example.punchcard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.punchcard.bean.PunchStatistics;
import com.example.punchcard.database.SQLiteHelper;
import com.example.punchcard.database.StatisticsDbHelper;
import com.example.punchcard.utils.DBUtils;
import com.example.punchcard.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 打卡界面的activity
 */
public class RecordActivity extends AppCompatActivity {

    private static final String TAG =  "RecordActivity";
    private SQLiteHelper mSQLiteHelper;
    private Button dakaButton;
    Intent intent;
    Integer activity, times;
    String id, item;
    private Button bt_calendar;
    private Context context = this;
    private int year;
    private int month;
    private int day;
    private List<PunchStatistics> statistics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //获得成员变量
        initView();
        //点击事件
        initClick();
        //获取数据
        getIntentData();


        //先根据activity判断是否已经打卡
        if (activity == 0) {
            dakaButton.setText("打卡");
        } else {
            dakaButton.setText("今日已打卡");
        }

        if (dakaButton.getText() == "打卡") {
            dakaButton.setOnClickListener(new View.OnClickListener() {//点击打卡
                @Override
                public void onClick(View v) {
                    activity = 1;
                    times++;
                    GenerateStatistic();
                    //修改成功
                    if (mSQLiteHelper.updateData(id, times, activity, DBUtils.getTime())) {
                        dakaButton.setText("今日已打卡");
                        setResult(2);
                    } else {
                        showToast("打卡失败");
                    }
                }
            });
        }else{   //打卡按钮没有被点击过
            dakaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showToast("今日已打卡，不能重复打卡！");
                }
            });
        }
    }

    public void showToast(String message){
        Toast.makeText(RecordActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    //获取从第一个界面传过来的数据
    private void getIntentData() {
        mSQLiteHelper = new SQLiteHelper(this);
        intent = getIntent();
        activity = intent.getIntExtra("activity",0);
        times = intent.getIntExtra("times",0);
        id = intent.getStringExtra("id");
        item  = intent.getStringExtra("item");
    }

    /*
    * 获得xml文件的视图id，并生成成员变量
     */
    private void initView(){
        dakaButton = (Button) findViewById(R.id.dakaButton);
        bt_calendar = this.findViewById(R.id.bt_calendar);
        year = DateUtils.getYear();
        month = DateUtils.getMonth();
        day = DateUtils.getCurrentDayOfMonth();
    }

    /*
    * 点击事件
     */
    private void initClick(){
        bt_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipCalendar();
            }
        });
    }

    /*
    *点击按钮，跳转到日历界面的意图
     */
    private void skipCalendar(){
        Intent intent = new Intent();
        intent.setClass(this,CalendarActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);

        //测试
        statistics = new ArrayList<>();
        StatisticsDbHelper op = new StatisticsDbHelper(context);
        statistics = op.GetStatistics(Integer.parseInt(id),year,month);
        Log.d(TAG,"+++++"+statistics.size()+item);
        if(statistics.size()>0){
            for(PunchStatistics statistic : statistics){
                Log.d(TAG,"++++++++"+statistic.getDay()+id);
            }
        }

    }
    /*
    * 当点击打卡后进行统计，生成记录
     */
    private void GenerateStatistic(){
        PunchStatistics newStatistic = new PunchStatistics(Integer.parseInt(id),year,month,day);
        StatisticsDbHelper DbHelper = new StatisticsDbHelper(context);
        DbHelper.PunchCard(newStatistic);
        DbHelper.close();
    }
}
