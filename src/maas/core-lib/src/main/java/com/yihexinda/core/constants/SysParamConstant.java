package com.yihexinda.core.constants;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Administrator
 * @date 2018/12/15 0015 9:10
 */
public class SysParamConstant {

    /**
     * 高峰票价
     */
    public static final  String  SYS_PEAK_PRICE = "sys.peak.price";

    /**
     * 平峰票价
     */
    public static final String  SYS_OFFPEAK_PRICE = "sys.offpeak.price";

    //缓存内部人员价格
    public static final String SYS_COMPANY_PRICE = "sys.company.price";
    //缓存外部人员价格
    public static final String SYS_OTHER_PRICE  = "sys.other.price";

    /**
     * 高峰时间段
     */
    public static final String  SYS_PEAK_TIME_RANGE = "sys.peak.timerange";

    /**
     * 高峰时间段
     */
    public static final String  APPOINTMENT_NUMBER = "appointmentNumber";

    //
    @Value("${pjValue}")
    public static String  pjValue ;

    public static final String COMMON_URL = "http://183.62.69.35:18011/maas/common";
    public static final String PEAK_URL = "http://183.62.69.35:18011/maas/peakAll";
//    public static final String PEAK_URL = "http://183.62.69.35:18011/maas/peak";

}
