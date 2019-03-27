package com.yihexinda.userweb.client;

import com.yihexinda.core.vo.ResultVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author zl
 *
 */
@FeignClient(value = "data-service")
@RequestMapping("/sysParam/client")
public interface SysParamClient {
    /**
     * 高峰、平峰价格
     * @return
     */
    @RequestMapping(value = "/getTicketPrice",method = RequestMethod.GET)
     ResultVo getTicketPrice();
      /**
       * 内部/外部人员价格
       *
       * @return
       */
      @RequestMapping(value = "/getTicketPriceByPerson",method = RequestMethod.POST)
      ResultVo getTicketPriceByPerson(@RequestBody Map<String,Object> data);

}
