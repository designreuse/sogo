package com.yihexinda.dataservice.service;

import com.yihexinda.data.dto.TRoutePeakDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工时段统计
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface RouteCountService{


    /**
     * 历史工时段统计
     * @param map 查询参数
     * @return 统计列表
     */
    List<Map> getListCount(Map map);

    /**
     * 当天工时段统计
     * @param map 查询参数
     * @return 统计列表
     */
    List<Map> getRouteStation(Map map);


}
