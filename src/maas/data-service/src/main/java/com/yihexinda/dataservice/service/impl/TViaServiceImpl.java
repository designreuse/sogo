package com.yihexinda.dataservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.data.dto.TViaDto;
import com.yihexinda.dataservice.dao.TViaDao;
import com.yihexinda.dataservice.service.TViaService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 途经站点表 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class TViaServiceImpl extends ServiceImpl<TViaDao, TViaDto> implements TViaService {


    /**
     * 根据线路id 查询出 途径站点
     *
     * @param lineId 线路id
     * @return 途径站点列表
     */
    @Override
    public List<TViaDto> getViaList(String lineId) {
        return super.baseMapper.getViaList(lineId);

    }
}
