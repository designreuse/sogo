package com.yihexinda.dataservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TRouteOffpeakDto;

import java.util.ArrayList;

/**
 * <p>
 * 平峰行程信息表 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-12-04
 */
public interface TRouteOffpeakDao extends BaseMapper<TRouteOffpeakDto> {

    /**
     * 批量修改平峰行程信息
     * @param updateRouteOffpeakList
     * @return
     */
    int batchUpdateRouteOffpeak(ArrayList<TRouteOffpeakDto> updateRouteOffpeakList);
}
