package com.mobile.test.testmonitor.collector;

import android.os.Environment;
import android.os.FileObserver;

import com.mobile.test.testmonitor.util.DateFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/20.
 */

public class ScreenshotRecord {
    private boolean isScreenshot;
    private ArrayList record;
    private CustomFileObserver customFileObserver;
    private static final String PATH = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/Screenshots";

    public ScreenshotRecord(ArrayList record) {
        this.isScreenshot = false;
        this.record = record;
        this.customFileObserver = new CustomFileObserver(PATH, 256);
    }

    public void startWatching(){
        this.customFileObserver.startWatching();
    }

    public void stopWatching(){
        try{
            this.customFileObserver.stopWatching();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void resetScreenshotRecord() {
        this.isScreenshot = false;
    }

    public boolean isScreenshot() {
        return isScreenshot;
    }

    public void setScreenshot(boolean screenshot) {
        isScreenshot = screenshot;
    }


    private class CustomFileObserver extends FileObserver {
        private String mPath;

        public CustomFileObserver(String path) {
            super(path);
            this.mPath = path;
        }

        public CustomFileObserver(String path, int mask) {
            super(path, mask);
            this.mPath = path;
        }

        @Override
        public void onEvent(int event, String path) {
            // 监听到事件，做一些过滤去重处理操作
            if(event == 256){
                setScreenshot(true);
                SystemInfoLogger.i("003@截屏事件");
                record.add(new DateFormat().cTMToyyyyMMddHHmmssHasSign(System.currentTimeMillis()));
            }
        }
    }
}
