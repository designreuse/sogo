package com.yihexinda.bussweb.client;

import com.yihexinda.core.vo.ResultVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/1 0001
 */
@FeignClient(value = "data-service")
@RequestMapping("/routeOffpeak/client")
public interface RouteOffpeakClient {

    /**
     * 查询我的平峰行程列表
     * @return
     */
    @PostMapping(value = "/buss/routeOffpeakList")
    ResultVo bussRouteOffpeakList(@RequestBody Map<String,Object> condition);
}
