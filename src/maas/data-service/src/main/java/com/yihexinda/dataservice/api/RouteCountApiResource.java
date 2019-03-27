package com.yihexinda.dataservice.api;

import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.dataservice.service.RouteCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author pengfeng
 * @version 1.0
 * @date 2018/11/30
 */
@RestController
@RequestMapping("/routeCount/client")
@Slf4j
public class RouteCountApiResource {

    @Autowired
    private RouteCountService routeCountService;

    /**
     * 历史工时段统计
     *
     * @return 历史工时段统计
     */
    @PostMapping("/getListCount")
    public ResultVo getListCount(@RequestBody Map<String,Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")),1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")),10);
        if (!"".equals(StringUtil.trim(condition.get("startTime")))&&!"".equals(condition.get("endTime"))){
            Timestamp aStartTime = Timestamp.valueOf(StringUtil.trim(condition.get("startTime")));
            String endTime = StringUtil.trim(condition.get("endTime")).replace("00:00:00","23:59:59");
            Timestamp aEndTime = Timestamp.valueOf(endTime);
            condition.put("aEndTime", aEndTime);
        }
        ResultVo resultVo = new AbstractPageTemplate<Map>() {
            @Override
            protected List<Map> executeSql() {
                List<Map> list = routeCountService.getListCount(condition);
                return list;
            }
        }.preparePageTemplate(pageIndex,pageSize);
        return resultVo;
        //return ResultVo.success().setDataSet(routeCountService.getListCount());
    }


    /**
     * 当天工时段统计
     *
     * @return 当天工时段统计
     */
    @PostMapping("/getRouteStation")
    public ResultVo getRouteStation(@RequestBody Map<String,Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")),1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")),10);
        if (!"".equals(StringUtil.trim(condition.get("startTime")))&&!"".equals(condition.get("endTime"))){
            Timestamp aStartTime = Timestamp.valueOf(StringUtil.trim(condition.get("startTime")));
            String endTime = StringUtil.trim(condition.get("endTime")).replace("00:00:00","23:59:59");
            Timestamp aEndTime = Timestamp.valueOf(endTime);
            condition.put("aStartTime", aStartTime);
            condition.put("aEndTime", aEndTime);
        }
        ResultVo resultVo = new AbstractPageTemplate<Map>() {
            @Override
            protected List<Map> executeSql() {
                List<Map> list = routeCountService.getRouteStation(condition);
                return list;
            }
        }.preparePageTemplate(pageIndex,pageSize);
        return resultVo;
        //return ResultVo.success().setDataSet(routeCountService.getRouteStation());
    }

    /**
     * 当天工时段统计（导出）
     *
     * @return 当天工时段统计
     */
    @GetMapping("/getRouteStationExcel")
    public List<Map> getRouteStationExcel() {
        return  routeCountService.getRouteStation(null);
    }

    /**
     * 历史工时段统计（导出）
     *
     * @return 历史工时段统计
     */
    @GetMapping("/getListCountExcel")
    public List<Map> getListCountExcel() {
        return  routeCountService.getListCount(null);
    }

}
