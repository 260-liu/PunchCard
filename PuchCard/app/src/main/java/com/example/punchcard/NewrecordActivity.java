package com.example.punchcard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.punchcard.database.SQLiteHelper;
import com.example.punchcard.utils.DBUtils;

/**
 * 新建一个打卡项目的activity
 */
public class NewrecordActivity extends AppCompatActivity {

    private static final String TAG = "NewrecordActivity" ;
    EditText editText;
    Button saveButton;
    SQLiteHelper mSQLiteHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newrecord);

        mSQLiteHelper = new SQLiteHelper(this);

        editText = (EditText)findViewById(R.id.item);
        saveButton = (Button)findViewById(R.id.saveButton);

        //点击保存按钮
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equalsIgnoreCase("")){
                    String item = editText.getText().toString().trim();
                    Log.d(TAG, "item == " + item);
                    if (mSQLiteHelper.insertData(item, DBUtils.getTime())){
                        showToast("保存成功");
                        Log.d(TAG, "=====保存成功");
                        setResult(2);
                        finish();
                    }else{
                        showToast("保存失败");
                        Log.d(TAG, "=====保存失败");
                    }
                }else{
                    showToast("输入为空");
                }
            }
        });
    }

    public void showToast(String message){
        Toast.makeText(NewrecordActivity.this,message,Toast.LENGTH_SHORT).show();
    }

}
