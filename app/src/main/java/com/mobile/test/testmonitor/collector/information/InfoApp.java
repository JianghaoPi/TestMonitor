package com.mobile.test.testmonitor.collector.information;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/8/20.
 */

public class InfoApp {
    private String number;
    private List<Map<String, String>> detail;

    public InfoApp() {
        this.number = "无";
        detail = new ArrayList<>();
    }

    public InfoApp(String number, List<Map<String, String>> detail) {
        this.number = number;
        this.detail = detail;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Map<String, String>> getDetail() {
        return detail;
    }

    public void setDetail(List<Map<String, String>> detail) {
        this.detail = detail;
    }

    public void addDetail(Map<String, String> one){
        this.detail.add(one);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String detailString(){
        StringBuilder result = new StringBuilder();
        for (Map<String, String> e: detail) {
            result.append(e.get("name"));
            result.append("(");
            result.append(e.get("versionCode"));
            result.append(")\n");
        }
        return result.toString();
    }
}
