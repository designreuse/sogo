package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TBroadcastDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@FeignClient(value = "data-service")
@RequestMapping("/broadcast/client")
public interface BroadcastClient {

    /**
     *  添加广播
     * @param tbroadcastDto
     * @return
     */
    @RequestMapping(value = "/addBroadcast", method = RequestMethod.POST)
     ResultVo addBroadcast(@RequestBody TBroadcastDto tbroadcastDto);


    /**
     *  删除广播
     * @param broadcastId
     * @return
     */
    @RequestMapping(value = "/deleteBroadcast/{broadcastId}",method = RequestMethod.GET)
    ResultVo deleteBroadcast(@PathVariable("broadcastId") String broadcastId);


    /**
     *  修改广播
     * @param tbroadcastDto
     * @return
     */
    @RequestMapping(value = "/updateBroadcast",method = RequestMethod.PUT)
     ResultVo updateBroadcast(@RequestBody TBroadcastDto tbroadcastDto);

    /**
     * 查询广播
     * @param condition
     * @return
     */
    @RequestMapping(value = "/broadcastList",method = RequestMethod.POST)
    ResultVo broadcastList(@ApiIgnore @RequestBody Map<String, Object> condition);


    /**
     * 查询广播详情
     * @param id 广播id
     * @return ResultVo
     */
    @RequestMapping(value = "/getBroadcast/{id}",method = RequestMethod.GET)
    ResultVo getBroadcast(@PathVariable("id") String id);

    /**
     *  修改广播状态
     * @param tbroadcastDto 广播信息
     * @return ResultVo
     */
    @RequestMapping(value = "/updateBroadcastStatus",method = RequestMethod.POST)
    ResultVo updateBroadcastStatus(@RequestBody TBroadcastDto tbroadcastDto);
}
