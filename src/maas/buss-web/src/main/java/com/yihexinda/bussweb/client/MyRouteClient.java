package com.yihexinda.bussweb.client;

import com.yihexinda.core.vo.ResultVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author chenzeqi
 * @date 2018/12/24
 */
@FeignClient(value = "DATA-SERVICE")
@RequestMapping("/myRoute/client")
public interface MyRouteClient {

    /**
     * 查询我的平峰行程列表
     * @return
     */
    @PostMapping(value = "/getMyRouteList")
    ResultVo getMyRouteList(@RequestBody Map<String,Object> condition);

    /**
     * 查询我的行程详情
     * @return
     */
    @PostMapping(value = "/getMyRouteDetails")
    ResultVo getMyRouteDetails(@RequestBody Map<String,Object> condition);


    /**
     * 查询司机是否有进行中行程
     * @param driverId 司机id
     * @return ResultVo
     */
//    @PostMapping(value = "/getRouteInfo/{driverId}")
//    ResultVo getRouteInfo(@PathVariable("driverId") String driverId);
    @PostMapping(value = "/getRouteInfo")
    ResultVo getRouteInfo(@RequestBody Map<String,Object> data);


    /**
     * 结束行程
     * @type 1请求平峰列表 2 请求高峰列表
     * @routeId 行程ID
     * @param
     * @return
     */
    @PostMapping(value = "/finishRoute")
    ResultVo finishRoute(@RequestBody Map<String, Object> data);

    /**
     * 检查行程是否结束
     * @type type 0 高峰 1平峰
     * @routeId 行程ID
     * @param
     * @return
     */
    @PostMapping(value = "/checkFinish")
    ResultVo checkFinish(Map<String, Object> data);
}
