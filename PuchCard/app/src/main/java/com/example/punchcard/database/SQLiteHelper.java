package com.example.punchcard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.punchcard.bean.PunchCard;
import com.example.punchcard.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private SQLiteDatabase sqLiteDatabase;

    //创建数据库
    public SQLiteHelper(Context context){
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERION);
        sqLiteDatabase = this.getWritableDatabase();
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ DBUtils.DATABASE_TABLE+"("+ DBUtils._ID+
                " integer primary key autoincrement,"+ DBUtils._ITEM +
                " text," + DBUtils._TIMES+ " integer," + DBUtils._ACTIVITY + " integer," + DBUtils._DATE + " text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    //添加数据，新建一个项目。新建项目时times和activity默认为0
    public boolean insertData(String item, String date){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUtils._ITEM,item);
        contentValues.put(DBUtils._TIMES,0);
        contentValues.put(DBUtils._ACTIVITY,0);
        contentValues.put(DBUtils._DATE,date);
        return
                sqLiteDatabase.insert(DBUtils.DATABASE_TABLE,null,contentValues)>0;
    }

    //删除数据，根据id删除
    public boolean deleteData(String id){
        String sql= DBUtils._ID+"=?";
        String[] contentValuesArray=new String[]{String.valueOf(id)};
        return
                sqLiteDatabase.delete(DBUtils.DATABASE_TABLE,sql,contentValuesArray)>0;
    }

    //修改数据
    public Boolean updateData(String id, Integer times ,Integer activity, String date){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUtils._TIMES, times);
        contentValues.put(DBUtils._ACTIVITY, activity);
        contentValues.put(DBUtils._DATE, date);
        //contentValues.put(DBUtils._ITEM,item);
        String sql= DBUtils._ID+"=?";
        String[] strings=new String[]{id};
        return
                sqLiteDatabase.update(DBUtils.DATABASE_TABLE,contentValues,sql,strings)>0;

        /*String sql = "update " + DBUtils.DATABASE_TABLE + " set times=" + times + ",set activity=" + activity +" where id=" + id;
        sqLiteDatabase.execSQL(sql);*/
    }

    //查询数据
    public List<PunchCard> query(){
        List<PunchCard> list = new ArrayList<PunchCard>();
        Cursor cursor=sqLiteDatabase.query(DBUtils.DATABASE_TABLE,null,null,null,
                null,null, DBUtils._ID+" desc");
        if (cursor!=null){
            while (cursor.moveToNext()){
                PunchCard info = new PunchCard();

                String id = String.valueOf(cursor.getInt(cursor.getColumnIndex(DBUtils._ID)));
                String item = cursor.getString(cursor.getColumnIndex(DBUtils._ITEM));
                Integer times = cursor.getInt(cursor.getColumnIndex(DBUtils._TIMES));
                Integer activity = cursor.getInt(cursor.getColumnIndex(DBUtils._ACTIVITY));
                String date = cursor.getString(cursor.getColumnIndex(DBUtils._DATE));

                //将数据库存储的日期跟当前日期进行比较，不能直接用“==”进行比较，“==”比较的是地址
                if(!date.equals(DBUtils.getTime()) ) {
                    //不一样，说明已经过了一天，将activity置0，且将数据库中的activity、date更新
                    activity = 0;
                    this.updateData(id, times, 0, DBUtils.getTime());
                }

                info.setId(id);
                info.setItem(item);
                info.setTimes(times);
                info.setActivity(activity);
                info.setDate(date);

                list.add(info);
            }
            cursor.close();
        }
        return list;
    }
}
