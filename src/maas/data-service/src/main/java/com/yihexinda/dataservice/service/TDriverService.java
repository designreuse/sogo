package com.yihexinda.dataservice.service;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TDriverDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yihexinda.dataservice.dao.TDriverDao;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 司机信息表 服务类
 * </p>
 *
 * @author pengfeng
 * @since 2018-11-28
 */
public interface TDriverService extends IService<TDriverDto> {

    /**
     * 司机批量排班
     * @param driverDtoList 司机listId
     * @param scheduleId 排班id
     * @return boolean
     */
    boolean batchScheduling(List<TDriverDto> driverDtoList,String scheduleId);

    /**
     * 新增司机信息
     * @param tDriverDto 司机实体
     * @return boolean
     */
    boolean addDriverInfo(TDriverDto tDriverDto);


    /**
     * 修改司机信息
     * @param tDriverDto 司机实体
     * @return boolean
     */
    boolean updateDriverInfo(TDriverDto tDriverDto);

    /**
     * 司机信息列表
     * @param data 查询参数
     * @return  司机信息列表
     */
    List<TDriverDto> getDriverList(Map data);

    /**
     *  获取司机详细信息
     * @param id 司机id
     * @return 司机详情
     */
    TDriverDto getDriverInfo(String id);

    /**
     * 退出登录
     * @param driverId 司机id
     * @return boolean
     */
    boolean loginOutDriver(String driverId);

    //司机退出登录
    ResultVo driverLogout(Map<String,Object> data);



}
