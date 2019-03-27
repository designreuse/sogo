package com.yihexinda.dataservice.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TRouteOffpeakDto;
import com.yihexinda.data.dto.TRoutePeakDto;
import com.yihexinda.data.dto.TTravelEvaluateDto;
import com.yihexinda.dataservice.service.TRouteOffpeakService;
import com.yihexinda.dataservice.service.TRoutePeakService;
import com.yihexinda.dataservice.service.TTravelEvaluateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 行程评价管理
 *
 * @author yhs
 * @version 1.0
 * @date 2018/12/3 0003
 */
@RestController
@RequestMapping("/TravelEvaluateApiResource/client")
@Slf4j
public class TravelEvaluateApiResource {

    @Autowired
    private TTravelEvaluateService travelEvaluateService;
    @Autowired
    private TRouteOffpeakService routeOffpeakService;
    @Autowired
    private TRoutePeakService routePeakService;


    /**
     * 添加行程评价
     *
     * @param condition
     * @return
     */
    @PostMapping(value = "/saveTravelEvaluate")
    public ResultVo saveTravelEvaluate(@RequestBody Map<String, Object> condition) {
        String token = StringUtil.trim(condition.get("token"));
        String userId = RequestUtil.analysisToken(token).getUserId();
        String content = StringUtil.trim(condition.get("content"));
        String startNo = StringUtil.trim(condition.get("startNo"));
        String routeId = StringUtil.trim(condition.get("routeId"));
        String carEnvStartNo = StringUtil.trim(condition.get("carEnvStartNo"));
        String orderId = StringUtil.trim(condition.get("orderId"));
        //使用UUID生成主键,插入表数据
        String id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        //组装评价数据
        TTravelEvaluateDto travelEvaluateDto = new TTravelEvaluateDto();
        if(!StringUtil.isEmpty(routeId)){
            //根据行程id判断是平峰还是高峰
            TRouteOffpeakDto routeOffpeakDto = routeOffpeakService.getById(routeId);
            if (routeOffpeakDto == null) {
                //高峰
                TRoutePeakDto routePeakDto = routePeakService.getByRouteId(routeId);
//            //判断查询是否存在
                if (routePeakDto == null) {
//                return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_PARAMETER_ERROR_VALUE);
                }else{
                    travelEvaluateDto.setDriverUserId(routePeakDto.getDriverUserId());
                }
                //设置平高峰类型'平峰0 高峰1
                travelEvaluateDto.setRouteType("1");
            } else {
                //设置平高峰类型'平峰0 高峰1
                travelEvaluateDto.setDriverUserId(routeOffpeakDto.getDriverUserId());
                travelEvaluateDto.setRouteType("0");
            }
        }
        travelEvaluateDto.setId(id);
        travelEvaluateDto.setUserId(userId);
        travelEvaluateDto.setContent(content);
        travelEvaluateDto.setStartNo(startNo);
        travelEvaluateDto.setCarEnvStartNo(carEnvStartNo);
        travelEvaluateDto.setRouteId(routeId);
        travelEvaluateDto.setOrderId(orderId);
        travelEvaluateDto.setCreateDate(new Date());
        travelEvaluateDto.setCreateId(userId);
        boolean success = travelEvaluateService.save(travelEvaluateDto);
        if (success) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 删除行程评价
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteTravelEvaluate/{id}", method = RequestMethod.POST)
    public ResultVo deleteTravelEvaluate(@PathVariable(value = "id") String id) {
        TTravelEvaluateDto travelEvaluateDto = travelEvaluateService.getById(id);
        if (null != travelEvaluateDto) {
            boolean success = travelEvaluateService.removeById(id);
            if (success) {
                return ResultVo.success();
            }
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 修改行程评价
     *
     * @param travelEvaluateDto
     * @return
     */
    @PostMapping(value = "/updateTravelEvaluate")
    public ResultVo updateTravelEvaluate(@RequestBody TTravelEvaluateDto travelEvaluateDto) {
        if (travelEvaluateService.updateById(travelEvaluateDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }


    /**
     * 查询用户行程评价
     *
     * @param userId 用户id
     * @return 行程列表
     */
    @GetMapping("/getTravelEvaluateList/{userId}")
    public ResultVo getTravelEvaluateList(@PathVariable String userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        //获取行程评价列表,展示在后台
        return ResultVo.success().setDataSet(travelEvaluateService.list(queryWrapper));
    }

    /**
     * 行程管理列表
     */
    @GetMapping("/getTravelEvaluateManager")
    public ResultVo getTravelEvaluateManager() {
        List<Map<String, Object>> travelEvaluateList = travelEvaluateService.getTravelEvaluateManager();
        return ResultVo.success().setDataSet(travelEvaluateList);
    }

    /**
     * 查询评价列表
     *
     * @param data 参数
     * @return 评价列表
     */
    @PostMapping("/travelEvaluateList")
    public ResultVo travelEvaluateList(@RequestBody Map<String,Object> data) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(data.get("pageIndex")),1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(data.get("pageSize")),10);
        if (!"".equals(StringUtil.trim(data.get("startTime")))&&!"".equals(data.get("endTime"))){
            Timestamp aStartTime = Timestamp.valueOf(StringUtil.trim(data.get("startTime")));
            String endTime = StringUtil.trim(data.get("endTime")).replace("00:00:00","23:59:59");
            Timestamp aEndTime = Timestamp.valueOf(endTime);
            data.put("aStartTime", aStartTime);
            data.put("aEndTime", aEndTime);
        }

        ResultVo resultVo = new AbstractPageTemplate<TTravelEvaluateDto>() {
            @Override
            protected List<TTravelEvaluateDto> executeSql() {
                List<TTravelEvaluateDto> list = travelEvaluateService.travelEvaluateList(data);
                return list;
            }
        }.preparePageTemplate(pageIndex,pageSize);
        return resultVo;
        //return ResultVo.success().setDataSet(travelEvaluateService.travelEvaluateList());
    }

    /**
     *Excel导出列表（列表不分页）
     *
     * @return 评价列表(不分页)
     */
    @GetMapping("/travelEvaluateExcel")
    public List<TTravelEvaluateDto> travelEvaluateExcel() {

        return travelEvaluateService.travelEvaluateList(null);
    }


}
