package com.yihexinda.auth.enums;

import com.yihexinda.core.abst.BaseCodeEnum;

/**
 * @author Jack
 * @date 2018/10/24.
 */
public enum SysMenuType implements BaseCodeEnum {
    菜单(0),
    按钮(1);

    SysMenuType(Integer code) {
        this.code = code;
    }

    private int code;

    @Override
    public int getCode() {
        return code;
    }
}
