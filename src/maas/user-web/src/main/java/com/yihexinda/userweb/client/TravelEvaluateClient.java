package com.yihexinda.userweb.client;


import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TTravelEvaluateDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 行程评价管理
 *
 * @author yhs
 * @version 1.0
 * @date 2018/12/4 0003
 */
@FeignClient(value = "data-service")
@RequestMapping("/TravelEvaluateApiResource/client")
public interface TravelEvaluateClient {


    /**
     * 添加行程评价
     *
     * @param travelEvaluateDto
     * @return
     */
    @PostMapping(value = "/saveTravelEvaluate")
    public ResultVo saveTravelEvaluate(@RequestBody Map<String, Object> condition);


    /**
     * 删除行程评价
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteTravelEvaluate/{id}", method = RequestMethod.GET)
    public ResultVo deleteTravelEvaluate(@PathVariable(value = "id") String id);


    /**
     * 修改行程评价
     *
     * @param travelEvaluateDto
     * @return
     */

    @PostMapping(value = "/updateTravelEvaluate")
    public ResultVo updateTravelEvaluate(@RequestBody TTravelEvaluateDto travelEvaluateDto);


    /**
     * 查询用户行程评价
     *
     * @param userId 用户id
     * @return 行程列表
     */
    @GetMapping("/getTravelEvaluateList/{userId}")
    ResultVo getTravelEvaluateList(@PathVariable(value = "userId") String userId);

    /**
     * 行程管理列表
     */
    @GetMapping("/getTravelEvaluateManager")
    ResultVo getTravelEvaluateManager();
}
