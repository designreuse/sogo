package com.yihexinda.dataservice.service.impl;

import com.yihexinda.data.dto.TLineDto;
import com.yihexinda.dataservice.dao.TLineDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.dataservice.service.TLineService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 高峰的线路信息表 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class TLineServiceImpl extends ServiceImpl<TLineDao, TLineDto> implements TLineService {


    /**
     * 查出线路途径站点经纬度
     *
     * @param lineId 线路id、
     * @return 经纬度列表
     */
    @Override
    public List<TLineDto> lineVia(String lineId) {
        return this.baseMapper.lineVia(lineId);
    }
}
