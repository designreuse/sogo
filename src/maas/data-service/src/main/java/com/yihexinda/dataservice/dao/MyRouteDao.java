package com.yihexinda.dataservice.dao;

import com.yihexinda.data.dto.MyRouteDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chenzeqi
 * @date 2018/12/24
 */
@Mapper
public interface MyRouteDao {

    /**
     * 查询我的行程 -- 平峰
     * @param driverId 司机id
     * @param startTime 当天时间 （只查当天的记录）
     * @return
     */
    List<MyRouteDto> getMyRouteOffpeakList(@Param("driverId")String driverId,@Param("startTime")Date startTime);

    /**
     * 查询我的行程 -- 高峰
     * @param driverId 司机id
     * @param startTime 当天时间 （只查当天的记录）
     * @return
     */
    List<MyRouteDto> getMyRoutePeakList(@Param("driverId")String driverId,@Param("startTime")Date startTime);

    /**
     * 查询我的行程详情 -- 平峰
     * @param routeId 路线id
     * @param
     * @return
     */
    MyRouteDto getMyRouteOffpeakDetails(String routeId);

    /**
     * 查询我的行程详情 -- 高峰
     * @param routeId 路线id
     * @param
     * @return
     */
    MyRouteDto getMyRoutePeakDetails(String routeId);

    //路线详情
    List<MyRouteDto> getRouteDetails(@Param("routeId")String routeId,@Param("type")String type);


    /**
     * 是否有进行中的行程
     * @param driverId 司机id
     * @return  是否有进行中的行程
     */
    Map getRouteInfo(String driverId);
}
