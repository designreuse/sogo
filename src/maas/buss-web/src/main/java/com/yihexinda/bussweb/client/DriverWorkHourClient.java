package com.yihexinda.bussweb.client;

import com.yihexinda.core.vo.ResultVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author yhs
 * @date 2018/11/28.
 */

@FeignClient(value = "data-service")
@RequestMapping("/driverWorkHour/client")
public interface DriverWorkHourClient {


    /**
     * 根据司机Id查询司机工时
     * @param paramter
     * @return
     */
    @PostMapping(value = "/getDriverWorkHour")
    ResultVo getDriverWorkHour(@RequestBody Map<String, String> paramter) ;
}
