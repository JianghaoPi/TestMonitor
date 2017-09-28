package com.mobile.test.testmonitor.collector;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SystemInfoService extends Service {
    private Timer mTimer;
    private SystemInfo info;
    private SystemInfoListener mListener;

    public class SystemInfoBinder extends Binder {
        public SystemInfoService getService() {
            return SystemInfoService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SystemInfoBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    public void setContext(Context context) {
        info = new SystemInfo(context);
    }

    public void startTimer(SystemInfoListener listener, long period) {
        mListener = listener;
        mTimer = new Timer();

        Map data = info.getSystemInfo();
        SystemInfoLogger.i("001@" + JSON.toJSONString(data));
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Map data = info.getSystemInfo();
                SystemInfoLogger.i("001@" + JSON.toJSONString(data));
                mListener.onChange(data);
            }
        }, 0, period);
    }

}
