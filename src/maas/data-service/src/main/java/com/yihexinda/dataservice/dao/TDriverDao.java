package com.yihexinda.dataservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TDriverDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 司机信息表 Mapper 接口
 * </p>
 *
 * @author pengfeng
 * @since 2018-11-28
 */
public interface TDriverDao extends BaseMapper<TDriverDto> {

    /**
     * 查询司机列表
     * @param map  查询参数
     * @return 司机信息列表
     */
    List<TDriverDto> getDriverList(Map map);


    //更改司机车辆绑定信息
    int updateDriverCatStatus(Map<String,Object> data);

}
