package com.yihexinda.pcweb.api;


import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.ExcelXUtils;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TSysUserDto;
import com.yihexinda.data.dto.TUserFeedDto;
import com.yihexinda.pcweb.client.OrderClient;
import com.yihexinda.pcweb.client.SysUserClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author pengfeng
 * @date 2018/12/08
 */
@Api(description = "订单接口")
@RestController()
@RequestMapping("api/pc")
@Slf4j
public class OrderResource {

    @Autowired
    private OrderClient orderClient;

    /**
     * 查询订单信息列表
     * @return 订单信息列表
     */
    @ApiOperation(value = "查询订单信息列表", httpMethod = "POST")
    @RequestMapping(value = "/getOrderList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getOrderList(@RequestBody Map<String,Object> condition) {
        return orderClient.getOrderList(condition);
    }

    /**
     * 首页查询订单数
     * @return 订单数
     */
    @ApiOperation(value = "首页查询订单数", httpMethod = "POST")
    @RequestMapping(value = "/getPcIndex")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getPcIndex(@RequestBody Map<String,Object> data) {
        return orderClient.getPcIndex();
    }


    /**
     *
     * 订单列表导出Excel
     */
    @ApiOperation(value = "订单列表导出Excel", httpMethod = "GET")
    @RequestMapping(value = "/getOrderExcel")
    @NoRequireLogin
    public void getOrderExcel(HttpServletResponse response) {
        try {
            String fileName = "订单.xls";
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("Content-Type:application/vnd.ms-excel");
            String[] title = {"订单号", "用户昵称", "司机名称","车牌号", "行程类型","用车状态","订单状态","支付状态","购票类型","上车人数","上车站点","下车站点","支付金额","退款金额","站点间距","开始时间","结束时间"};
            List<Map> maps=orderClient.getOrderExcel();
            String[][] objects = new String[maps.size()][];
            for (int i = 0; i < maps.size(); i++) {
                Map map = maps.get(i);
                objects[i][0] = StringUtil.trim(map.get("order_no"));
                objects[i][1] = StringUtil.trim(map.get("nick"));
                objects[i][2] = StringUtil.trim(map.get("driver_name"));
                objects[i][3] = StringUtil.trim(map.get("car_no"));
                objects[i][4] = StringUtil.trim(map.get("trip_type"));
                if ("0".equals(StringUtil.trim(map.get("order_type")))){
                    objects[i][5] = "即时";
                }
                if ("1".equals(StringUtil.trim(map.get("order_type")))){
                    objects[i][5] = "预约";
                }
                if ("0".equals(StringUtil.trim(map.get("order_status")))){
                    objects[i][6] = "创建订单";
                }
                if ("1".equals(StringUtil.trim(map.get("order_status")))){
                    objects[i][6] = "待使用";
                }
                if ("2".equals(StringUtil.trim(map.get("order_status")))){
                    objects[i][6] = "进行中";
                }
                if ("3".equals(StringUtil.trim(map.get("order_status")))){
                    objects[i][6] = "已完成";
                }
                if ("4".equals(StringUtil.trim(map.get("order_status")))){
                    objects[i][6] = "已失效";
                }
                if ("1".equals(StringUtil.trim(map.get("is_pay")))&&"0".equals(StringUtil.trim(map.get("0")))){
                    objects[i][7] = "支付成功";
                }
                if ("0".equals(StringUtil.trim(map.get("refund_status")))){
                    objects[i][7] = "退款中";
                }
                if ("1".equals(StringUtil.trim(map.get("refund_status")))){
                    objects[i][7] = "退款成功";
                }
                if ("0".equals(StringUtil.trim(map.get("ticket_type")))){
                    objects[i][8] = "多票";
                }
                if ("1".equals(StringUtil.trim(map.get("ticket_type")))){
                    objects[i][8] = "单票";
                }
                objects[i][9] = StringUtil.trim(map.get("passengers_num"));
                objects[i][10] = StringUtil.trim(map.get("start_station"));
                objects[i][11] = StringUtil.trim(map.get("end_station"));
                if ("1".equals(StringUtil.trim(map.get("is_pay")))){
                    objects[i][12] = StringUtil.trim(map.get("order_amount"));
                }else {
                    objects[i][12] ="";
                }
                if ("1".equals(StringUtil.trim(map.get("is_refund")))){
                    objects[i][13] = StringUtil.trim(map.get("refund_amount"));
                }else {
                    objects[i][13] ="";
                }
                objects[i][14] = StringUtil.trim(map.get("site_dis"));
                if ("".equals(StringUtil.trim(map.get("ride_start_date")))){
                    objects[i][15] ="";
                }else{
                    objects[i][15] =  DateUtils.formatDate(new Date(Long.valueOf(StringUtil.trim(map.get("ride_start_date")))),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);;
                }
                if ("".equals(StringUtil.trim(map.get("ride_end_date")))){
                    objects[i][16] ="";
                }else{
                    objects[i][16] =  DateUtils.formatDate(new Date(Long.valueOf(StringUtil.trim(map.get("ride_end_date")))),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);;
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
     * 查询订单列表
     * @return 用户订单列表
     */
    @ApiOperation(value = "查询用户订单列表", httpMethod = "POST")
    @RequestMapping(value = "/getOrderUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "string"),
    })
    public ResultVo getOrderUser(@RequestBody Map<String,Object> condition) {
        return orderClient.getOrderUser(condition);
    }




}
