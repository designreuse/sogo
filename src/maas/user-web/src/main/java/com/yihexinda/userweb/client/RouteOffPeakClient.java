package com.yihexinda.userweb.client;

import com.yihexinda.core.vo.ResultVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2019/3/15 0015
 */
@FeignClient(value = "data-service")
@RequestMapping("/routeOffpeak/client")
public interface RouteOffPeakClient {


    @RequestMapping(method = RequestMethod.POST, value = "/setRouteOrderExpire")
    ResultVo setRouteOrderExpire(@RequestBody Map<String, Object> condition);
}
