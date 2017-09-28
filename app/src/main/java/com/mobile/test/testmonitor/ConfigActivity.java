package com.mobile.test.testmonitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.test.testmonitor.templates.home.config.ConfigInfo;
import com.mobile.test.testmonitor.templates.home.config.ConfigSaver;


public class ConfigActivity extends AppCompatActivity {

    private ConfigSaver mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
        setContentView(R.layout.activity_home_config);

        setBackButton();
        setSaveButton();
        timeSelect();

        mConfig = new ConfigSaver(getApplicationContext());
        initConfig(mConfig.getConfig());
    }


    //返回按钮
    private void setBackButton() {
        TextView back = (TextView) findViewById(R.id.back_config);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
            }
        });
    }

    //时间间隔选择
    private void timeSelect() {
        // 初始化控件
        Spinner spinner = (Spinner) findViewById(R.id.time_selector);
        // 建立数据源
        String[] mItems = getResources().getStringArray(R.array.time_intervals);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner.setAdapter(adapter);
    }

    //保存配置信息
    private void setSaveButton() {
        Button save = (Button) findViewById(R.id.button_save);
        save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigInfo configInfo = new ConfigInfo();
                //时间间隔
                Spinner spinner = (Spinner) findViewById(R.id.time_selector);
                configInfo.setInterval(spinner.getSelectedItem().toString());
                //信息采集
                Switch collectorSim = (Switch) findViewById(R.id.collector_sim);
                configInfo.setCollectorSim(collectorSim.isChecked());
                Switch collectorWifi = (Switch) findViewById(R.id.collector_wifi);
                configInfo.setCollectorWifi(collectorWifi.isChecked());
                Switch collectorBt = (Switch) findViewById(R.id.collector_bt);
                configInfo.setCollectorBt(collectorBt.isChecked());
                Switch collectorSetting = (Switch) findViewById(R.id.collector_setting);
                configInfo.setCollectorSetting(collectorSetting.isChecked());
                Switch collectorApp = (Switch) findViewById(R.id.collector_app);
                configInfo.setCollectorApp(collectorApp.isChecked());
                Switch collectorElectricity = (Switch) findViewById(R.id.collector_electricity);
                configInfo.setCollectorElectricity(collectorElectricity.isChecked());
                //截屏时间记录
                Switch screenshot = (Switch) findViewById(R.id.screenshot_record);
                configInfo.setScreenshot(screenshot.isChecked());
                //保存
                mConfig.saveConfig(configInfo);
                //提示框
                Toast.makeText(ConfigActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化配置信息
    private void initConfig(ConfigInfo configInfo) {
        //时间间隔
        Spinner spinner = (Spinner) findViewById(R.id.time_selector);
        SpinnerAdapter apsAdapter = spinner.getAdapter();
        int k = apsAdapter.getCount();
        String value = configInfo.getInterval();
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                spinner.setSelection(i);
                break;
            }
        }
        //信息采集
        Switch collectorSim = (Switch) findViewById(R.id.collector_sim);
        collectorSim.setChecked(configInfo.isCollectorSim());
        Switch collectorWifi = (Switch) findViewById(R.id.collector_wifi);
        collectorWifi.setChecked(configInfo.isCollectorWifi());
        Switch collectorBt = (Switch) findViewById(R.id.collector_bt);
        collectorBt.setChecked(configInfo.isCollectorBt());
        Switch collectorSetting = (Switch) findViewById(R.id.collector_setting);
        collectorSetting.setChecked(configInfo.isCollectorSetting());
        Switch collectorApp = (Switch) findViewById(R.id.collector_app);
        collectorApp.setChecked(configInfo.isCollectorApp());
        Switch collectorElectricity = (Switch) findViewById(R.id.collector_electricity);
        collectorElectricity.setChecked(configInfo.isCollectorElectricity());
        //截屏时间记录
        Switch screenshot = (Switch) findViewById(R.id.screenshot_record);
        screenshot.setChecked(configInfo.isScreenshot());
    }
}
