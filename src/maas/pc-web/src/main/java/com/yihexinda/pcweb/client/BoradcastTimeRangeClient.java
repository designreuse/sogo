package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TBoradcastTimeRangeDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@FeignClient(value = "data-service")
@RequestMapping("/broadcastTimeRange/client")
public interface BoradcastTimeRangeClient {

    /**
     *  添加广播时间段
     * @param boradcastTimeRangeDto
     * @return
     */
    @RequestMapping(value = "/addBroadcastTimeRange",method = RequestMethod.POST)
    ResultVo addBroadcastTimeRange(@RequestBody TBoradcastTimeRangeDto boradcastTimeRangeDto);

    /**
     * 删除广播时间段
     * @param broadcastId
     * @return
     */
    @RequestMapping(value = "/deleteBroadcastTimeRange/{broadcastId}",method = RequestMethod.GET)
    ResultVo deleteBroadcastTimeRange(@PathVariable("broadcastId") String broadcastId);


    /**
     *  修改广播时间段
     * @param boradcastTimeRangeDto
     * @return
     */
    @RequestMapping(value = "/updateBroadcastTimeRange",method = RequestMethod.PUT)
    ResultVo updateBroadcastTimeRange(@RequestBody TBoradcastTimeRangeDto boradcastTimeRangeDto);

    /**
     * 查询广播时间段
     * @param condition
     * @return
     */
    @RequestMapping(value = "/broadcastTimeRangeList",method = RequestMethod.POST)
    ResultVo broadcastTimeRangeList(@RequestBody Map<String, Object> condition);
}
