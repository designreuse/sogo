package com.yihexinda.dataservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yihexinda.data.dto.TTravelEvaluateDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 行程评价表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TTravelEvaluateService extends IService<TTravelEvaluateDto> {

    /**
     * 显示行程评价管理
     */
    List<Map<String ,Object>> getTravelEvaluateManager();

    /**
     * 查询行程评价列表
     * @param map 查询参数
     * @return 评价列表
     */
    List<TTravelEvaluateDto> travelEvaluateList(Map map);
}
