package com.yihexinda.dataservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.data.dto.TTravelEvaluateDto;
import com.yihexinda.dataservice.dao.TTravelEvaluateDao;
import com.yihexinda.dataservice.service.TTravelEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 行程评价表 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class TTravelEvaluateServiceImpl extends ServiceImpl<TTravelEvaluateDao, TTravelEvaluateDto> implements TTravelEvaluateService {

    @Autowired
    private TTravelEvaluateDao travelEvaluateDao;


    /**
     *后台行程评价数据展示t
     * @return
     */
    @Override
    public List<Map<String, Object>> getTravelEvaluateManager() {
        return travelEvaluateDao.getTravelEvaluateManager();
    }

    /**
     * 查询行程评价列表
     * @param map  查询参数
     * @return 评价列表
     */
    @Override
    public List<TTravelEvaluateDto> travelEvaluateList(Map map) {
        return this.baseMapper.travelEvaluateList(map);
    }
}
