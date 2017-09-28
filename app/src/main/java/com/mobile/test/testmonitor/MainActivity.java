package com.mobile.test.testmonitor;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mobile.test.testmonitor.collector.ScreenshotRecord;
import com.mobile.test.testmonitor.collector.SystemInfoListener;
import com.mobile.test.testmonitor.collector.SystemInfoLogger;
import com.mobile.test.testmonitor.collector.SystemInfoService;
import com.mobile.test.testmonitor.templates.file.FragmentFile;
import com.mobile.test.testmonitor.templates.home.FragmentHome;
import com.mobile.test.testmonitor.templates.home.config.ConfigSaver;
import com.mobile.test.testmonitor.util.DateFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ServiceConnection conn;
    private SystemInfoService mService;
    private AlertDialog.Builder alertDialog;
    private String serverStartTime;
    private static ArrayList record;
    static ScreenshotRecord screenshotRecord;

    private FragmentFile fragmentFile;
    private FragmentHome fragmentHome;
    private FragmentManager manager;

    private Handler mHandler;
    private ConfigSaver mConfig;

    //导航切换
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentTransaction transaction1 = manager.beginTransaction();
                    transaction1.replace(R.id.content, fragmentHome);
                    transaction1.commit();
                    return true;
                case R.id.navigation_file:
                    FragmentTransaction transaction2 = manager.beginTransaction();
                    transaction2.replace(R.id.content, fragmentFile);
                    transaction2.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化两个导航页面
        manager = getSupportFragmentManager();
        fragmentFile = new FragmentFile();
        fragmentHome = new FragmentHome();
        //主页显示
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, fragmentHome);
        transaction.commit();
        //导航切换监听
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //初始化弹窗
        alertDialog = new AlertDialog.Builder(this);

        // Configurations
        mConfig = new ConfigSaver(getApplicationContext());
        //截屏监听
        record = new ArrayList();
        screenshotRecord = new ScreenshotRecord(record);

        // 统一请求权限
        verifyPermissions(this);
    }

    @Override
    protected void onDestroy() {
        if (conn != null) {
            unbindService(conn);
        }
        super.onDestroy();
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    /*
    * 保存日志
    * */
    private void dumpLogs(String filename) {
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TestMonitor";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(path + "/" + filename);
            file.createNewFile();
            SystemInfoLogger.i("002@" + JSON.toJSONString(record));
            String filter = "logcat -s " + SystemInfoLogger.INFO_TAG + ":I";
            String export = filter + " -f " + file.getAbsolutePath();
            Runtime.getRuntime().exec(export);
            Toast.makeText(this, "已保存：TestMonitor/" + filename, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Debug
        String[] files = fileList();
        for (String file : files) {
            SystemInfoLogger.d(file);
        }
    }

    /*
    * 点击开始/停止按钮
    * */
    public void changeTimer() {
        if (conn == null) {
            //判断是否监听蓝牙，是的话弹窗提醒用户
            if (mConfig.getConfig().isCollectorBt()) {
                Toast.makeText(this, "您已开启蓝牙监听，只有在蓝牙扫描后才可监听到信号变化。", Toast.LENGTH_SHORT).show();
            }
            //初始化按钮
            Button home_btn_start_or_stop = (Button) fragmentHome.getView().findViewById(R.id.btn_start_or_stop);
            Button home_btn_record = (Button) fragmentHome.getView().findViewById(R.id.btn_record);
            home_btn_start_or_stop.setText("停止");
            home_btn_record.setBackgroundColor(getResources().getColor(R.color.colorButtonEnabled));
            home_btn_record.setEnabled(true);
            TextView config = (TextView) fragmentHome.getView().findViewById(R.id.configure);
            config.setClickable(false);
            config.setTextColor(Color.GRAY);

            fragmentHome.startServer = true;

            //截屏监听
            if (mConfig.getConfig().isScreenshot()) {
                screenshotRecord.startWatching();
            }
            //清除旧日志
            record.clear();
            try {
                String clear = "logcat -s " + SystemInfoLogger.INFO_TAG + ":I -c";
                Runtime.getRuntime().exec(clear);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //开启server
            serverStartTime = new DateFormat().cTMToyyMMddHHmmss(System.currentTimeMillis());
            Intent intent = new Intent(this, SystemInfoService.class);
            conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mService = ((SystemInfoService.SystemInfoBinder) service).getService();
                    mService.setContext(getApplicationContext());
                    long interval = Long.parseLong(mConfig.getConfig().getInterval().substring(0,
                            mConfig.getConfig().getInterval().indexOf("s"))) * 1000;
                    mService.startTimer(new SystemInfoListener() {
                        @Override
                        public void onChange(Map info) {
                            Message msg = Message.obtain();
                            msg.obj = info;
                            mHandler.sendMessage(msg);
                        }
                    }, interval);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                }
            };

            bindService(intent, conn, Context.BIND_AUTO_CREATE);
        } else {
            //停止服务
            unbindService(conn);
            conn = null;
            //修改页面相关样式
            fragmentHome.startServer = false;
            Button home_btn_start_or_stop = (Button) fragmentHome.getView().findViewById(R.id.btn_start_or_stop);
            Button home_btn_record = (Button) fragmentHome.getView().findViewById(R.id.btn_record);
            home_btn_start_or_stop.setText("开始");
            home_btn_record.setBackgroundColor(getResources().getColor(R.color.colorButtonDisabled));
            home_btn_record.setEnabled(false);
            TextView config = (TextView) fragmentHome.getView().findViewById(R.id.configure);
            config.setClickable(true);
            config.setTextColor(Color.WHITE);

            //停止截屏监听
            if (mConfig.getConfig().isScreenshot()) {
                screenshotRecord.stopWatching();
            }

            //弹窗输入文件名
            final EditText edit = new EditText(this);
            edit.setText(serverStartTime + "_");
            edit.setSelection(serverStartTime.length() + 1);
            alertDialog.setTitle("请输入文件名");
            alertDialog.setView(edit);
            alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //记录日志
                    String text = edit.getText().toString();
                    if (text.length() == serverStartTime.length() + 1) {
                        text = text.substring(0, serverStartTime.length());
                    }
                    dumpLogs(text + ".log");
                }
            });
            alertDialog.show();
        }
    }

    /*
    * 点击记录按钮
    * */
    public void recordInfo() {
        SystemInfoLogger.i("003@记录时间点");
        record.add(new DateFormat().cTMToyyyyMMddHHmmssHasSign(System.currentTimeMillis()));
        Toast.makeText(this, "记录成功", Toast.LENGTH_SHORT).show();
    }


    /*
    * 存储权限
    * */
    private final static int REQUEST_SENSITIVE_PERMISSIONS = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyPermissions(Activity activity) {
        // Check if we have all permissions we need.
        int permissionExternalStorage = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionReadPhoneStateCheck = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_PHONE_STATE);
        int permissionAccessCoarseLocationCheck = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionExternalStorage != PackageManager.PERMISSION_GRANTED ||
                permissionReadPhoneStateCheck != PackageManager.PERMISSION_GRANTED ||
                permissionAccessCoarseLocationCheck != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS,
                    REQUEST_SENSITIVE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SENSITIVE_PERMISSIONS) {
            boolean getAllPermisson = true;
            if (grantResults.length < 3) {
                getAllPermisson = false;
            }
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    getAllPermisson = false;
                }
            }

            if (!getAllPermisson) {
                verifyPermissions(MainActivity.this);
            }
        }
    }
}
