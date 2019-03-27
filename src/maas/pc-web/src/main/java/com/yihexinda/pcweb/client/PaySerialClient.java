package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TPaySerialDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
    * @author zl
    * @date 2018/10/12.
    */
@FeignClient(value = "data-service")
@RequestMapping("/paySerial/client")
public interface PaySerialClient {
    /**
     * 支付统计列表
     * @param data 参数
     * @return ResultVo
     */
      @RequestMapping(value =  "/getPaySerialList",method = RequestMethod.POST)
       ResultVo getPaySerialList(@RequestBody Map<String, Object> data);

    /**
     * 支付统计导出(不分页)
     * @return ResultVo
     */
    @RequestMapping(value =  "/paySerialExcel",method = RequestMethod.GET)
    List<TPaySerialDto> paySerialExcel();
}
