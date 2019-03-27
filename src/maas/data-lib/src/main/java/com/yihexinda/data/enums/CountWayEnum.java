package com.yihexinda.data.enums;

public enum CountWayEnum{

    订单类别("o.ORDER_SORT"),
    出票员("u.USERNAME"),
    订单类型("o.ORDER_TYPE"),
    订单来源("o.ORDER_SOURCE"),
    来源方式("o.FROM_ACTION");
//    订单状态("LAST_OPERATE_STATUS");

    private String cade;

    CountWayEnum(String value){
        this.cade = value;
    }

    public String getCode() {
        return cade;
    }
}
