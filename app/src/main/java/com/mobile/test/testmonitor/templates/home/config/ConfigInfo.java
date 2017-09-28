package com.mobile.test.testmonitor.templates.home.config;

/**
 * Created by Administrator on 2017/8/19.
 */

public class ConfigInfo {
    private String interval;
    private boolean collectorSim;
    private boolean collectorWifi;
    private boolean collectorBt;
    private boolean collectorSetting;
    private boolean collectorApp;
    private boolean collectorElectricity;
    private boolean screenshot;

    public ConfigInfo() {
        this.interval = "5s";
        this.collectorSim = true;
        this.collectorWifi = true;
        this.collectorBt = true;
        this.collectorSetting = true;
        this.collectorApp = true;
        this.collectorElectricity = true;
        this.screenshot = true;
    }

    public ConfigInfo(String interval, boolean collectorSim, boolean collectorWifi,
                      boolean collectorBt, boolean collectorSetting,boolean collectorApp,
                      boolean collectorElectricity,boolean screenshot) {
        this.interval = interval;
        this.collectorSim = collectorSim;
        this.collectorWifi = collectorWifi;
        this.collectorBt = collectorBt;
        this.collectorSetting = collectorSetting;
        this.collectorApp = collectorApp;
        this.collectorElectricity = collectorElectricity;
        this.screenshot = screenshot;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public boolean isCollectorSim() {
        return collectorSim;
    }

    public void setCollectorSim(boolean collectorSim) {
        this.collectorSim = collectorSim;
    }

    public boolean isCollectorWifi() {
        return collectorWifi;
    }

    public void setCollectorWifi(boolean collectorWifi) {
        this.collectorWifi = collectorWifi;
    }

    public boolean isCollectorBt() {
        return collectorBt;
    }

    public void setCollectorBt(boolean collectorBt) {
        this.collectorBt = collectorBt;
    }

    public boolean isCollectorSetting() {
        return collectorSetting;
    }

    public void setCollectorSetting(boolean collectorSetting) {
        this.collectorSetting = collectorSetting;
    }

    public boolean isCollectorApp() {
        return collectorApp;
    }

    public void setCollectorApp(boolean collectorApp) {
        this.collectorApp = collectorApp;
    }

    public boolean isCollectorElectricity() {
        return collectorElectricity;
    }

    public void setCollectorElectricity(boolean collectorElectricity) {
        this.collectorElectricity = collectorElectricity;
    }

    public boolean isScreenshot() {
        return screenshot;
    }

    public void setScreenshot(boolean screenshot) {
        this.screenshot = screenshot;
    }
}
