package com.yihexinda.dataservice.service;

import com.yihexinda.data.dto.TLineDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 高峰的线路信息表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TLineService extends IService<TLineDto> {

    /**
     * 查出线路途径站点经纬度
     * @param lineId 线路id
     * @return 经纬度列表
     */
    List<TLineDto> lineVia(String lineId);

}
