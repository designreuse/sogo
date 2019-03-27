package com.yihexinda.bussweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TUserFeedDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author yhs
 * @date 2018/12/1.
 */
@FeignClient(value = "data-service")
@RequestMapping("/userFeed/client")
public interface UserFeedClient {

    /**
     * yhs
     * 提交司机用户反馈
     *@param condition
     * @return
     */
    @RequestMapping(value = "/addDriverFeed" ,method = RequestMethod.POST)
    ResultVo addDriverFeed(@RequestBody Map<String,Object> condition);


    /**
     * yhs
     * 删除用户反馈
     * @param userFeedId
     * @return
     */

    @RequestMapping(value = "/deleteUserFeed/{userFeedId}",   method=RequestMethod.DELETE)
    ResultVo deleteUserFeed(@PathVariable(value = "userFeedId") int userFeedId);


    /**
     * yhs
     * 修改用户反馈
     * @param userFeed
     * @return
     */
    @RequestMapping(value = "/updateUserFeed" ,method = RequestMethod.PUT)
    ResultVo updateUserFeed(TUserFeedDto userFeed);

    /**
     * yhs
     * 查询用户反馈
     * @param
     * @return
     */

    @RequestMapping(value = "/userFeedList" ,method = RequestMethod.POST)
    ResultVo userFeedList(@RequestBody Map<String, Object> condition);

}
