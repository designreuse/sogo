package com.yihexinda.userweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TUserFeedDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author pengfeng
 * @date 2018/11/29
 */
@FeignClient(value = "data-service")
@RequestMapping("/userFeed/client")
public interface UserFeedClient {

    /**
     * pengFeng
     *  新增用户反馈
     * @param tUserFeedDto 反馈信息
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addUserFeed")
    ResultVo addUserFeed(@RequestBody TUserFeedDto tUserFeedDto);

}
