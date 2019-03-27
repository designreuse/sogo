package com.yihexinda.dataservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TNodeDto;

/**
 * <p>
 * Node表，和link表里的fromNode和toNode来自此表
中心提供 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TNodeDao extends BaseMapper<TNodeDto> {

}
