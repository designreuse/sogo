package com.yihexinda.core.constants;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/3 0003
 */
public class RedisConstant {

    //缓存系统变量
    public static final String SYS_PARAM_CACHE_KEY = "SYS_PARAM_CACHE_KEY";

    //缓存系统城市信息
    public static final String SYS_RESIONS_CACHE_KEY = "SYS_RESIONS_CACHE_KEY";

    public static final String SYS_STATIONS_CACHE_KEY = "SYS_STATIONS_CACHE_KEY";

    //保存高峰途经站点
    public static final String SYS_PEAK_VIA_STATIONS_CACHE_KEY = "SYS_PEAK_VIA_STATIONS_CACHE_KEY";

    //缓存系统预约时间
    public static final String SYS_APPOINTMENT_TIME_KEY = "SYS_APPOINTMENT_TIME_KEY";

    public static final String SYS_CREATE_ORDER_KEY_EXPIRE_PREFIX = "SYS_CREATE_ORDER_KEY_EXPIRE_PREFIX";

    //缓存高峰价格
    public static final String SYS_PEAK_PRICE = "SYS_PEAK_PRICE";

    //缓存平峰价格
    public static final String SYS_OFFPEAK_PRICE  = "SYS_OFFPEAK_PRICE";
    //缓存内部人员价格
    public static final String SYS_COMPANY_PRICE = "SYS_COMPANY_PRICE";
    //缓存外部人员价格
    public static final String SYS_OTHER_PRICE  = "SYS_OTHER_PRICE";

    //高峰时间段
    public static final String SYS_PEAK_TIME_RANGE = "SYS_PEAK_TIME_RANGE";

    //保存用户高峰订单线路最近的车辆信息
    public static final String MAAS_PEAK_USER_ORDER_LINE_VALUE = "MAAS_PEAK_USER_ORDER_LINE_VALUE";

    //保存司机与车辆的绑定
    public static final String SYS_TODAY_CAR_DRIVER_BIND_VALUE = "SYS_TODAY_CAR_DRIVER_BIND_VALUE";

    //key过期时间
    public static final long SYS_CACHE_KEY_TIME_ONE_DAY = 60*60*24;
    public static final long SYS_CACHE_KEY_TIME_ONE_WEEK = 60*60*24*7;
    public static final long SYS_CACHE_KEY_TIME_ONE_MONTH = 60*60*24*30;
    public static final long SYS_CACHE_KEY_TIME_ONE_YEAR = 60*60*24*365;
}
