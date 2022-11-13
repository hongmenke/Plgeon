package com.firstapp.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.firstapp.R;
import com.firstapp.common.SendRequest;

import java.io.UnsupportedEncodingException;

public class SetUrl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_url);
        Button saveUrlButton = (Button) findViewById(R.id.saveUrlButton);
        SharedPreferences prefs = getSharedPreferences("com." + R.string.app_name + ".localstorage", MODE_PRIVATE);
        //如果已经设置url，则回显url
        String baseUrl = prefs.getString("basePath", null);
        if (baseUrl != null) {
            EditText urlTestView = findViewById(R.id.urlTextView);
            urlTestView.setText(baseUrl);
        }
        //SaveUrlButton按钮监听事件
        saveUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ((EditText) findViewById(R.id.urlTextView)).getText().toString();
                try {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("basePath", url);
                    editor.commit();
                    String basePath = prefs.getString("basePath", null);
                    if (basePath != null) {
                        Toast.makeText(SetUrl.this, "保存成功👌👌👌", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SetUrl.this, "保存失败，请重试😓😓😓", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Button testButton = (Button) findViewById(R.id.testButton);
        //TestButton按钮监听事件
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendRequest sendRequest = new SendRequest();
                try {
                    sendRequest.sendMsg(prefs,SetUrl.this,"测试消息",null);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}