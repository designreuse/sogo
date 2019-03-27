package com.yihexinda.dataservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.data.dto.TRouteOffpeakDto;
import com.yihexinda.dataservice.dao.TRouteOffpeakDao;
import com.yihexinda.dataservice.service.TRouteOffpeakService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * <p>
 * 平峰行程信息表 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class TRouteOffpeakServiceImpl extends ServiceImpl<TRouteOffpeakDao, TRouteOffpeakDto> implements TRouteOffpeakService {

    /**
     * 批量修改平峰行程信息
     * @param updateRouteOffpeakList
     * @return
     */
    @Override
    public int batchUpdateRouteOffpeak(ArrayList<TRouteOffpeakDto> updateRouteOffpeakList) {
        return this.baseMapper.batchUpdateRouteOffpeak(updateRouteOffpeakList);
    }
}
