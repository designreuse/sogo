package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @version 1.0
 * @date 2018/11/30 0030
 */
@FeignClient(value = "data-service")
@RequestMapping("/station/client")
public interface StationClient {

    /**
     * 查询高峰站点列表
     * @return ResultVo
     */
    @GetMapping(value = "/stationList")
    ResultVo stationList();

}
