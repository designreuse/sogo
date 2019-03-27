package com.yihexinda.data.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by wxf on 2018/11/23.
 */
@ApiModel("首页用户在线人数统计")
@Data
public class UserOnlineDto {
    private Integer online ;
    private String address;

}
