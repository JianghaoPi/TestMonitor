package com.mobile.test.testmonitor.collector.information;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/20.
 */

public class InfoBt {
    private String state;
    private String type;
    private String signal;

    public InfoBt() {
        this.state = "无";
        this.type = "无";
        this.signal = "无";
    }

    public InfoBt(String state,String type,String signal) {
        this.state = state;
        this.type = type;
        this.signal = signal;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public String toInfoString(){
        Map result = new ArrayMap();
        result.put("状态",this.state);
        result.put("类型",this.type);
        result.put("信号强度",this.signal);
        return result.toString();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
