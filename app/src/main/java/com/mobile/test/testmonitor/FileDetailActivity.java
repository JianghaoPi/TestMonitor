package com.mobile.test.testmonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.mobile.test.testmonitor.collector.information.InfoApp;
import com.mobile.test.testmonitor.collector.information.InfoBt;
import com.mobile.test.testmonitor.collector.information.InfoElectricity;
import com.mobile.test.testmonitor.collector.information.InfoSetting;
import com.mobile.test.testmonitor.collector.information.InfoSim;
import com.mobile.test.testmonitor.collector.information.InfoWifi;
import com.mobile.test.testmonitor.util.DateFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 */

public class FileDetailActivity extends AppCompatActivity {
    private TimePickerView pvTime;
    //时间选择
    private TextView tvTime;
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
    //记录时间列表
    private ArrayList<Calendar> recordTimeList;
    //文件绝对路径
    private String path;
    private AlertDialog.Builder alertDialog;
    private ArrayAdapter<String> adapter;
    private Spinner timeSpinner;
    //返回、删除
    private TextView back;
    private TextView deleteFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        //获取文件绝对路径
        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        alertDialog = new AlertDialog.Builder(this);

        //退出按钮
        back = (TextView) findViewById(R.id.file_detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //删除按钮
        deleteFile = (TextView) findViewById(R.id.file_detail_delete);
        deleteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.setTitle("确认删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    File file = new File(path);
                                    if (file.isFile() && file.exists()) {
                                        file.delete();
                                    }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                                finally {
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作
                            }
                        })
                        .show();
            }
        });
        //时间选择控件
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvTime.setText(new DateFormat().cTMToyyyyMMddHHmmssHasSign(date.getTime()));
            }
        })
                .setCancelText("取消")
                .setSubmitText("确定")
                .build();
        //时间选择UI
        tvTime = (TextView) findViewById(R.id.file_detail_time);
        tvTime.setText(new DateFormat().cTMToyyyyMMddHHmmssHasSign(
                Calendar.getInstance().getTimeInMillis()));
        tvTime.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.setDate(new DateFormat().stringToCalendar(tvTime.getText().toString()));
                pvTime.show();
            }
        });
        tvTime.addTextChangedListener(new

                                              TextWatcher() {
                                                  @Override
                                                  public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                      getRecordInfo(tvTime.getText().toString());
                                                  }

                                                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                  }

                                                  public void afterTextChanged(Editable s) {
                                                  }


                                              });
        //选择记录的时间节点
        timeSpinner = (Spinner) findViewById(R.id.file_detail_time_selector);
        String _recordTime = getRecordTime();
        _recordTime = _recordTime.split("002\\@")[1];
        List mItems = JSON.parseArray(_recordTime);
        adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);
        timeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvTime.setText(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        //信息展示——sim
        mInfoSimPhone1 = (TextView) findViewById(R.id.info_sim_phone1);
        mInfoSimPhone2 = (TextView) findViewById(R.id.info_sim_phone2);
        mInfoSimNet = (TextView) findViewById(R.id.info_sim_net);
        mInfoSimMobile = (TextView) findViewById(R.id.info_sim_mobile);
        mInfoSimSignal = (TextView) findViewById(R.id.info_sim_signal);
        //信息展示——设置
        mInfoSettingFont = (TextView) findViewById(R.id.info_setting_font);
        mInfoSettingLanguage = (TextView) findViewById(R.id.info_setting_language);
        mInfoSettingBrightness = (TextView) findViewById(R.id.info_setting_brightness);
        mInfoSettingStorage = (TextView) findViewById(R.id.info_setting_storage);
        mInfoSettingMemory = (TextView) findViewById(R.id.info_setting_memory);
        //信息展示——wifi
        mInfoWifiState = (TextView) findViewById(R.id.info_wifi_state);
        mInfoWifiType = (TextView) findViewById(R.id.info_wifi_type);
        mInfoWifiSignal = (TextView) findViewById(R.id.info_wifi_signal);
        //信息展示——bt
        mInfoBtState = (TextView) findViewById(R.id.info_bt_state);
        mInfoBtType = (TextView) findViewById(R.id.info_bt_type);
        mInfoBtSignal = (TextView) findViewById(R.id.info_bt_signal);
        //信息展示——app
        mInfoAppNumber = (TextView) findViewById(R.id.info_app_number);
        mInfoApp = (LinearLayout) findViewById(R.id.info_app);
        mInfoAppDetail = "无";
        mInfoApp.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView edit = new TextView(getBaseContext());
                edit.setText(mInfoAppDetail);
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
        mInfoElectricityModel = (TextView) findViewById(R.id.info_electricity_model);
        mInfoElectricityElectricity = (TextView) findViewById(R.id.info_electricity_electricity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    //获取记录时间点
    public String getRecordTime() {
        String result = "002@[]";
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String str = null;
            while ((str = br.readLine()) != null) {
                if(str.contains("002@")){
                    result = str;
                    break;
                }
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //根据时间获取信息
    public void getRecordInfo(String time) {
        time = time.substring(5);
        String result = null;
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String str1 = "", str2;
            while ((str2 = br.readLine()) != null) {
                if (str2.startsWith("-")) continue;
                if (time.compareTo(str2.substring(0, 14)) < 0) {
                    if (str1.contains("001@")) {
                        result = str1;
                    }
                    break;
                }
                if (str2.contains("001@")) {
                    str1 = str2;
                }
            }
            if (result != null) {
                JSONObject jsonObject = (JSONObject) JSONObject.parse(result.split("001@")[1]);
                InfoElectricity ie = JSONObject.parseObject(jsonObject.get("Electricity").toString(), InfoElectricity.class);
                InfoApp ia = JSONObject.parseObject(jsonObject.get("APP").toString(), InfoApp.class);
                InfoBt ib = JSONObject.parseObject(jsonObject.get("BT").toString(), InfoBt.class);
                InfoSetting ise = JSONObject.parseObject(jsonObject.get("Settings").toString(), InfoSetting.class);
                InfoSim isi = JSONObject.parseObject(jsonObject.get("SIM").toString(), InfoSim.class);
                InfoWifi iw = JSONObject.parseObject(jsonObject.get("WIFI").toString(), InfoWifi.class);
                resetInformation(isi, ise, iw, ib, ia, ie);
            } else {
                resetInformation(new InfoSim(), new InfoSetting(), new InfoWifi(), new InfoBt(),
                        new InfoApp(), new InfoElectricity());
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        mInfoAppDetail = infoApp.detailString();
        //信息展示——电量
        mInfoElectricityModel.setText(infoElectricity.getModel());
        mInfoElectricityElectricity.setText(infoElectricity.getElectricity());
    }

}
