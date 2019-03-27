package com.yihexinda.dataservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TCarPositionDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车辆实时位置记录表 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-12-05
 */
public interface TCarPositionDao extends BaseMapper<TCarPositionDto> {

    /**
     * 查询车辆现在所属位置
     * @return
     */
    List<Map<String,Object>> selectCurCarPostion(Map<String,Object> condition);
}
