package com.yihexinda.dataservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TViaDto;

import java.util.List;

/**
 * <p>
 * 途经站点表（ 子表） Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-12-04
 */
public interface TViaDao extends BaseMapper<TViaDto> {

    /**
     * 根据线路id 查询出 途径站点
     * @param lineId 线路id
     * @return 途径站点列表
     */
    List<TViaDto> getViaList(String lineId);
}
