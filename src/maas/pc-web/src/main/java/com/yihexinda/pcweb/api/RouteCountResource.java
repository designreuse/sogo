package com.yihexinda.pcweb.api;


import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.ExcelXUtils;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TUserFeedDto;
import com.yihexinda.pcweb.client.RouteCountClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/12/6
 */
@Api(description = "工时段统计接口")
@RestController()
@RequestMapping("api/pc")
@Slf4j
public class RouteCountResource {

    @Autowired
    private RouteCountClient routeCountClient;

    /**
     * 历史工时段统计
     *
     * @return 历史工时段统计
     */
    @ApiOperation(value = "历史工时段统计列表", httpMethod = "POST")
    @RequestMapping(value = "/getListCount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getListCount(@RequestBody Map<String,Object> condition) {
        return routeCountClient.getListCount(condition);
    }

    /**
     * 当天工时段统计
     *
     * @return 当天工时段统计
     */
    @ApiOperation(value = "当天工时段统计列表", httpMethod = "POST")
    @RequestMapping(value = "/getRouteStation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getRouteStation(@RequestBody Map<String,Object> condition) {
        return routeCountClient.getRouteStation(condition);
    }


    /**
     * 历史工时段统计（导出）
     *
     */
    @ApiOperation(value = "历史工时段统计列表(导出)", httpMethod = "GET")
    @RequestMapping(value = "/getListCountExcel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    @NoRequireLogin
    public void getListCountExcel(HttpServletResponse response) {
        try {
            String fileName = "历史工时段统计列表.xls";
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("Content-Type:application/vnd.ms-excel");
            String[] title = {"司机名称", "行程类型", "发车站点", "终点", "载客人数","途径站点数","总里程","开始时间","结束时间"};
            List<Map> mapList = routeCountClient.getListCountExcel();
//            List<Map> mapList = routeCountClient.getRouteStationExcel();
            String[][] objects = new String[mapList.size()]
                    [mapList.get(0).toString().split(",").length];
            for (int i = 0; i < mapList.size(); i++) {
                Map map = mapList.get(i);
                objects[i][0] = StringUtil.trim(map.get("name"));
                objects[i][1] = StringUtil.trim(map.get("type"));
                objects[i][2] = StringUtil.trim(map.get("start_name"));
                objects[i][3] = StringUtil.trim(map.get("end_name"));
                objects[i][4] = StringUtil.trim(map.get("on_num"));
                objects[i][5] = StringUtil.trim(map.get("total_station"));
                objects[i][6] = StringUtil.trim(map.get("pre_station_mile"));
                if ("".equals(StringUtil.trim(map.get("start_date")))){
                    objects[i][7] ="";
                }else{
                    objects[i][7] =  DateUtils.formatDate(new Date(Long.valueOf(StringUtil.trim(map.get("start_date")))),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);;
                }
                if ("".equals(StringUtil.trim(map.get("end_date")))){
                    objects[i][8] ="";
                }else{
                    objects[i][8] =  DateUtils.formatDate(new Date(Long.valueOf(StringUtil.trim(map.get("end_date")))),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);;
                }
            }
            HSSFWorkbook workbook = ExcelXUtils.getHSSFWorkbook(fileName, title, objects, null);
            workbook.write(output);
            output.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 当天工时段统计（导出）
     *
     */
    @ApiOperation(value = "当天工时段统计列表（导出）", httpMethod = "GET")
    @RequestMapping(value = "/getRouteStationExcel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    @NoRequireLogin
    public void getRouteStationExcel(HttpServletResponse response) {
        try {
            String fileName = "当天工时段统计列表.xls";
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("Content-Type:application/vnd.ms-excel");
            String[] title = {"司机名称", "行程类型", "到站站点", "上车人数", "下车人数","站点间距(KM)","到站时间"};
            List<Map> mapList = routeCountClient.getRouteStationExcel();
            String[][] objects = new String[mapList.size()]
                    [mapList.get(0).toString().split(",").length];
            for (int i = 0; i < mapList.size(); i++) {
                Map map = mapList.get(i);
                objects[i][0] = StringUtil.trim(map.get("name"));
                objects[i][1] = StringUtil.trim(map.get("type"));
                objects[i][2] = StringUtil.trim(map.get("site_name"));
                objects[i][3] = StringUtil.trim(map.get("on_num"));
                objects[i][4] = StringUtil.trim(map.get("off_num"));
                objects[i][5] = StringUtil.trim(map.get("pre_station_mile"));

                if ("".equals(StringUtil.trim(map.get("create_date")))){
                    objects[i][6] ="";
                }else{
                    objects[i][6] =  DateUtils.formatDate(new Date(Long.valueOf(StringUtil.trim(map.get("create_date")))),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);;
                }
            }
            HSSFWorkbook workbook = ExcelXUtils.getHSSFWorkbook(fileName, title, objects, null);
            workbook.write(output);
            output.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
}



}
