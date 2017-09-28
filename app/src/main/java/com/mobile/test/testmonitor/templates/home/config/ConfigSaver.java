package com.mobile.test.testmonitor.templates.home.config;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigSaver {
    private SharedPreferences spf;
    private static ConfigInfo mConfig;
    private static final String PREFERENCE_NAME = "config";
    private static final String KEYS_INTERVAL = "interval";
    private static final String KEYS_COLLECTOR_SIM = "collector_sim";
    private static final String KEYS_COLLECTOR_WIFI = "collector_wifi";
    private static final String KEYS_COLLECTOR_BT = "collector_bt";
    private static final String KEYS_COLLECTOR_SETTING = "collector_setting";
    private static final String KEYS_COLLECTOR_APP = "collector_app";
    private static final String KEYS_COLLECTOR_ELECTRICITY = "collector_electricity";
    private static final String KEYS_SCREENSHOT_RECORD = "screenshot_record";

    public ConfigSaver(Context context) {
        spf = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_APPEND);

        mConfig = new ConfigInfo(spf.getString(KEYS_INTERVAL,"5s"),
                spf.getBoolean(KEYS_COLLECTOR_SIM,true),spf.getBoolean(KEYS_COLLECTOR_WIFI,true),
                spf.getBoolean(KEYS_COLLECTOR_BT,true),spf.getBoolean(KEYS_COLLECTOR_SETTING,true),
                spf.getBoolean(KEYS_COLLECTOR_APP,true),spf.getBoolean(KEYS_COLLECTOR_ELECTRICITY,true),
                spf.getBoolean(KEYS_SCREENSHOT_RECORD,true));
    }

    //保存配置
    public int saveConfig(ConfigInfo configInfo) {
        SharedPreferences.Editor editor = spf.edit();
        editor.putString(KEYS_INTERVAL,configInfo.getInterval());
        editor.putBoolean(KEYS_COLLECTOR_SIM,configInfo.isCollectorSim());
        editor.putBoolean(KEYS_COLLECTOR_WIFI,configInfo.isCollectorWifi());
        editor.putBoolean(KEYS_COLLECTOR_BT,configInfo.isCollectorBt());
        editor.putBoolean(KEYS_COLLECTOR_SETTING,configInfo.isCollectorSetting());
        editor.putBoolean(KEYS_COLLECTOR_APP,configInfo.isCollectorApp());
        editor.putBoolean(KEYS_COLLECTOR_ELECTRICITY,configInfo.isCollectorElectricity());
        editor.putBoolean(KEYS_SCREENSHOT_RECORD,configInfo.isScreenshot());
        editor.apply();

        mConfig = configInfo;
        return 1;
    }

    //获取配置
    public ConfigInfo getConfig() {
        return mConfig;
    }
}
