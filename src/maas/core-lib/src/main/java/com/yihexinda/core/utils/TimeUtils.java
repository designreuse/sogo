package com.yihexinda.core.utils;

/**
 * Created by T470p on 2018/11/3.
 */
public class TimeUtils {

    /**
     * 时间戳转换天,时,分,秒
     * @param clockDate
     * @param clockTime
     * @return
     */
    public static Long timeUtils(long clockDate,long clockTime)

    {
        long dateTime = clockDate- clockTime;
        long day = dateTime / (24 * 60 * 60 * 1000);
        long hour = (dateTime / (60 * 60 * 1000) - day * 24);
        long min = ((dateTime / (60 * 1000)) - day * 24 * 60 - hour * 60);
        if(hour > 0){
            min = min+(hour * 60);
        }
        long s = (dateTime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if( min > 0){
            s = s +(min * 60);
        }
        return s;

    }
}
