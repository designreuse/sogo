package com.yihexinda.pcweb.api;

import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.ExcelXUtils;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TPaySerialDto;
import com.yihexinda.data.dto.TTravelEvaluateDto;
import com.yihexinda.pcweb.client.PaySerialClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/12/19 0028
 */
@Api(description = "支付统计接口")
@RestController()
@RequestMapping("/api/pc")
public class PaySerialResource {

    @Autowired
    private PaySerialClient paySerialClient;

    /**
     * 支付统计列表
     * @return 返回统计列表
     */
    @ApiOperation(value = "支付统计", httpMethod = "POST")
    @RequestMapping("/getPaySerialList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageIndex", value = "", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "", required = true, paramType = "string")
    })
    public ResultVo getPaySerialList(@RequestBody Map<String, Object> data){
        return paySerialClient.getPaySerialList(data);
    }

    /**
     *  导出支付统计列表Excel
     * @param response HttpServletResponse
     */
    @ApiOperation(value = "查询支付统计列表", httpMethod = "GET")
    @RequestMapping(value = "/paySerialExcel")
    @NoRequireLogin
    public void paySerialExcel(HttpServletResponse response) {
        try {
            String fileName = "支付统计.xls";
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("Content-Type:application/vnd.ms-excel");
            String[] title = {"流水号", "订单编号", "用户名称", "购票类型","支付金额","支付状态","退款金额","支付时间","退款时间"};
            List<TPaySerialDto> paySerialDtoList= paySerialClient.paySerialExcel();
            String[][] objects = new String[paySerialDtoList.size()]
                    [paySerialDtoList.get(0).toString().split(",").length];
            for (int i = 0; i < paySerialDtoList.size(); i++) {
                TPaySerialDto tPaySerialDto = paySerialDtoList.get(i);
                if (StringUtil.isNotEmpty(tPaySerialDto.getRefundNo())){
                    objects[i][0] = tPaySerialDto.getRefundNo();
                }else {
                    objects[i][0] = tPaySerialDto.getTradeNo();
                }

                objects[i][1] = tPaySerialDto.getOrderNo();
                objects[i][2] = tPaySerialDto.getNick();
                if ("0".equals(tPaySerialDto.getTicketType())){
                    objects[i][3] = "单票";
                }else {
                    objects[i][3] = "多票";
                }
                objects[i][4] = String.valueOf(tPaySerialDto.getPayAmount());
                if ("2".equals(tPaySerialDto.getPayStatus())){
                    objects[i][5] = "退款成功";
                }
                if ("1".equals(tPaySerialDto.getPayStatus())){
                    objects[i][5] = "退款中";
                }
                if ("0".equals(tPaySerialDto.getPayStatus())){
                    objects[i][5] = "支付成功";
                }
                if (!"0".equals(tPaySerialDto.getPayStatus())){
                    objects[i][6] = String.valueOf(tPaySerialDto.getRefundAmount());
                }else {
                    objects[i][6] = "";
                }
                objects[i][7] = DateUtils.formatDate(tPaySerialDto.getPayTime(),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
                if (!"0".equals(tPaySerialDto.getPayStatus())){
                    objects[i][8] = DateUtils.formatDate(tPaySerialDto.getRefundTime(),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
                }else {
                    objects[i][8] = "";
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
