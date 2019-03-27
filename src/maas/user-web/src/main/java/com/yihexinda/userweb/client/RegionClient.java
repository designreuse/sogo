package com.yihexinda.userweb.client;

import com.yihexinda.core.vo.ResultVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/11 0011
 */
@FeignClient(value = "data-service")
@RequestMapping("/region/client")
public interface RegionClient {

    /**
     * 获取省市区
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/regionList")
    ResultVo regionList();
}
