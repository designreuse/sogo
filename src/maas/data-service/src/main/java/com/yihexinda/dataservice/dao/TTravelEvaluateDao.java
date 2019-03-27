package com.yihexinda.dataservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TTravelEvaluateDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 行程评价表 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TTravelEvaluateDao extends BaseMapper<TTravelEvaluateDto> {

    /**
     * 显示行程评价管理
     */
    List<Map<String ,Object>> getTravelEvaluateManager();


    /**
     * 查询行程评价列表
     * @param map  查询参数
     * @return 评价列表
     */
    List<TTravelEvaluateDto> travelEvaluateList(Map map);
}
