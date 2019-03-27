package com.yihexinda.bussweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TSmsLogDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "data-service")
@RequestMapping("/smsLog/client")
public interface SmsLogClient {
    /**
     *  发送短信
     * @param smsLogDto
     * @return
     */
    @RequestMapping(value = "/addSmsLog",method = RequestMethod.POST)
    ResultVo addSmsLog(@RequestBody TSmsLogDto smsLogDto);
}
