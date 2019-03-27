package com.yihexinda.pcweb.api;

import com.alibaba.fastjson.JSONObject;
import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.*;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.*;
import com.yihexinda.pcweb.client.LineClient;
import com.yihexinda.pcweb.client.RoutePeakClient;
import com.yihexinda.pcweb.client.StationClient;
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
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 高峰行程信息管理类
 * @author wenbn
 * @version 1.0
 * @date 2018/11/30 0030
 */
@RestController
@RequestMapping("api/pc")
@Slf4j
@Api(description = "高峰行程信息管理接口")
public class RoutePeakResource {

    @Autowired
    private RoutePeakClient routePeakClient;

    @Resource
    private LineClient lineClient;

    /**
     * 站点信息
     */
    @Resource
    private StationClient stationClient;

    /**
     * wenbn
     * 添加高峰行程
     * @param data 高峰行程参数
     * @return ResultVo
     */
    @ApiOperation(value = "添加高峰行程", httpMethod = "POST")
    @RequestMapping(value = "/addRoutePeak" ,method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "tRoutePeakDto", value = "高峰信息", required = true, paramType = "string")
    })
    public ResultVo addRoutePeak(@RequestBody Map<String,Object> data) {
        TRoutePeakDto tRoutePeakDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tRoutePeakDto")),TRoutePeakDto.class);
        if (tRoutePeakDto==null){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"参数不正确，请检验参数");
        }
        PayLoadVo payLoadVo= RequestUtil.analysisToken(StringUtil.trim(data.get("token")));
        if (null!=payLoadVo){
            tRoutePeakDto.getLineDto().setCreateId(payLoadVo.getUserId());
        }
        return routePeakClient.addRoutePeak(tRoutePeakDto);
    }

    /**
     * wenbn
     * 修改高峰行程
     * @param data 高峰行程参数
     * @return ResultVo
     */
    @ApiOperation(value = "修改高峰行程", httpMethod = "POST")
    @RequestMapping(value = "/updateRoutePeak" ,method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "tRoutePeakDto", value = "高峰信息", required = true, paramType = "string")
    })
    public ResultVo updateRoutePeak(@RequestBody Map<String,Object> data) {
        TRoutePeakDto tRoutePeakDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tRoutePeakDto")),TRoutePeakDto.class);
        return routePeakClient.updateRoutePeak(tRoutePeakDto);
    }

    /**
     * wenbn
     * 高峰详情
     * @param data 高峰行程id
     * @return ResultVo
     */
    @ApiOperation(value = "查询高峰行程", httpMethod = "POST")
    @RequestMapping(value = "/getRoutePeakId" ,method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "id", value = "高峰id", required = true, paramType = "string")
    })
    public ResultVo getRoutePeakId(@RequestBody Map<String,Object> data) {
        return routePeakClient.getRoutePeakId(String.valueOf(data.get("id")));
    }


    /**
     *
     * 查询高峰行程列表
     * @param condition 参数
     * @return 高峰行程列表
     */
    @ApiOperation(value = "查询高峰行程", httpMethod = "POST")
    @PostMapping(value = "/routePeakList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo routePeakList(@RequestBody Map<String,Object> condition){
        return routePeakClient.routePeakList(condition);
    }


    /**
     *
     * 查询高峰站点列表
     * @param data 参数
     * @return 高峰站点列表
     */
    @ApiOperation(value = "高峰站点列表", httpMethod = "POST")
    @PostMapping(value = "/stationList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo stationList(@RequestBody Map<String,Object> data){
        return stationClient.stationList();
    }


    /**
     * 修改线路状态
     * @param data 参数
     * @return ResultVo
     */
    @ApiOperation(value = "修改线路状态", httpMethod = "POST")
    @RequestMapping(value = "/updateLineStatus" ,method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "id", value = "线路ID", required = true, paramType = "string"),
            @ApiImplicitParam(name = "lineState", value = "线路状态", required = true, paramType = "string")
    })
    public ResultVo updateLineStatus(@RequestBody Map<String,Object> data) {
        String id = StringUtil.trim(data.get("id"));
        String lineState = StringUtil.trim(data.get("lineState"));
        if ("".equals(id)||"".equals(lineState)){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"参数缺少，请检查");
        }
        TLineDto tLineDto =new TLineDto();
        tLineDto.setId(id);
        tLineDto.setLineState(lineState);
        return lineClient.updateLine(tLineDto);
    }

    /**
     * 高峰线路分配司机
     * @param data 参数
     * @return ResultVo
     */
    @ApiOperation(value = "高峰线路分配司机", httpMethod = "POST")
    @RequestMapping(value = "/distribution" ,method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "lineDriverBindDtoList", value = "司机列表", required = true, paramType = "string"),
            @ApiImplicitParam(name = "lineId", value = "线路id", required = true, paramType = "string"),
    })
    public ResultVo distribution(@RequestBody Map<String,Object> data) {
        return routePeakClient.distribution(data);
    }


    /**
     *
     * 查询高峰列表（导出）
     */
    @ApiOperation(value = "高峰站点列表", httpMethod = "GET")
    @GetMapping(value = "/getRoutePeakListExcel")
    @NoRequireLogin
    public void getRoutePeakListExcel(HttpServletResponse response) {
        try {
            String fileName = "高峰列表.xls";
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("Content-Type:application/vnd.ms-excel");
            String[] title = {"线路名称", "线路状态", "发车间隔（分）", "发车区域", "发车站点","终点站","途径站点","创建时间","修改时间","创建人"};
            List<TRoutePeakDto> tRoutePeakDtoList =   routePeakClient.getRoutePeakListExcel();
            String[][] objects = new String[tRoutePeakDtoList.size()]
                    [tRoutePeakDtoList.get(0).toString().split(",").length];
            for (int i = 0; i < tRoutePeakDtoList.size(); i++) {
                TRoutePeakDto tRoutePeakDto = tRoutePeakDtoList.get(i);
                objects[i][0] = tRoutePeakDto.getLineName();
                if ("0".equals(tRoutePeakDto.getLineState())){
                    objects[i][1] = "停用";
                }
                if ("1".equals(tRoutePeakDto.getLineState())){
                    objects[i][1] = "启用";
                }
                objects[i][2] = String.valueOf(tRoutePeakDto.getTimeLip());
                objects[i][3] = tRoutePeakDto.getRegionAddress();
                objects[i][4] = tRoutePeakDto.getLineStartName();
                objects[i][5] = tRoutePeakDto.getLineEndName();
                if (tRoutePeakDto.getViaList().size()>0){
                    StringBuilder station = new StringBuilder();
                    for (TViaDto tViaDto : tRoutePeakDto.getViaList()) {
                        station.append(tViaDto.getSiteName());
                    }
                    objects[i][6] = String.valueOf(station);
                }else {
                    objects[i][6] = "";
                }

                if ("".equals(StringUtil.trim(tRoutePeakDto.getCreateDate()))){
                    objects[i][7] ="";
                }else{
                    objects[i][7] =  DateUtils.formatDate(tRoutePeakDto.getCreateDate(),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
                }
                if ("".equals(StringUtil.trim(tRoutePeakDto.getUpdateDate()))){
                    objects[i][8] ="";
                }else{
                    objects[i][8] =  DateUtils.formatDate(tRoutePeakDto.getUpdateDate(),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
                }
                objects[i][9] = tRoutePeakDto.getCreateName();
            }
            HSSFWorkbook workbook = ExcelXUtils.getHSSFWorkbook(fileName, title, objects, null);
            workbook.write(output);
            output.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }




}
