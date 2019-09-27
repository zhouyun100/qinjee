package com.qinjee.masterdata.utils;


import java.util.Date;


/**
 * @author Administrator
 */
public class GetMonthUtil {
    public static int getMonth(Date early, Date late) {
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        //设置时间为0时
        calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calst.set(java.util.Calendar.MINUTE, 0);
        calst.set(java.util.Calendar.SECOND, 0);
        caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
        caled.set(java.util.Calendar.MINUTE, 0);
        caled.set(java.util.Calendar.SECOND, 0);
        //得到两个日期相差的月数
        int month = Math.round((int) (caled.getTime().getTime() / 1000) - (int) (calst
                .getTime().getTime() / 1000) / 3600 / 24 / 30);

        return month;
    }
}
