package com.mobile.test.testmonitor.util;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/8/20.
 */

public class DateFormat {
    public String cTMToyyMMddHHmmss(long currentTimeMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return formatter.format(currentTimeMillis);
    }

    public String cTMToyyyyMMddHHmmssHasSign(long currentTimeMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return formatter.format(currentTimeMillis);
    }

    public Calendar stringToCalendar(String time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = formatter.parse(time);
            calendar.setTime(date);
            return calendar;
        } catch (Exception e) {

        }
        return calendar;
    }

    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        return calendar;
    }
}
