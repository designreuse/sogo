package com.yihexinda.dataservice.service;

import com.yihexinda.data.dto.TScheduleWorkDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 排班信息表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TScheduleWorkService extends IService<TScheduleWorkDto> {

    /**
     * 新增排班
     * @param tScheduleWorkDto 排班信息
     * @return Boolean
     */
    Boolean addWorkHour(TScheduleWorkDto tScheduleWorkDto);

    /**
     * 修改排班
     * @param tScheduleWorkDto 排班信息
     * @return Boolean
     */
    Boolean updateWorkHour(TScheduleWorkDto tScheduleWorkDto);


    List<TScheduleWorkDto> getScheduleWorkList(Map map);


}
