package com.mobile.test.testmonitor.collector;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.text.format.Formatter;

import com.mobile.test.testmonitor.collector.information.InfoSetting;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Administrator on 2017/8/20.
 */

public class CollectorSetting extends CollectorBase {
    private InfoSetting infoSetting;

    public CollectorSetting(Context context) {
        super(context);
        mCollectorTag = "Setting";
        infoSetting = new InfoSetting();
    }

    /**
     * 更新信息并返回
     */
    public InfoSetting updateInformation() {
        this.infoSetting.setFont(getDefaultFont());
        this.infoSetting.setLanguage(getLanguage());
        this.infoSetting.setBrightness(getBrightnessMode());
        this.infoSetting.setStorage(getDataDirectorySpace());
        this.infoSetting.setMemory(getRamMemory());
        return this.infoSetting;
    }

    public InfoSetting defaultInformation(){
        return this.infoSetting;
    }

    /**
     * 获得系统默认字体
     */
    public String getDefaultFont() {
        String result = String.valueOf(mContext.getResources().getConfiguration().fontScale);
        //Typeface typeface = Typeface.DEFAULT;
        //String font = typeface.toString();
        return result;
    }

    /**
     * 获得系统语言
     */
    public String getLanguage() {
        String defaultLanguage = mContext.getResources().getConfiguration().locale.getLanguage();
        return defaultLanguage;
    }

    /**
     * 获得系统亮度调节模式
     */
    public String getBrightnessMode() {
        try {
            if (Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE)
                    == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                return "自动调节";
            } else if (Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE)
                    == Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL) {
                return "手动调节";
            }
            ;
        } catch (Exception e) {

        }
        return "未知";
    }

    /**
     * 获得内部存储空间信息
     */
    public String getDataDirectorySpace() {
        String path = Environment.getDataDirectory().getAbsolutePath();//内部存储空间路径
        StatFs statfs = new StatFs(path);
        long free = statfs.getFreeBytes();//空闲空间字节数
        long total = statfs.getTotalBytes();//总空间字节数
        String result = Formatter.formatFileSize(mContext, free) + "/" +
                Formatter.formatFileSize(mContext, total);
        return result;
    }

    /**
     * 获得内存使用情况
     */
    public String getRamMemory() {
        final ActivityManager activityManager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        String result = Formatter.formatFileSize(mContext, (info.totalMem - info.availMem)) + "/" +
                Formatter.formatFileSize(mContext, info.totalMem);
        return result;
    }

}
