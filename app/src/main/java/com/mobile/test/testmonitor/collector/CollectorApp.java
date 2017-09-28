package com.mobile.test.testmonitor.collector;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.mobile.test.testmonitor.collector.information.InfoApp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/20.
 */

public class CollectorApp extends CollectorBase{
    private InfoApp infoApp;
    private PackageManager packageManager;

    public CollectorApp(Context context){
        super(context);
        mCollectorTag = "App";
        packageManager = mContext.getPackageManager();
        this.infoApp = new InfoApp();
    }

    public InfoApp updateInformation(){
        this.infoApp = getAllInfo();
        return this.infoApp;
    }

    public InfoApp defaultInformation(){
        return this.infoApp;
    }

    protected InfoApp getAllInfo() {
        InfoApp infoApp = new InfoApp();

        //getPackageManager()函数在Activity可以直接调用，
        //在Fragment里需要调用getActivity()获得Activity的上下文，
        //在一个单独的类中调用需要上下文被作为参数传过来。
        List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        try {
            for (PackageInfo info : packages) {
                Map<String, String> temp = new HashMap<>();
                if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    temp.put("versionCode", String.valueOf(info.versionCode));
                    temp.put("name", info.applicationInfo.loadLabel(packageManager).toString());
                    infoApp.addDetail(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        infoApp.setNumber(String.valueOf(infoApp.getDetail().size()));
        return infoApp;
    }
}
