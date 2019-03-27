package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TSysParamDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/11/29
 */
@FeignClient(value = "data-service")
@RequestMapping("/sysParam/client")
public interface SysParamClient {

    /**
     * 查询用户信息列表
     * @param  tSysParamDto 配置信息
     * @return 用户信息列表
     */
    @RequestMapping(method = RequestMethod.POST, value = "/updateAppointmentNumber")
    ResultVo updateAppointmentNumber(@RequestBody TSysParamDto tSysParamDto);

    /**
     * 查询系统预约人数配置
     * @param key 系统配置key
     * @return ResultVo
     */
    @GetMapping(value = "/getAppointmentNumber/{key}")
    ResultVo getAppointmentNumber(@PathVariable("key") String key);

}
