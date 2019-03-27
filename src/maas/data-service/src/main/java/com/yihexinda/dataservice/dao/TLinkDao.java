package com.yihexinda.dataservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TLinkDto;

/**
 * <p>
 * 路网由一条条的link组成，link有起点和终点坐标，
link表，中心提供 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TLinkDao extends BaseMapper<TLinkDto> {

}
