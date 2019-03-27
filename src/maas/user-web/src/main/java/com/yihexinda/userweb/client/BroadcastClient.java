package com.yihexinda.userweb.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TBroadcastDto;

/**
 * @author zhanglei
 * @date 2018/10/12.
 */
@FeignClient(value = "data-service")
@RequestMapping("/broadcast/client")
public interface BroadcastClient {
	
	 /**
      * 显示广播
	  * @return
	  */
    @RequestMapping(value = "/getBroadcastList",method = RequestMethod.GET)
    ResultVo getBroadcastList();
}
