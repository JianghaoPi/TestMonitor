package com.mobile.test.testmonitor.collector;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.mobile.test.testmonitor.templates.home.config.ConfigInfo;
import com.mobile.test.testmonitor.templates.home.config.ConfigSaver;

import java.util.Map;


public class SystemInfo {
    private ConfigSaver mConfig;
    private Map<String, CollectorBase> mInformations;

    public SystemInfo(Context context) {
        mConfig = new ConfigSaver(context);

        mInformations = new ArrayMap<>();
        mInformations.put("Settings", new CollectorSetting(context));
        mInformations.put("Electricity", new CollectorElectricity(context));
        mInformations.put("BT", new CollectorBluetooth(context));
        mInformations.put("WIFI", new CollectorWifi(context));
        mInformations.put("APP", new CollectorApp(context));
        mInformations.put("SIM", new CollectorSim(context));
    }

    public Map getSystemInfo() {
        Map<String, Object> result = new ArrayMap<>();
        ConfigInfo configInfo = mConfig.getConfig();
        if(configInfo.isCollectorSetting()){
            result.put("Settings", mInformations.get("Settings").updateInformation());
        }else{
            result.put("Settings", mInformations.get("Settings").defaultInformation());
        }
        if(configInfo.isCollectorElectricity()){
            result.put("Electricity", mInformations.get("Electricity").updateInformation());
        }else{
            result.put("Electricity", mInformations.get("Electricity").defaultInformation());
        }
        if(configInfo.isCollectorBt()){
            result.put("BT", mInformations.get("BT").updateInformation());
        }else{
            result.put("BT", mInformations.get("BT").defaultInformation());
        }
        if(configInfo.isCollectorWifi()){
            result.put("WIFI", mInformations.get("WIFI").updateInformation());
        }else{
            result.put("WIFI", mInformations.get("WIFI").defaultInformation());
        }
        if(configInfo.isCollectorApp()){
            result.put("APP", mInformations.get("APP").updateInformation());
        }else{
            result.put("APP", mInformations.get("APP").defaultInformation());
        }
        if(configInfo.isCollectorSim()){
            result.put("SIM", mInformations.get("SIM").updateInformation());
        }else{
            result.put("SIM", mInformations.get("SIM").defaultInformation());
        }
        return result;
    }
}
