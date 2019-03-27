package com.yihexinda.data.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ApiModel("我的行程接收实体")
@ToString
public class MyRouteDto {

    /**
     * 行程id
     */
    @ApiModelProperty("行程id")
    private String Id;

    /**
     * 司机ID
     */
    @ApiModelProperty("司机ID")
    private String driverId;

    /**
     * 车辆id
     */
    @ApiModelProperty("车辆id")
    private String carId;

    /**
     *车牌号牌照
     */
    @ApiModelProperty("车牌号牌照")
    private String license;

    /**
     * 行程状态(0:未完成  1：已完成)
     */
    @ApiModelProperty("行程状态(0:未完成  1：已完成)")
    private String routeState;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createDate;

    /**
     * 途径站点
     */
    @ApiModelProperty("途径站点")
    private String siteName;

    /**
     * 载客人数
     */
    @ApiModelProperty("载客人数")
    private Integer peopleNum;

    /**
     * 总里程
     */
    @ApiModelProperty("总里程")
    private Double totalMile;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 总时长
     */
    @ApiModelProperty("总时长")
    private Integer totalTime;

    /**
     * 上车人数
     */
    @ApiModelProperty("上车人数")
    public Integer onNum;

    /**
     * 下车人数
     */
    @ApiModelProperty("下车人数")
    public Integer offNum;

}




