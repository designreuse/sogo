package com.yihexinda.pcweb.api;


import com.alibaba.fastjson.JSONObject;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.JsonUtil;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TDriverDto;
import com.yihexinda.data.dto.TScheduleWorkDto;
import com.yihexinda.pcweb.client.DriverClient;
import com.yihexinda.pcweb.client.ScheduleWorkClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/12/6
 */
@Api(description = "排班信息接口")
@RestController()
@RequestMapping("api/pc")
@Slf4j
public class ScheduleWorkResource {

    @Autowired
    private ScheduleWorkClient scheduleWorkClient;

    /**
     * 查询排班信息列表
     * @return 排班信息列表
     */
    @ApiOperation(value = "查询排班信息列表", httpMethod = "POST")
    @RequestMapping(value = "/getScheduleWorkList",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getScheduleWorkList(@RequestBody Map<String,Object> condition) {
        return scheduleWorkClient.getScheduleWorkList(condition);
    }

    /**
     * 查询排班详情
     * @param data 排班id
     * @return 排班详情
     */
    @ApiOperation(value = "查询排班信息", httpMethod = "POST")
    @RequestMapping(value = "/ScheduleWork",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "排班id", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getScheduleWork(@RequestBody Map<String,Object> data) {
        return scheduleWorkClient.getScheduleWork(String.valueOf(data.get("id")));
    }

    /**
     * 新增排班信息
     * @param data 排班信息
     * @return ResultVo
     */
    @ApiOperation(value = "新增排班信息", httpMethod = "POST")
    @RequestMapping(value = "/addScheduleWork",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tScheduleWorkDto", value = "排班信息", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo addScheduleWork(@RequestBody Map<String,Object> data) {
        if ("null".equals(String.valueOf(data.get("tScheduleWorkDto")))){
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR,ResultConstant.SYS_REQUIRED_PARAMETER_ERROR_VALUE);
        }
        TScheduleWorkDto tScheduleWorkDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tScheduleWorkDto")),TScheduleWorkDto.class);
        PayLoadVo payLoadVo= RequestUtil.analysisToken(StringUtil.trim(data.get("token")));
        if (null!=payLoadVo){
            tScheduleWorkDto.setCreateId(payLoadVo.getUserId());
        }
        return scheduleWorkClient.addScheduleWork(tScheduleWorkDto);
    }

    /**
     * 修改排班信息信息
     * @param data 排班信息
     * @return ResultVo
     */
    @ApiOperation(value = "修改排班信息", httpMethod = "PUT")
    @RequestMapping(value = "/updateScheduleWork")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tScheduleWorkDto", value = "排班信息", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo updateScheduleWork(@RequestBody Map<String,Object> data) {
        TScheduleWorkDto tScheduleWorkDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tScheduleWorkDto")),TScheduleWorkDto.class);
        return scheduleWorkClient.updateScheduleWork(tScheduleWorkDto);
    }

    /**
     * 查询排班信息列表(不分页)
     * @param   data token参数
     * @return 排班信息列表
     */
    @ApiOperation(value = "查询排班信息列表", httpMethod = "POST")
    @RequestMapping(value = "/queryScheduleWorkList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo queryScheduleWorkList(@RequestBody Map<String,Object> data) {
        return scheduleWorkClient.queryScheduleWorkList();
    }


    /**
     * 修改排班状态
     * @param data 排班信息
     * @return ResultVo
     */
    @ApiOperation(value = "修改排班状态", httpMethod = "PUT")
    @RequestMapping(value = "/updateScheduleWorkStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tScheduleWorkDto", value = "排班信息", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo updateScheduleWorkStatus(@RequestBody Map<String,Object> data) {
        TScheduleWorkDto tScheduleWorkDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tScheduleWorkDto")),TScheduleWorkDto.class);
        return scheduleWorkClient.updateScheduleWorkStatus(tScheduleWorkDto);
    }

    /**
     * 查询排班列表（用于司机设置排班）
     *
     * @return 排班列表
     */
    @ApiOperation(value = "查询排班信息列表", httpMethod = "POST")
    @RequestMapping(value = "/queryScheduleList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo queryScheduleList(@RequestBody Map<String,Object> data) {
        return scheduleWorkClient.queryScheduleList();
    }

}
