package com.yihexinda.dataservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TStationDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 站点信息表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TStationService extends IService<TStationDto> {

    /**
     * 根据站点ids查询站点信息（专为算法提供）
     * @param ids
     * @return
     */
    List<Map<String, Object>> queryStationByIds(List<String> ids);

    //根据stationId 查询属于该路线的所有站点信息
    ResultVo loadPeakLineStations(Map<String, Object> data);

}
