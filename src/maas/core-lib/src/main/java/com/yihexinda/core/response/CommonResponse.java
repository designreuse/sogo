package com.yihexinda.core.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/10 0010
 */
@ApiModel(value = "CommonResponse", description = "平峰返回信息描述")
@Data
public class CommonResponse {


    @ApiModelProperty("车辆路线列表")
    private List<RouteListObj> vehicleRouteList;
    @ApiModelProperty("用户派车列表")
    private List<UserVehicle> userVehicleList;
    private Map<String,List<String>> userStationMap;

    public CommonResponse(){}

    public CommonResponse(List<RouteListObj> vehicleRouteList, List<UserVehicle> userVehicleList,
                          Map<String, List<String>> userStationMap) {
        this.vehicleRouteList = vehicleRouteList;
        this.userVehicleList = userVehicleList;
        this.userStationMap = userStationMap;
    }

    public Map<String, List<String>> getUserStationMap() {
        return userStationMap;
    }

    public void setUserStationMap(Map<String, List<String>> userStationMap) {
        this.userStationMap = userStationMap;
    }

    public List<RouteListObj> getVehicleRouteList() {
        return vehicleRouteList;
    }

    public void setVehicleRouteList(List<RouteListObj> vehicleRouteList) {
        this.vehicleRouteList = vehicleRouteList;
    }

    public List<UserVehicle> getUserVehicleList() {
        return userVehicleList;
    }

    public void setUserVehicleList(List<UserVehicle> userVehicleList) {
        this.userVehicleList = userVehicleList;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "vehicleRouteList=" + vehicleRouteList +
                ", userVehicleList=" + userVehicleList +
                ", userStationMap=" + userStationMap +
                '}';
    }
}