package com.yihexinda.data.dto;

import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * @author Jack
 * @date 2018/11/3.
 */
@Data
@ToString
public class UserToBeAssignedQueryDto {

    private String deptCity;
    /**
     * 需要权限
     */
    private String authority;

    /**
     * not in deptCities
     */
    private List<String> deptCityNot;
}
