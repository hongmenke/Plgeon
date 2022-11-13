package com.firstapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.firstapp.View.SetUrl;
import com.firstapp.common.SendRequest;
import com.firstapp.service.MyService;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SmsObserver smsObserver;
    private final Handler smsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //如果未获取读取权限，则申请
        String[] permissions = new String[]{Manifest.permission.READ_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECEIVE_SMS};
        ActivityCompat.requestPermissions(this,permissions,1);

        SharedPreferences prefs = getSharedPreferences("com." + R.string.app_name + ".localstorage", MODE_PRIVATE);
        String basePath = prefs.getString("basePath", null);
        if(basePath == null) {
            TextView isHaveUrl = (TextView) findViewById(R.id.isHaveUrl);
            isHaveUrl.setText("未设置url，请先设置url");
        }
        //SetUrlButton按钮
        Button setUrlButton = (Button) findViewById(R.id.setUrlButton);
        Intent intent = new Intent(this, SetUrl.class);
        setUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        Button sendMsgButton = (Button) findViewById(R.id.sendMsgButton);
        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText msgEditText = (EditText) findViewById(R.id.msgEditText);
                String msg = msgEditText.getText().toString();
                SendRequest sendRequest = new SendRequest();
                try {
                    sendRequest.sendMsg(prefs,MainActivity.this,msg,null);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        Button startButton = (Button) findViewById(R.id.startService);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startForegroundService(new Intent(MainActivity.this, MyService.class));}
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //处理取得权限和后的业务逻辑
                } else {
                    //未取得权限的业务逻辑
                    Toast.makeText(this, "你为什么不给权限😓😓😓", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 2:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //处理取得权限和后的业务逻辑
                } else {
                    //未取得权限的业务逻辑
                    Toast.makeText(this, "你为什么还不给权限😓😓😓", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    class SmsObserver extends ContentObserver {
        private Uri uri;

        public SmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);
            if (uri.toString().equals("content://sms/raw")) {
                return;
            }
            //读取短信操作
        }
    }
}