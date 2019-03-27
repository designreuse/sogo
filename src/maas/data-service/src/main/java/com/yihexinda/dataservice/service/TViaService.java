package com.yihexinda.dataservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yihexinda.data.dto.TViaDto;

import java.util.List;

/**
 * <p>
 * 途经站点表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TViaService extends IService<TViaDto> {

    /**
     * 根据线路id 查询出 途径站点
     * @param lineId 线路id
     * @return 途径站点列表
     */
    List<TViaDto> getViaList(String lineId);
}
