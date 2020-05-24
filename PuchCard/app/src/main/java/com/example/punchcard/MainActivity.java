package com.example.punchcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.punchcard.adapter.PunchCardAdapter;
import com.example.punchcard.bean.PunchCard;
import com.example.punchcard.database.SQLiteHelper;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity" ;
    ListView listView;
    List<PunchCard> list;
    SQLiteHelper mSQLiteHelper;
    PunchCardAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //用于显示便签的列表
        listView = (ListView) findViewById(R.id.listview);
        ImageView add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewrecordActivity.class);
                startActivityForResult(intent, 1);
//                startActivity(intent);
            }
        });
        mSQLiteHelper= new SQLiteHelper(this); //创建数据库

        initData();
    }
    protected void initData() {
        showQueryData();
        //点击每条item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                PunchCard punchCard = list.get(position);
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                intent.putExtra("id", punchCard.getId());
                intent.putExtra("item", punchCard.getItem());
                intent.putExtra("times", punchCard.getTimes());
                intent.putExtra("activity", punchCard.getActivity());
                MainActivity.this.startActivityForResult(intent, 1);
            }
        });

        //长按每条item删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this)
                        .setMessage("是否删除此事件？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PunchCard notepadBean = list.get(position);
                                if(mSQLiteHelper.deleteData(notepadBean.getId())){
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(MainActivity.this,"删除成功",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
                return true;
            }
        });

    }
    public void showQueryData(){
        if (list!=null){
            list.clear();
        }
        //从数据库中查询出所有的数据
        list = mSQLiteHelper.query();
        adapter = new PunchCardAdapter(this, list);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2){
            showQueryData();
        }
    }
}