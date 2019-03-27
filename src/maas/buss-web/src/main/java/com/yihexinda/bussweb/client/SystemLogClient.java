package com.yihexinda.bussweb.client;

import com.yihexinda.core.vo.ResultVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2019/3/13 0013
 */
@FeignClient(value = "data-service")
@RequestMapping("/syslog/client")
public interface SystemLogClient {


    /**
     *保存日志
     * @param condition
     * @return
     */
    @RequestMapping(value = "/save" ,method = RequestMethod.POST)
    ResultVo save(@RequestBody Map<String,Object> condition);


}
