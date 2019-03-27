package com.yihexinda.dataservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TLineDto;

import java.util.List;

/**
 * <p>
 * 高峰的线路信息表 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TLineDao extends BaseMapper<TLineDto> {

    /**
     * 查出线路途径站点经纬度
     * @param lineId 线路id
     * @return 经纬度列表
     */
    List<TLineDto> lineVia(String lineId);

}
