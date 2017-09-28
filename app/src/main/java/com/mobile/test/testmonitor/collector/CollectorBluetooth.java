package com.mobile.test.testmonitor.collector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mobile.test.testmonitor.collector.information.InfoBt;
import com.mobile.test.testmonitor.templates.home.config.ConfigSaver;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Administrator on 2017/8/19.
 */

public class CollectorBluetooth extends CollectorBase {
    private BluetoothAdapter mBluetoothAdapter;
    private InfoBt infoBt;

    private static final String DEVICE_NOT_FOUND = "无设备";
    private static final String DEVICE_ON = "开启";
    private static final String DEVICE_OFF = "关闭";
    private static final String DEVICE_DISCONNECTED = "未连接";
    private static final String DEVICE_CONNECTING = "连接中";
    private static final String DEVICE_CONNECTED = "已连接";
    private HashMap<String, Integer> mCachedDevice = new HashMap<>();

    CollectorBluetooth(Context context) {
        super(context);
        mCollectorTag = "Bluetooth";
        infoBt = new InfoBt();
    }
    private String enable2string(int state) {
        switch (state) {
            // Simplify states
            case BluetoothAdapter.STATE_ON: case BluetoothAdapter.STATE_TURNING_ON:
                return DEVICE_ON;
            case BluetoothAdapter.STATE_OFF: case BluetoothAdapter.STATE_TURNING_OFF:
                return DEVICE_OFF;
        }
        return null;
    }

    public InfoBt updateInformation(){
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter != null) {
                int state = mBluetoothAdapter.getState();
                infoBt.setState(enable2string(state));

                // Listening ACTION_FOUND to store the RSSI
                ConfigSaver configSaver = new ConfigSaver(mContext);
                if(configSaver.getConfig().isCollectorBt()){
                    IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                    filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                    filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                    filter.addAction(BluetoothDevice.ACTION_FOUND);
                    mContext.registerReceiver(mReceiver, filter);
                }
            }else{
                infoBt.setState(DEVICE_NOT_FOUND);
            }
        }
        return this.infoBt;
    }

    public InfoBt defaultInformation(){
        return this.infoBt;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED: {
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                    String enable = enable2string(state);
                    infoBt.setState(enable);

                    // Clear the scan data in case of running out of memory.
                    if (Objects.equals(enable, DEVICE_OFF)) {
                        mCachedDevice.clear();
                    }
                }   break;
                case BluetoothDevice.ACTION_FOUND: {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String address = device.getAddress();

                    // Use Short.MIN_VALUE(-32768) as default value
                    int rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    mCachedDevice.put(address, rssi);
                }   break;
                case BluetoothDevice.ACTION_ACL_CONNECTED: {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String address = device.getAddress();
                    infoBt.setType(DEVICE_CONNECTED);

                    // Use RSSI in last scan
                    infoBt.setSignal(String.valueOf(mCachedDevice.get(address)));
                }   break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    infoBt.setType(DEVICE_DISCONNECTED);
                    infoBt.setSignal(String.valueOf(Short.MIN_VALUE));
                    break;
            }
        }
    };
}
