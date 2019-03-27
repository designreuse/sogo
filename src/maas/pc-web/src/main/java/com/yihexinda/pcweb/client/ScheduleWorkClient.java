package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TScheduleWorkDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/11/29
 */
@FeignClient(value = "data-service")
@RequestMapping("/scheduleWork/client")
public interface ScheduleWorkClient {

    /**
     * pengFeng
     * 查询排班信息列表
     * @param  condition 参数信息
     * @return 司机信息排班
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getScheduleWorkList")
    ResultVo getScheduleWorkList(@RequestBody Map<String,Object> condition);

    /**
     * pengFeng
     * 排班详情
     * @param id 排班id
     * @return  排班详情
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getScheduleWork/{id}")
    ResultVo getScheduleWork(@PathVariable("id") String id);

    /**
     * pengFeng
     *  新增排班信息
     * @param tScheduleWorkDto 排班信息
     * @return ResultVo
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addScheduleWork")
    ResultVo addScheduleWork(@RequestBody TScheduleWorkDto tScheduleWorkDto);

    /**
     *  修改排班信息
     * @param tScheduleWorkDto 排班信息
     * @return ResultVo
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/updateScheduleWork")
    ResultVo updateScheduleWork(@RequestBody TScheduleWorkDto tScheduleWorkDto);

    /**
     * 查询排班信息列表(不分页)
     * @return 司机信息排班
     */
    @RequestMapping(method = RequestMethod.GET, value = "/queryScheduleWorkList")
    ResultVo queryScheduleWorkList();

    /**
     *  修改排班状态
     * @param tScheduleWorkDto 排班信息
     * @return ResultVo
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/updateScheduleWorkStatus")
    ResultVo updateScheduleWorkStatus(@RequestBody TScheduleWorkDto tScheduleWorkDto);
    /**
     * 查询排班列表（用于司机设置排班）
     *
     * @return 排班列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/queryScheduleList")
    ResultVo queryScheduleList();
}
