package com.mobile.test.testmonitor.collector;

import android.util.Log;

public class SystemInfoLogger {
    public static final String DEBUG_TAG = "SystemInfoDebug";
    public static final String INFO_TAG = "SystemInfoCollector";

    public static void d(String msg) {
        Log.d(DEBUG_TAG, msg);
    }

    // only 'i' would be collected when it dumps to log file
    public static void i(String msg) {
        Log.i(INFO_TAG, msg);
    }
}
