package com.yihexinda.platformweb.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yihexinda.core.dto.Json;
import com.yihexinda.data.dto.RemarkDto;

/**
 *
 */
@FeignClient("data-service")
@RequestMapping("/remark")
public interface DataRemarkClient {

    @RequestMapping(value = "/addRemark", method = RequestMethod.POST)
    @ResponseBody
    Json addRemark(RemarkDto requestDto);

    @RequestMapping(value = "/findRemark", method = RequestMethod.POST)
    @ResponseBody
    List<RemarkDto> findRemark(RemarkDto requestDto);
}
