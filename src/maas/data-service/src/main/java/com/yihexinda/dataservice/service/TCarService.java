package com.yihexinda.dataservice.service;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TCarDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yihexinda.data.dto.TCarPositionDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车辆信息表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TCarService extends IService<TCarDto> {

    /**
     * 查询车辆是否到站
     * @param carPosition 车辆实时位置
     * @return boolean
     */
    boolean carGetDown(TCarPositionDto carPosition);


    /**
     * 查询车辆总座位数
     * @return ResultVo
     */
    Integer getCarCount();

    /**
     * 司机确认到站
     * @param condition
     * @return
     */
    ResultVo confirmArrive(Map<String, Object> condition);
}
