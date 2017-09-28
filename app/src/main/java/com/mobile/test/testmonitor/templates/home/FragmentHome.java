package com.mobile.test.testmonitor.templates.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.test.testmonitor.MainActivity;
import com.mobile.test.testmonitor.R;
import com.mobile.test.testmonitor.collector.information.InfoApp;
import com.mobile.test.testmonitor.collector.information.InfoBt;
import com.mobile.test.testmonitor.collector.information.InfoElectricity;
import com.mobile.test.testmonitor.collector.information.InfoSetting;
import com.mobile.test.testmonitor.collector.information.InfoSim;
import com.mobile.test.testmonitor.collector.information.InfoWifi;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/16.
 */

public class FragmentHome extends Fragment {
    private MainActivity mActivity;

    private Button mButtonStartOrStop;
    private Button mButtonRecord;

    //信息展示——SIM
    private TextView mInfoSimPhone1;
    private TextView mInfoSimPhone2;
    private TextView mInfoSimNet;
    private TextView mInfoSimMobile;
    private TextView mInfoSimSignal;
    //信息展示——设置
    private TextView mInfoSettingFont;
    private TextView mInfoSettingLanguage;
    private TextView mInfoSettingBrightness;
    private TextView mInfoSettingStorage;
    private TextView mInfoSettingMemory;
    //信息展示——WIFI
    private TextView mInfoWifiState;
    private TextView mInfoWifiType;
    private TextView mInfoWifiSignal;
    //信息展示——BT
    private TextView mInfoBtState;
    private TextView mInfoBtType;
    private TextView mInfoBtSignal;
    //信息展示——APP
    private TextView mInfoAppNumber;
    private LinearLayout mInfoApp;
    private String mInfoAppDetail;
    //信息展示——电量
    private TextView mInfoElectricityModel;
    private TextView mInfoElectricityElectricity;

    //页面信息
    public boolean startServer = false;
    InfoSim myInfoSim;
    InfoSetting myInfoSetting;
    InfoWifi myInfoWifi;
    InfoBt myInfoBt;
    InfoApp myInfoApp;
    InfoElectricity myInfoElectricity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        mActivity = ((MainActivity) getActivity());
        mActivity.setHandler(mHandler);

        //打开配置页面
        TextView config = (TextView) view.findViewById(R.id.configure);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.mobile.test.testmonitor.ACTION_START");
                intent.addCategory("com.mobile.test.testmonitor.MY_CATEGORY");
                startActivity(intent);
            }
        });

        //信息展示——sim
        mInfoSimPhone1 = (TextView) view.findViewById(R.id.info_sim_phone1);
        mInfoSimPhone2 = (TextView) view.findViewById(R.id.info_sim_phone2);
        mInfoSimNet = (TextView) view.findViewById(R.id.info_sim_net);
        mInfoSimMobile = (TextView) view.findViewById(R.id.info_sim_mobile);
        mInfoSimSignal = (TextView) view.findViewById(R.id.info_sim_signal);
        //信息展示——设置
        mInfoSettingFont = (TextView) view.findViewById(R.id.info_setting_font);
        mInfoSettingLanguage = (TextView) view.findViewById(R.id.info_setting_language);
        mInfoSettingBrightness = (TextView) view.findViewById(R.id.info_setting_brightness);
        mInfoSettingStorage = (TextView) view.findViewById(R.id.info_setting_storage);
        mInfoSettingMemory = (TextView) view.findViewById(R.id.info_setting_memory);
        //信息展示——wifi
        mInfoWifiState = (TextView) view.findViewById(R.id.info_wifi_state);
        mInfoWifiType = (TextView) view.findViewById(R.id.info_wifi_type);
        mInfoWifiSignal = (TextView) view.findViewById(R.id.info_wifi_signal);
        //信息展示——bt
        mInfoBtState = (TextView) view.findViewById(R.id.info_bt_state);
        mInfoBtType = (TextView) view.findViewById(R.id.info_bt_type);
        mInfoBtSignal = (TextView) view.findViewById(R.id.info_bt_signal);
        //信息展示——app
        mInfoAppNumber = (TextView) view.findViewById(R.id.info_app_number);
        mInfoApp = (LinearLayout) view.findViewById(R.id.info_app);
        mInfoAppDetail = "无";
        mInfoApp.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView edit = new TextView(mActivity);
                edit.setText(mInfoAppDetail);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
                alertDialog.setTitle("第三方APP及其版本号");
                alertDialog.setView(edit);
                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                });
                alertDialog.show();
            }
        });
        //信息展示——电量
        mInfoElectricityModel = (TextView) view.findViewById(R.id.info_electricity_model);
        mInfoElectricityElectricity = (TextView) view.findViewById(R.id.info_electricity_electricity);

        //控制和记录按钮
        mButtonStartOrStop = (Button) view.findViewById(R.id.btn_start_or_stop);
        mButtonRecord = (Button) view.findViewById(R.id.btn_record);
        mButtonStartOrStop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.changeTimer();
            }
        });
        mButtonRecord.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.recordInfo();
            }
        });

        super.onViewCreated(view, savedInstanceState);

        //之前已有页面信息
        if (startServer) {
            resetInformation(myInfoSim, myInfoSetting, myInfoWifi, myInfoBt, myInfoApp, myInfoElectricity);
            mInfoAppDetail = myInfoApp.detailString();
            Button home_btn_start_or_stop = (Button)view.findViewById(R.id.btn_start_or_stop);
            Button home_btn_record = (Button) view.findViewById(R.id.btn_record);
            home_btn_start_or_stop.setText("停止");
            home_btn_record.setBackgroundColor(getResources().getColor(R.color.colorButtonEnabled));
            home_btn_record.setEnabled(true);
            TextView mconfig = (TextView) view.findViewById(R.id.configure);
            mconfig.setClickable(false);
            mconfig.setTextColor(Color.GRAY);
        }
    }

    //采集信息获取和展示
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Map<String, Object> info = (Map) msg.obj;
            myInfoSim = (InfoSim) info.get("SIM");
            myInfoSetting = (InfoSetting) info.get("Settings");
            myInfoWifi = (InfoWifi) info.get("WIFI");
            myInfoBt = (InfoBt) info.get("BT");
            myInfoApp = (InfoApp) info.get("APP");
            myInfoElectricity = (InfoElectricity) info.get("Electricity");
            resetInformation(myInfoSim, myInfoSetting, myInfoWifi, myInfoBt, myInfoApp, myInfoElectricity);
            mInfoAppDetail = myInfoApp.detailString();
        }
    };

    //刷新信息展示
    public void resetInformation(InfoSim infoSim, InfoSetting infoSetting, InfoWifi infoWifi,
                                 InfoBt infoBt, InfoApp infoApp, InfoElectricity infoElectricity) {
        //信息展示——sim
        mInfoSimPhone1.setText(infoSim.getPhone1());
        mInfoSimPhone2.setText(infoSim.getPhone2());
        mInfoSimNet.setText(infoSim.getNet());
        mInfoSimMobile.setText(infoSim.getMobile());
        mInfoSimSignal.setText(infoSim.getSignal());
        //信息展示——设置
        mInfoSettingFont.setText(infoSetting.getFont());
        mInfoSettingLanguage.setText(infoSetting.getLanguage());
        mInfoSettingBrightness.setText(infoSetting.getBrightness());
        mInfoSettingStorage.setText(infoSetting.getStorage());
        mInfoSettingMemory.setText(infoSetting.getMemory());
        //信息展示——wifi
        mInfoWifiState.setText(infoWifi.getState());
        mInfoWifiType.setText(infoWifi.getType());
        mInfoWifiSignal.setText(infoWifi.getSignal());
        //信息展示——bt
        mInfoBtState.setText(infoBt.getState());
        mInfoBtType.setText(infoBt.getType());
        mInfoBtSignal.setText(infoBt.getSignal());
        //信息展示——app
        mInfoAppNumber.setText(infoApp.getNumber());

        //信息展示——电量
        mInfoElectricityModel.setText(infoElectricity.getModel());
        mInfoElectricityElectricity.setText(infoElectricity.getElectricity());
    }
}
