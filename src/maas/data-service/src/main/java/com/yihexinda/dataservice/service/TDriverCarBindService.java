package com.yihexinda.dataservice.service;

import com.yihexinda.data.dto.TDriverCarBindDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 司机车辆绑定记录表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TDriverCarBindService extends IService<TDriverCarBindDto> {

    List<TDriverCarBindDto> getList();

    /**
     * 根据司机查询绑定信息
     * @param driverId 司机id
     * @return  查询是否绑定
     */
    TDriverCarBindDto getDriverId(String driverId);

    /**
     * 根据司机id 查询历史绑定列表
     * @param map 查询参数
     * @return 绑定列表信息
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
     * @return boolean
     */
    boolean updateStatus();

}
