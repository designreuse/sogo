package com.yihexinda.nodeweb.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yihexinda.core.dto.Json;
import com.yihexinda.data.dto.OperateLogCreateDto;
import com.yihexinda.data.dto.OperateLogDto;
import com.yihexinda.data.dto.OperateLogQueryDto;

/**
 * @author Jack
 * @date 2018/10/24.
 */
@FeignClient(value = "data-service")
@RequestMapping("/operateLog")
public interface DataOperateLogClient {
    @RequestMapping(method = RequestMethod.POST, value = "/findByAble")
    @ResponseBody
    List<OperateLogDto> findByAble(OperateLogQueryDto queryDto);

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    @ResponseBody
    Json add(OperateLogCreateDto operateLogDto);

    @PostMapping("/findByAbleAndUserId")
    List<OperateLogDto> findByAbleAndUserId(OperateLogQueryDto queryDto);
}
