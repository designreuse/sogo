package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TSysUserDto;
import com.yihexinda.data.dto.TTravelEvaluateDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/11/29
 */
@FeignClient(value = "data-service")
@RequestMapping("/TravelEvaluateApiResource/client")
public interface TravelEvaluateClient {

    /**
     * 查询用户评价列表
     * @param  data 参数map
     * @return 用户评价列表
     */
    @RequestMapping(method = RequestMethod.POST, value = "/travelEvaluateList")
    ResultVo travelEvaluateList(@RequestBody Map<String, Object> data);


    /**
     * 查询用户信息列表
     * @return 用户信息列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/travelEvaluateExcel")
    List<TTravelEvaluateDto> travelEvaluateExcel();

}
