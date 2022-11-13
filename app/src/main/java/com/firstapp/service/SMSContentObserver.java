package com.firstapp.service;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

/**
 * 类说明：监听短信有两种方式：第一通过接受系统短息广播;第二监听短信数据库 
 * 本类是用来观察系统里短信收件箱的数据库的变化，只要短信收件箱数据库发生变化，就会触发该类。
 *
 */
public class SMSContentObserver extends ContentObserver {
    private static final int MSG_INBOX = 1;
    private Context mContext;
    private Handler mHandler; // 更新UI线程

    public SMSContentObserver(Context mContext,
            Handler mHandler) {
        super(mHandler); // 所有ContentObserver的派生类都需要调用该构造方法
        this.mContext = mContext;
        this.mHandler = mHandler;
    }

    /**
     * 当观察到的Uri发生变化时，回调该方法去处理。所有ContentObserver的派生类都需要重载该方法去处理逻辑
     * selfChange:回调后，其值一般为false，该参数意义不大
     */
    @Override
    public void onChange(boolean selfChange) {
        // TODO Auto-generated method stub
        super.onChange(selfChange);
        mHandler.obtainMessage(MSG_INBOX, "SMS Received").sendToTarget(); 
    }

}