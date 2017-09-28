package com.mobile.test.testmonitor.collector.information;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/20.
 */

public class InfoSetting {
    private String font;
    private String language;
    private String brightness;
    private String storage;
    private String memory;

    public InfoSetting(){
        this.font = "无";
        this.language = "无";
        this.brightness = "无";
        this.storage = "无";
        this.memory = "无";
    }

    public InfoSetting(String font,String language,String brightness,String storage,String memory){
        this.font = font;
        this.language = language;
        this.brightness = brightness;
        this.storage = storage;
        this.memory = memory;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String toInfoString(){
        Map result = new ArrayMap();
        result.put("字体",this.font);
        result.put("语言",this.language);
        result.put("亮度模式",this.brightness);
        result.put("内部存储空间",this.storage);
        result.put("内存使用情况",this.memory);
        return result.toString();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
