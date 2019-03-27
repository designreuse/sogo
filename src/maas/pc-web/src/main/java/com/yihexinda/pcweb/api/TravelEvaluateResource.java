package com.yihexinda.pcweb.api;


import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.ExcelXUtils;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TTravelEvaluateDto;
import com.yihexinda.data.dto.TUserFeedDto;
import com.yihexinda.pcweb.client.TravelEvaluateClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2018/12/08
 */
@Api(description = "行程评价接口")
@RestController()
@RequestMapping("api/pc")
@Slf4j
public class TravelEvaluateResource {

    @Autowired
    private TravelEvaluateClient travelEvaluateClient;


    /**
     * 查询用户信息列表
     * @param  data 参数
     * @return 用户信息列表
     */
    @ApiOperation(value = "查询用户信息列表", httpMethod = "POST")
    @RequestMapping(value = "/travelEvaluateList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo travelEvaluateList(@RequestBody Map<String,Object> data) {
        return travelEvaluateClient.travelEvaluateList(data);
    }

    /**
     *  导出评价列表Excel
     * @param response HttpServletResponse
     */
    @ApiOperation(value = "查询评价列表", httpMethod = "GET")
    @RequestMapping(value = "/travelEvaluateExcel")
    @NoRequireLogin
    public void travelEvaluateExcel(HttpServletResponse response) {
        try {
            String fileName = "评价.xls";
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("Content-Type:application/vnd.ms-excel");
            String[] title = {"订单编号", "用户名称", "司机名称", "用车状态", "行程类型","购票类型","星级","评价内容","上车站点","下车站点","站点间距","上车时间","下车时间"};
            List<TTravelEvaluateDto> tTravelEvaluateDtos= travelEvaluateClient.travelEvaluateExcel();
            String[][] objects = new String[tTravelEvaluateDtos.size()]
                    [tTravelEvaluateDtos.get(0).toString().split(",").length];
            for (int i = 0; i < tTravelEvaluateDtos.size(); i++) {
                TTravelEvaluateDto tTravelEvaluateDto = tTravelEvaluateDtos.get(i);
                objects[i][0] = tTravelEvaluateDto.getOrderNo();
                objects[i][1] = tTravelEvaluateDto.getNick();
                objects[i][2] = tTravelEvaluateDto.getName();
                if ("0".equals(tTravelEvaluateDto.getOrderType())){
                    objects[i][3] = "即时";
                }else {
                    objects[i][3] = "预约";
                }
                if ("0".equals(tTravelEvaluateDto.getRouteType())){
                    objects[i][4] = "平峰";
                }else {
                    objects[i][4] = "高峰";
                }
                if ("0".equals(tTravelEvaluateDto.getTicketType())){
                    objects[i][5] = "单票";
                }else {
                    objects[i][5] = "多票";
                }
                objects[i][6] = "司机服务  "+tTravelEvaluateDto.getStartNo()+"星，车内环境"+tTravelEvaluateDto.getCarEnvStartNo()+"星";
                objects[i][7] = tTravelEvaluateDto.getContent();
                objects[i][8] = tTravelEvaluateDto.getStartName();
                objects[i][9] = tTravelEvaluateDto.getEndName();
                objects[i][10] = tTravelEvaluateDto.getSiteDis();
                if (null!=tTravelEvaluateDto.getRideStartDate()){
                    objects[i][11] =  DateUtils.formatDate(tTravelEvaluateDto.getRideStartDate(),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
                }
                if (null!=tTravelEvaluateDto.getRideEndDate()){
                    objects[i][12] =  DateUtils.formatDate(tTravelEvaluateDto.getRideEndDate(),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
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
