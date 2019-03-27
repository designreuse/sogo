package com.yihexinda.dataservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yihexinda.data.dto.TLineDriverBindDto;
import com.yihexinda.data.dto.TRoutePeakDto;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 高峰行程信息表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TRoutePeakService extends IService<TRoutePeakDto> {

    /**
     * 添加高峰行程信息
     * @param tRoutePeakDto 高峰行程信息
     * @return boolean
     */
    boolean addRoutePeak(TRoutePeakDto tRoutePeakDto);

    /**
     * 修改高峰行程信息
     * @param tRoutePeakDto 高峰行程信息
     * @return boolean
     */
    boolean updateRoutePeak(TRoutePeakDto tRoutePeakDto);

    /**
     *  根据id 查询高峰详情
     * @param id id
     * @return 高峰详情
     */
    TRoutePeakDto getRoutePeakId(String id);

    /**
     *   高峰行程分配司机
     * @param lineId 线路id
     * @param lineDriverBindDtoList  lineDriverBindDtoList 司机id
     * @return 高峰详情
     */
    boolean distribution(String lineId, List<TLineDriverBindDto> lineDriverBindDtoList);


    /**
     *  pc 查询高峰列表
     * @param map  查询参数
     * @return 高峰列表
     */
    List<TRoutePeakDto> routePeakList(Map map);


    /**
     * 根据行程id查询高峰行程
     */

    public TRoutePeakDto getByRouteId(String routeId);

    //查询高峰线路信息
    List<Map<String,Object>> queryRouteLineList(Map<String, Object> condition);


    /**
     *  z注：目前只知道高峰有线路信息
     *  根据车辆id 查询线路
     * @param carId  车辆id
     * @return 线路信息
     */
    TRoutePeakDto getByCarId(String carId);

}
