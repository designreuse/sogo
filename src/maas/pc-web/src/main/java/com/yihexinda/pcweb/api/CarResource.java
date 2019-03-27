package com.yihexinda.pcweb.api;

import com.alibaba.fastjson.JSONObject;
import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.constants.SysParamConstant;
import com.yihexinda.core.utils.*;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.*;
import com.yihexinda.pcweb.client.CarClient;
import com.yihexinda.pcweb.client.SysParamClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author wenbn
 * @version 1.0
 * @date 2018/11/29 0029
 */
@Api(description = "车辆管理接口")
@RestController
@RequestMapping("/api/pc")
@Slf4j
public class CarResource {

    @Autowired
    private CarClient carClient;

    @Resource
    private SysParamClient sysParamClient;


    /**
     * wenbn
     * 添加车辆
     * @param data 参数
     * @return ResultVo
     */
    @ApiOperation(value = "添加车辆", httpMethod = "POST")
    @RequestMapping(value = "/addCar" ,method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tCarDto", value = "车辆信息", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo addCar(@RequestBody Map<String,Object> data) {
        TCarDto tCarDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tCarDto")),TCarDto.class);
        PayLoadVo payLoadVo= RequestUtil.analysisToken(StringUtil.trim(data.get("token")));
        if (null!=payLoadVo){
            tCarDto.setCreateId(payLoadVo.getUserId());
        }
        return carClient.addCar(tCarDto);
    }



    /**
     * wenbn
     * 修改车辆信息
     * @param data  参数
     * @return ResultVo
     */
    @ApiOperation(value = "修改车辆信息", httpMethod = "POST")
    @RequestMapping(value = "/updateCar" ,method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tCarDto", value = "车辆信息", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo updateCar(@RequestBody Map<String,Object> data) {
        TCarDto tCarDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tCarDto")),TCarDto.class);
        return carClient.updateCar(tCarDto);
    }

    /**
     * wenbn
     * 查询车辆列表
     * @param condition 参数
     * @return 车辆列表
     */
    @ApiOperation(value = "查询车辆列表", httpMethod = "POST")
    @PostMapping(value = "/carList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    ResultVo carList(@RequestBody Map<String,Object> condition) {
        return carClient.carList(condition);
    }

    /**
     * wenbn
     * 查询车辆位置
     * @param data 参数
     * @return 车辆经纬度
     */
    @ApiOperation(value = "查询车辆位置", httpMethod = "POST")
    @PostMapping(value = "/queryCarPosition")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carNo", value = "车牌号", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    ResultVo queryCarPosition(@RequestBody Map<String,Object> data) {
        return carClient.queryCarPosition(data);
    }


    /**
     * 查询车辆可预约人数配置（系统配置表 key："appointmentNumber"）
     * @param data 参数
     * @return ResultVo
     */
    @ApiOperation(value = "查询车辆可预约人数配置", httpMethod = "POST")
    @PostMapping(value = "/getAppointmentNumber")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    ResultVo getAppointmentNumber(@RequestBody Map<String,Object> data) {
        return sysParamClient.getAppointmentNumber(SysParamConstant.APPOINTMENT_NUMBER);
    }

    /**
     * 设置车辆可预约人数（系统配置表 key："appointmentNumber"）
     * @param data 参数
     * @return ResultVo
     */
    @ApiOperation(value = "配置车辆预约人数", httpMethod = "POST")
    @PostMapping(value = "/updateAppointmentNumber")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, paramType = "string"),
            @ApiImplicitParam(name = "paramValue", value = "参数值", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    ResultVo updateAppointmentNumber(@RequestBody Map<String,Object> data) {
        if(data == null){
           return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        String id = StringUtil.trim(data.get("id"));
        String paramValue = StringUtil.trim(data.get("paramValue"));
        if(StringUtil.isEmpty(id)){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"修改信息有误，请检查");
        }
        if(StringUtil.isEmpty(paramValue)){
            return ResultVo.success();
        }
        if(StringUtil.getAsInt(paramValue,-1)<1){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"请输入数值类型的数据");
        }
        TSysParamDto tSysParamDto = new TSysParamDto();
        tSysParamDto.setId(id);
        tSysParamDto.setParamValue(paramValue);
        return sysParamClient.updateAppointmentNumber(tSysParamDto);
    }

    /**
     * 查询车辆详情信息
     * @param data 参数
     * @return 车辆经纬度
     */
    @ApiOperation(value = "查询车辆详情信息", httpMethod = "POST")
    @PostMapping(value = "/getCarId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carId", value = "车辆id", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    ResultVo getCarId(@RequestBody Map<String,Object> data) {
        if ("".equals(StringUtil.trim(data.get("carId")))){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"车辆id不能为空");
        }
        return carClient.getCarId(StringUtil.trim(data.get("carId")));
    }


    /**
     * 修改车辆状态
     * @param data 参数
     * @return 车辆经纬度
     */
    @ApiOperation(value = "查询车辆详情信息", httpMethod = "POST")
    @PostMapping(value = "/updateCarStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carId", value = "车辆id", required = true, paramType = "string"),
            @ApiImplicitParam(name = "status", value = "车辆状态", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    ResultVo updateCarStatus(@RequestBody Map<String,Object> data) {
        if ("".equals(StringUtil.trim(data.get("carId")))&&"".equals(StringUtil.trim(data.get("status")))){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"车辆id或者状态为空");
        }
        TCarDto carDto =new TCarDto();
        carDto.setId(StringUtil.trim(data.get("carId")));
        carDto.setStatus(StringUtil.trim(data.get("status")));
        return carClient.updateCar(carDto);
    }
    /**
     * 查询车辆可用总数
     * @param data 参数
     */
    @ApiOperation(value = "查询车辆可用总数", httpMethod = "POST")
    @PostMapping(value = "/getCarCount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    ResultVo getCarCount(@RequestBody Map<String,Object> data) {
        return ResultVo.success().setDataSet(carClient.getCarCount());
    }

    /**
     * 查询状态为可用的车辆
     * @param data 参数
     */
    @ApiOperation(value = "查询状态为可用的车辆", httpMethod = "POST")
    @PostMapping(value = "/getCarListStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    ResultVo getCarListStatus(@RequestBody Map<String,Object> data) {
        return carClient.getCarListStatus();
    }

    /**
     * 车辆列表导出
     */
    @ApiOperation(value = "车辆列表导出", httpMethod = "POST")
    @GetMapping(value = "/getCarListExcel")
    @NoRequireLogin
    void getCarListExcel(HttpServletResponse response) {
        try {
            String fileName = "车辆列表.xls";
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("Content-Type:application/vnd.ms-excel");
            String[] title = {"车牌号", "车辆类型", "车辆载客人数", "车辆状态", "地址","创建时间","修改时间","创建人"};
            List<TCarDto> tCarDtoList = carClient.getCarListExcel();
            String[][] objects = new String[tCarDtoList.size()]
                    [tCarDtoList.get(0).toString().split(",").length];
            for (int i = 0; i < tCarDtoList.size(); i++) {
                TCarDto tCarDto = tCarDtoList.get(i);
                objects[i][0] = tCarDto.getCarNo();
                if ("0".equals(tCarDto.getType())){
                    objects[i][1] = "小巴";
                }
                if ("1".equals(tCarDto.getType())){
                    objects[i][1] = "中巴";
                }
                if ("2".equals(tCarDto.getType())){
                    objects[i][1] = "大巴";
                }
                objects[i][2] = String.valueOf(tCarDto.getPassNums());
                if ("0".equals(tCarDto.getStatus())){
                    objects[i][3] = "停用";
                }
                if ("1".equals(tCarDto.getStatus())){
                    objects[i][3] = "启用";
                }
                objects[i][4] = tCarDto.getRegionAddress();
                if ("".equals(StringUtil.trim(tCarDto.getCreateDate()))){
                    objects[i][5] ="";
                }else{
                    objects[i][5] =  DateUtils.formatDate(tCarDto.getCreateDate(),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
                }
                if ("".equals(StringUtil.trim(tCarDto.getUpdateDate()))){
                    objects[i][6] ="";
                }else{
                    objects[i][6] =  DateUtils.formatDate(tCarDto.getUpdateDate(),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
                }
                objects[i][7] =tCarDto.getCreateName();
            }
            HSSFWorkbook workbook = ExcelXUtils.getHSSFWorkbook(fileName, title, objects, null);
            workbook.write(output);
            output.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }


    }

}
