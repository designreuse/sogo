package com.yihexinda.core.utils;

import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.util.Date;
import java.util.Random;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/18 0018
 */
public class CreateUtils {

    public static String createOrderCode(String prefix){
        return prefix+DateUtils.formatDate(new Date(), DateUtils.PATTERN_yyyyMMddHHmmss)+ new Random().nextInt(9999);
    }

    public static void main(String[] args) {
        System.out.println(CreateUtils.createOrderCode("OC_"));
    }
}
