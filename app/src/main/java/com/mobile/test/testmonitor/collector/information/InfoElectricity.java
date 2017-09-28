package com.mobile.test.testmonitor.collector.information;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/20.
 */

public class InfoElectricity {
    private String model;
    private String electricity;

    public InfoElectricity() {
        this.model = "无";
        this.electricity = "无";
    }

    public InfoElectricity(String model,String electricity) {
        this.model = model;
        this.electricity = electricity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String toInfoString(){
        Map result = new ArrayMap();
        result.put("模式",this.model);
        result.put("电量",this.electricity);
        return result.toString();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
