package com.yihexinda.dataservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TStationDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 站点信息表 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TStationDao extends BaseMapper<TStationDto> {

    List<Map<String,Object>> loadPeakLineStations(Map<String, Object> data);

}
