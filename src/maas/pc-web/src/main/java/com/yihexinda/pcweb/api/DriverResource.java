package com.yihexinda.pcweb.api;


import com.alibaba.fastjson.JSONObject;
import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.*;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.*;
import com.yihexinda.pcweb.client.DriverCarBindClient;
import com.yihexinda.pcweb.client.DriverClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/11/29
 */
@Api(description = "司机接口")
@RestController()
@RequestMapping("api/pc")
@Slf4j
public class DriverResource {

    @Autowired
    private DriverClient driverClient;

    @Autowired
    private DriverCarBindClient driverCarBindClient;

    /**
     * 查询司机信息列表
     * @return 司机信息列表
     */
    @ApiOperation(value = "查询司机信息列表", httpMethod = "POST")
    @RequestMapping(value = "/getDriverList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string")
    })
    public ResultVo getDriverList(@RequestBody Map<String,Object> condition) {
        return driverClient.getDriverList(condition);
    }

    /**
     * 查询司机信息
     * @param data 司机id
     * @return 司机信息
     */
    @ApiOperation(value = "查询司机详情", httpMethod = "POST")
    @RequestMapping(value = "/getDriverInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "id", value = "司机id", required = true, paramType = "string"),
    })
    public ResultVo getDriverInfo(@RequestBody Map<String,Object> data) {
        return driverClient.getDriverInfo(String.valueOf(data.get("id")));
    }

    /**
     * 查询司机历史车辆
     * @param data 司机id
     * @return 司机信息
     */
    @ApiOperation(value = "查询司机历史车辆列表", httpMethod = "POST")
    @RequestMapping(value = "/getDriverBindCarList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "driverId", value = "司机id", required = true, paramType = "string"),
    })
    public ResultVo getDriverBindCarList(@RequestBody Map<String,Object> data) {
        if ("".equals(StringUtil.trim(data.get("driverId")))){
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"司机id为空");
        }
        return driverClient.getDriverBindCarList(String.valueOf(data.get("driverId")));
    }


    /**
     * 新增司机信息
     * @param data 司机实体信息
     * @return ResultVo
     */
    @ApiOperation(value = "新增司机信息", httpMethod = "POST")
    @RequestMapping(value = "/addDriverInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "tDriverDto", value = "司机信息", required = true, paramType = "string")
    })
    public ResultVo addDriverInfo(@RequestBody Map<String,Object> data) {
        TDriverDto tDriverDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tDriverDto")),TDriverDto.class);
        PayLoadVo payLoadVo= RequestUtil.analysisToken(StringUtil.trim(data.get("token")));
        if (null!=payLoadVo){
            tDriverDto.setCreateId(payLoadVo.getUserId());
        }
        String password = tDriverDto.getPassword();
        if(!StringUtil.isEmpty(password)){
            tDriverDto.setPassword(new Md5().getMD5ofStr(password));
        }
        return driverClient.addDriverInfo(tDriverDto);
    }

    /**
     * 修改司机信息
     * @param data 司机信息
     * @return ResultVo
     */
    @ApiOperation(value = "修改司机信息", httpMethod = "PUT")
    @RequestMapping(value = "/updateDriverInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "tDriverDto", value = "司机信息", required = true, paramType = "string")
    })
    public ResultVo updateDriverInfo(@RequestBody Map<String,Object> data) {
        TDriverDto tDriverDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tDriverDto")),TDriverDto.class);
        String password = tDriverDto.getPassword();
        if(!StringUtil.isEmpty(password)){
            tDriverDto.setPassword(new Md5().getMD5ofStr(password));
        }
        return driverClient.updateDriverInfo(tDriverDto);
    }

    /**
     * 查询司机历史绑定车辆
     * @return 绑定车辆列表
     */
    @ApiOperation(value = "查询司机历史绑定车辆", httpMethod = "POST")
    @RequestMapping(value = "/getDriverCarBind")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "driverId", value = "司机id", required = true, paramType = "string")
    })
    public ResultVo getDriverCarBind(@RequestBody Map<String,Object> condition) {
        return driverCarBindClient.getDriverCarBind(condition);
    }

    /**
     * 司机批量排班
     * @param driverDtoList map参数
     * @return ResultVo
     */
    @ApiOperation(value = "司机批量排班", httpMethod = "POST")
    @RequestMapping(value = "/batchScheduling")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "driverDtoList", value = "司机列表", required = true, paramType = "List<TDriverDto>"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "scheduleId", value = "排班id", required = true, paramType = "string")
    })
    public ResultVo batchScheduling(@RequestBody Map<String, Object> driverDtoList) {
        return driverClient.batchScheduling(driverDtoList);
    }


    /**
     * 司机查询状态(用于批量排班)
     * @param data 查询参数
     * @return ResultVo
     */
    @PostMapping("getDriverListStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getDriverListStatus(@RequestBody Map<String, Object> data){
        //启用状态
        data.put("status","1");
        return driverClient.getDriverListStatus(data);
    }

    /**
     * 修改司机状态
     * @param data map参数
     * @return ResultVo
     */
    @ApiOperation(value = "修改司机状态", httpMethod = "POST")
    @RequestMapping(value = "/updateDriverStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "driverId", value = "司机id", required = true, paramType = "string")
    })
    public ResultVo updateDriverStatus(@RequestBody Map<String, Object> data) {
        TDriverDto tDriverDto =new TDriverDto();
        tDriverDto.setId(StringUtil.trim(data.get("driverId")));
        tDriverDto.setStatus(StringUtil.trim(data.get("status")));
        return driverClient.updateDriverStatus(tDriverDto);
    }


    /**
     *  导出司机列表Excel
     * @param response HttpServletResponse
     */
    @ApiOperation(value = "导出司机列表Excel", httpMethod = "GET")
    @RequestMapping(value = "/getDriverExcel")
    @NoRequireLogin
    public void getDriverExcel(HttpServletResponse response) {
        try {
            String fileName = "司机列表.xls";
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("Content-Type:application/vnd.ms-excel");
            String[] title = {"司机名称", "工号", "手机号", "司机状态","地址","工时段","创建时间","修改时间","创建人"};
            List<TDriverDto> tDriverDtoList = driverClient.getDriverExcel();
            String[][] objects = new String[tDriverDtoList.size()]
                    [tDriverDtoList.get(0).toString().split(",").length];
            for (int i = 0; i < tDriverDtoList.size(); i++) {
                TDriverDto tDriverDto = tDriverDtoList.get(i);
                objects[i][0] = tDriverDto.getName();
                objects[i][1] = tDriverDto.getNo();
                objects[i][2] = tDriverDto.getTelephone();
                if ("0".equals(tDriverDto.getStatus())){
                    objects[i][3] = "停用";
                }else {
                    objects[i][3] = "启用";
                }
                objects[i][4] = tDriverDto.getRegionAddress()+StringUtil.trim(tDriverDto.getDetailAddr());
                if (null!=tDriverDto.getDriverWorkHourList()&&tDriverDto.getDriverWorkHourList().size()>0){
                    StringBuilder date = new StringBuilder();
                    for (TDriverWorkHourDto tDriverWorkHourDto : tDriverDto.getDriverWorkHourList()) {
                        date.append(DateUtils.formatDate(tDriverWorkHourDto.getStartTime(),DateUtils.PATTERN_HH_mm_ss)+"  - "+DateUtils.formatDate(tDriverWorkHourDto.getEndTime(),DateUtils.PATTERN_HH_mm_ss));
                    }
                    objects[i][5] = String.valueOf(date);
                }else {
                    objects[i][5] = "";
                }
                if ("".equals(StringUtil.trim(tDriverDto.getCreateDate()))) {
                    objects[i][6] = "";
                }else {
                    objects[i][6] = DateUtils.formatDate(tDriverDto.getCreateDate(), DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
                }
                if ("".equals(StringUtil.trim(tDriverDto.getUpdateDate()))) {
                    objects[i][7] = "";
                }else {
                    objects[i][7] = DateUtils.formatDate(tDriverDto.getUpdateDate(), DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
                }
                objects[i][8] =  tDriverDto.getUserName();
            }
            HSSFWorkbook workbook = ExcelXUtils.getHSSFWorkbook(fileName, title, objects, null);
            workbook.write(output);
            output.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *  导出司机绑定车辆列表Excel
     * @param response HttpServletResponse
     */
    @ApiOperation(value = "导出司机绑定车辆列表Excel", httpMethod = "GET")
    @RequestMapping(value = "/getDriverCarBindExcel/{driverId}")
    @NoRequireLogin
    public void getDriverCarBindExcel(HttpServletResponse response,@PathVariable("driverId")String driverId) {
        try {
            String fileName = "绑定车辆列表.xls";
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("Content-Type:application/vnd.ms-excel");
            String[] title = {"司机名称", "车牌号", "车辆类型", "车辆载客人数","绑定时间","解绑时间"};
            List<TDriverCarBindDto> tDriverCarBindDtoList = driverCarBindClient.getDriverCarBindExcel(driverId);
            String[][] objects = new String[tDriverCarBindDtoList.size()]
                    [tDriverCarBindDtoList.get(0).toString().split(",").length];
            for (int i = 0; i < tDriverCarBindDtoList.size(); i++) {
                TDriverCarBindDto tDriverCarBindDto = tDriverCarBindDtoList.get(i);
                objects[i][0] = tDriverCarBindDto.getName();
                objects[i][1] = tDriverCarBindDto.getCarNo();
                if ("0".equals(tDriverCarBindDto.getType())){
                    objects[i][2] = "小巴";
                }
                if ("1".equals(tDriverCarBindDto.getType())){
                    objects[i][2] = "中巴";
                }
                if ("2".equals(tDriverCarBindDto.getType())){
                    objects[i][2] = "大巴";
                }
                objects[i][3] = tDriverCarBindDto.getPassNums();
                if ("".equals(StringUtil.trim(tDriverCarBindDto.getCreateDate()))) {
                    objects[i][4] = "";
                }else {
                    objects[i][4] = DateUtils.formatDate(tDriverCarBindDto.getCreateDate(), DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
                }
                if ("".equals(StringUtil.trim(tDriverCarBindDto.getUpdateDate()))){
                    objects[i][5] = "";
                }else {
                objects[i][5] =  DateUtils.formatDate(tDriverCarBindDto.getUpdateDate(),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
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
