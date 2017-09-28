package com.mobile.test.testmonitor.collector;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.mobile.test.testmonitor.collector.information.InfoWifi;

public class CollectorWifi extends CollectorBase {
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private InfoWifi infoWifi;

    CollectorWifi(Context context) {
        super(context);
        mCollectorTag = "Wifi";
        mWifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        infoWifi = new InfoWifi();
    }

    private boolean is5GHz(int freq) {
        return freq > 4900 && freq < 5900;
    }

    public InfoWifi updateInformation(){
        if (mWifiManager.isWifiEnabled()) {
            this.infoWifi.setState("ON");
            mWifiInfo = mWifiManager.getConnectionInfo();
            int freq = mWifiInfo.getFrequency();
            this.infoWifi.setType(is5GHz(freq) ? "5GHz": "2.4GHz");
            this.infoWifi.setSignal("SSID("+mWifiInfo.getSSID()+"),RSSI("+mWifiInfo.getRssi()+")");
        }
        else {
            infoWifi.setState("OFF");
            infoWifi.setState("无");
            infoWifi.setState("无");
        }
        return this.infoWifi;
    }

    public InfoWifi defaultInformation(){
        return this.infoWifi;
    }
}
