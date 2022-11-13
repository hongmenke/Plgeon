package com.firstapp.common;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSON;
import com.firstapp.MainActivity;
import com.firstapp.R;
import com.firstapp.View.SetUrl;
import lombok.Data;
import okhttp3.*;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

@Data
public class SendRequest {
    public static void sendRequest(URL url) throws SQLException {
        HttpURLConnection connection=null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP错误code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendMsgRequest(SharedPreferences prefs, Activity activity,String msg) throws SQLException, MalformedURLException, UnsupportedEncodingException {
        String urlString = prefs.getString("basePath",null);
        if(urlString == null && activity != null) {
            Toast.makeText(activity, "未保存url，请先设置url", Toast.LENGTH_LONG).show();
            return;
        }
        msg = URLEncoder.encode(msg, "UTF-8");
        URL url = new URL(urlString+msg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK && activity != null) {
                        Toast.makeText(activity, "HTTP错误code: " + responseCode, Toast.LENGTH_LONG).show();
                        throw new IOException("HTTP错误code: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //OkHttp
    public void sendMsg(SharedPreferences prefs, Activity activity,String msg,String title) throws UnsupportedEncodingException {
        String urlString = prefs.getString("basePath",null);
        if(urlString == null){
            Toast.makeText(activity, "未保存url，请先设置url", Toast.LENGTH_LONG).show();
            return;
        }
        OkHttpClient httpClient = new OkHttpClient();
        Map<String,String> json = new HashMap<>();
        if(title == null){
            title = "Pigeon";
        }
        json.put("title", title);
        json.put("body",msg);
        String jsonString = JSON.toJSONString(json);
        RequestBody jsonBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);

        Request request = new Request.Builder()
                .url(urlString)
                .post(jsonBody)
                .build();
        Call call = httpClient.newCall(request);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    if(activity != null) {
                        if (response.code() == 200) {
                            Looper.prepare();
                            Toast.makeText(activity, "成功👌", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(activity, "出问题了😓", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
    //Retrofit
//    public  void sendMsgRetrofit(SharedPreferences prefs, Activity activity,String msg){
//        String urlString = prefs.getString("basePath",null);
//        if(urlString == null){
//            Toast.makeText(activity, "未保存url，请先设置url", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//    }
}
