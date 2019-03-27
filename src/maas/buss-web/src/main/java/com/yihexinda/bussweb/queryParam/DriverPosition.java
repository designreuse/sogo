package com.yihexinda.bussweb.queryParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/4 0004
 */
@Data
@ApiModel("司机实时位置")
public class DriverPosition {

    @ApiModelProperty(value = "车辆id")
    private String carId;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

}
