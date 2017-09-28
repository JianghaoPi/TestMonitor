package com.mobile.test.testmonitor.collector;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import com.mobile.test.testmonitor.collector.information.InfoSim;

import java.util.List;

/**
 * Created by Administrator on 2017/8/20.
 */

public class CollectorSim extends CollectorBase {
    private InfoSim infoSim;

    //没有网络连接
    private static final String NETWORK_NONE = "无网络连接";
    //wifi连接
    private static final String NETWORK_WIFI = "Wi-Fi连接";
    //手机网络数据连接类型
    private static final String NETWORK_2G = "2G网络连接";
    private static final String NETWORK_3G = "3G网络连接";
    private static final String NETWORK_4G = "4G网络连接";
    private static final String NETWORK_MOBILE = "未知";

    private SubscriptionManager subscriptionManager = null;
    private MyPhoneStateListener myPhoneStateListener;

    public CollectorSim(Context context) {
        super(context);
        mCollectorTag = "Sim";
        this.infoSim = new InfoSim();
        if (subscriptionManager == null) {
            subscriptionManager = SubscriptionManager.from(context);
        }
        myPhoneStateListener = new MyPhoneStateListener();
        TelephonyManager telephonyManager = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(myPhoneStateListener,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public InfoSim updateInformation() {
        infoSim.setPhone1("无");
        infoSim.setPhone2("无");
        infoSim.setNet("无");
        infoSim.setMobile("无");

        int size;
        List<SubscriptionInfo> pack = getActiveSubscriptionInfo();
        if (pack != null && (size = pack.size()) != 0) {
            infoSim.setPhone1(pack.get(0).getNumber() + "(" + pack.get(0).getCarrierName() + ")");
            if (size == 2) {
                infoSim.setPhone2(pack.get(1).getNumber() + "(" + pack.get(1).getCarrierName() + ")");
            }
            infoSim.setNet(getNetworkState());
            infoSim.setMobile(isMobileConnected() ? "ON" : "OFF");
        }
        return this.infoSim;
    }

    public InfoSim defaultInformation(){
        return this.infoSim;
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        /*
         *Get the Signal strength from the provider, each tiome there is an update
         *从得到的信号强度,每个tiome供应商有更新
         * */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (signalStrength.getGsmSignalStrength() != 99) {
                infoSim.setSignal(String.valueOf(signalStrength.getGsmSignalStrength() * 2 - 113) + "dbM");
            }
        }
    }

    private List<SubscriptionInfo> getActiveSubscriptionInfo() {
        return subscriptionManager.getActiveSubscriptionInfoList();
    }

    private boolean isMobileConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取当前网络连接类型
     *
     * @param
     * @return
     */
    public String getNetworkState() {
        //获取系统的网络服务
        ConnectivityManager connManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //如果当前没有网络
        if (null == connManager)
            return NETWORK_NONE;
        //获取当前网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORK_NONE;
        }
        // 判断是不是连接的是不是wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORK_WIFI;
                }
        }
        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        //如果是2g类型
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NETWORK_2G;
                        //如果是3g类型
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NETWORK_3G;
                        //如果是4g类型
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NETWORK_4G;
                        default:
                            //中国移动 联通 电信 三种3G制式
                            strSubTypeName = strSubTypeName.toUpperCase();
                            if (strSubTypeName.equals("TD-SCDMA") ||
                                    strSubTypeName.equals("WCDMA") ||
                                    strSubTypeName.equals("CDMA2000")) {
                                return NETWORK_3G;
                            } else {
                                return NETWORK_MOBILE;
                            }
                    }
                }
        }
        return NETWORK_NONE;
    }

}
