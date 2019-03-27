package com.yihexinda.core.utils;
/**
 * Copyright &copy; 2017-2020  All rights reserved.
 * <p>
 * Licensed under the 深圳市领居科技 License, Version 1.0 (the "License");
 */

import java.util.Random;
import java.util.UUID;


/**
 * *
 * 用算法生成全球唯一标识.
 *
 *
 */
public class GUID {


    public static void main(String args[]) {
        System.out.println(GUID.getPreUUID());
    }

    /**
     * *
     * 根据java UUID生成全球唯一字符串（32位）.
     *
     * @return the string
     */
    public static String generateByUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    /**
     *
     *
     * 方法说明：
     *根据java UUID生成全球唯一字符串（40位）.
     * 前8位前缀区分体系
     * @return
     *
     * @author 段志鹏 CreateDate: 2017年6月21日
     *
     */
    public static String getPreUUID() {
        return generateByUUID();
    }

    /** ** 计数器，启动产生一个随机数. */
    private static Object SYNC_LOCK = new Object();

    /** The count machine. */
    private static long COUNT_MACHINE = Math.abs(new Random().nextLong());


    /** * 产生多大的seq，（IP移位+序列号）. */
    private static final int SEQ_DIGITAL_MULTIPLY = 1000000;

}
