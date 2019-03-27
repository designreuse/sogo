package com.yihexinda.userweb.client;

import com.yihexinda.core.vo.ResultVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/3 0003
 */
@FeignClient(value = "data-service")
@RequestMapping("/sysCache/client")
public interface SysCacheClient {

    /**
     * 系统配置
     * @return
     */
    @PostMapping(value = "/sysParamList")
    ResultVo sysParamList();
}
