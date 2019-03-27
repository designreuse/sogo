package com.yihexinda.dataservice.service;

import com.yihexinda.data.dto.TRouteOffpeakDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;

/**
 * <p>
 * 平峰行程信息表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TRouteOffpeakService extends IService<TRouteOffpeakDto> {

    /**
     * 批量修改平峰行程信息
     * @param updateRouteOffpeakList
     * @return
     */
    int batchUpdateRouteOffpeak(ArrayList<TRouteOffpeakDto> updateRouteOffpeakList);
}
