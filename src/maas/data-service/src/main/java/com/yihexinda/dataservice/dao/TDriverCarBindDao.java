package com.yihexinda.dataservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TDriverCarBindDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 司机车辆绑定记录表 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Mapper
public interface TDriverCarBindDao extends BaseMapper<TDriverCarBindDto> {

    List<TDriverCarBindDto> getList();

    TDriverCarBindDto getDriverId(String driverId);

    /**
     * 根据司机id 查询历史绑定列表
     * @param map 查询参数
     * @return 历史绑定列表
     */
    List<TDriverCarBindDto> getDriverCarBind(Map map);

    /**
     * 获取司机登陆的时候所有绑定与未绑定车辆
     * @return
     */
    List<TDriverCarBindDto> getCarAndBindStatusList();

    /**
     * 司机端查询当前绑定的车辆
     * @param driverId
     * @return
     */
   TDriverCarBindDto getBindCar(String driverId);


    /**
     * 每天 23：59:59 将司机注销
     */
    boolean updateStatus();

}
