package com.yihexinda.dataservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TRoutePeakDto;

import java.util.List;
import java.util.Map;
/**
 * <p>
 * 高峰行程信息表 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TRoutePeakDao extends BaseMapper<TRoutePeakDto> {

    /**
     * 根据行程id查询高峰行程
     */

    public TRoutePeakDto getByRouteId(String routeId);
    /**
     * 查询高峰线路信息
     * @param condition
     * @return
     */
    List<Map<String,Object>> queryRouteLineList(Map<String, Object> condition);


    /**
     *  查询高峰行程列表
     * @param map  查询参数
     * @return 高峰行程列表
     */
    List<TRoutePeakDto> routePeakList(Map map);


    /**
     *  z注：目前只知道高峰有线路信息
     *  根据车辆id 查询线路
     * @param carId  车辆id
     * @return 线路信息
     */
    TRoutePeakDto getByCarId(String carId);




}
