package com.mobile.test.testmonitor.collector.information;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/20.
 */

public class InfoSim {
    private String phone1;
    private String phone2;
    private String net;
    private String mobile;
    private String signal;

    public InfoSim(){
        this.phone1 = "无";
        this.phone2 = "无";
        this.net = "无";
        this.mobile = "无";
        this.signal = "无";
    }

    public InfoSim(String phone1,String phone2,String net,String mobile,String signal){
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.net = net;
        this.mobile = mobile;
        this.signal = signal;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public String toInfoString(){
        Map result = new ArrayMap();
        result.put("卡1",this.phone1);
        result.put("卡2",this.phone2);
        result.put("网络制式",this.net);
        result.put("移动数据",this.mobile);
        result.put("信号强度",this.signal);
        return result.toString();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
