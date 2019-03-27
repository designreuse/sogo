package com.yihexinda.dataservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yihexinda.data.dto.TCarPositionDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车辆实时位置记录表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-12-05
 */
public interface TCarPositionService extends IService<TCarPositionDto> {

    //查询车辆现在所属位置
    List<Map<String,Object>> selectCurCarPostion();

    //查询车辆现在所属位置
    List<Map<String,Object>> selectCurCarPostion(Map<String,Object> condition);

    //根据车辆id查询车辆所属位置
    Map<String,Object> selectCurCarPostionById(Map<String,Object> condition);

    //计算车辆方向角
    Map<String,Object> calculateVehicleDirectionAngle() throws Exception;


    Map<String,Object> calculateVehicleDirectionAngle(Map<String,Object> condition) throws Exception;
}
