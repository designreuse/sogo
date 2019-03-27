package com.yihexinda.dataservice.dao;

import com.yihexinda.data.dto.TRoutePeakDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工时段统计
 * </p>
 *
 * @author pengfeng
 * @since 2018-12-4
 */
public interface RouteCountDao {

    /**
     * 历史工时段统计
     * @param  map 查询参数
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
