package com.mobile.test.testmonitor.collector;

import android.content.Context;
import android.os.BatteryManager;
import android.os.PowerManager;

import com.mobile.test.testmonitor.collector.information.InfoElectricity;

/**
 * Created by Administrator on 2017/8/20.
 */

public class CollectorElectricity extends CollectorBase {
    private InfoElectricity infoElectricity;
    private BatteryManager batteryManager;
    private PowerManager powerManager;

    public CollectorElectricity(Context context) {
        super(context);
        mCollectorTag = "Electricity";
        infoElectricity = new InfoElectricity();
        powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        batteryManager = (BatteryManager) mContext.getSystemService(Context.BATTERY_SERVICE);
    }

    public InfoElectricity updateInformation() {
        int batLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        this.infoElectricity.setElectricity(String.valueOf(batLevel));
        this.infoElectricity.setModel(powerManager.isPowerSaveMode() ? "省电模式" : "普通模式");
        return this.infoElectricity;
    }

    public InfoElectricity defaultInformation(){
        return this.infoElectricity;
    }
}
