package com.yihexinda.dataservice.service;


import com.yihexinda.core.vo.ResultVo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author chenzeqi
 * @date 2018/12/24
 */
public interface MyRouteService {

    //查询我的行程列表
    ResultVo getMyRouteList(@RequestBody Map<String,Object> condition);

    //查询我的行程详情
    ResultVo getMyRouteDetails(@RequestBody Map<String,Object> condition);


    /**
     * 查询是否有行程
     * @param driverId 司机id
     * @return ResultVo
     */
    Map getRouteInfo(String driverId);

}
