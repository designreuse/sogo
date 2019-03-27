package com.yihexinda.platformweb.api;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.core.dto.Json;
import com.yihexinda.core.dto.Page;
import com.yihexinda.core.utils.ExcelXUtils;
import com.yihexinda.data.dto.OrderDto;
import com.yihexinda.data.dto.OrderRecordAllPageDto;
import com.yihexinda.data.dto.OrderRecordCountWayDto;
import com.yihexinda.data.dto.OrderRecordQueryDto;
import com.yihexinda.platformweb.client.DataOrderRecordClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by wxf on 2018-11-13.
 */
@Api(description = "订单记录接口")
@RestController
@RequestMapping("/api/orderRecord")
public class OrderRecordResource {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OrderRecordResource.class);
    @Autowired
    private DataOrderRecordClient orderRecordClient;
    /**
     * 分页查询订单列表
     *
     * @param orderRecordQueryDto
     * @return
     */
    @ApiOperation(value = "抢单/派单/退回/抽回/暂存统计列表", httpMethod = "POST")
    @PostMapping("/findGrabbedByCondition")
    @ResponseBody
    public Json<Page<OrderDto>> findGrabbedByCondition(@RequestBody OrderRecordQueryDto dto) {
        Json json = new Json();
        try {
            if (dto.getStartOrderIn()!=null&&dto.getEndOrderIn()!=null){
                //时间转换
                getDate(dto);
            }
            json.setSuccess(true);
            json.setObj(orderRecordClient.findGrabbedByCondition(dto));
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg(e.getMessage());
            log.error("list {}", e);
        }
        return json;
    }



    @ApiOperation(value = "抢单/派单/退回/抽回/暂存统计列表导出",httpMethod = "GET")
    @GetMapping("/findGrabbedByConditionExport")
    public void findGrabbedByConditionExport(HttpServletRequest request,HttpServletResponse response,@RequestBody OrderRecordQueryDto dto)throws Exception{
        String[] title = {};
        String fileName = null;
        if (dto.getStartOrderIn()!=null&&dto.getEndOrderIn()!=null){
            //时间转换
            getDate(dto);
        }
        //获取数据
        List<OrderRecordAllPageDto>  list = (List<OrderRecordAllPageDto>) orderRecordClient.findGrabbedByCondition(dto).getRows();
        //如果是抢单/派单/超时
        if (dto.getFindingType().getCode()==1||dto.getFindingType().getCode()==2||dto.getFindingType().getCode()==5){
            //excel标题
            title= new String[]{"订单属性", "外部订单号", "订单来源", "订单编号", "订单类型", "订单类别", "进单时间", "来源方式", "抢单时间", "处理倒计时", "锁单时间", "处理时长", "处理状态", "出票时间", "出票员"};
            //excel文件名
            fileName = "抢派量统计"+".xls";
        }else if (dto.getFindingType().getCode()==3){
            //excel标题
            title= new String[]{"订单属性", "外部订单号", "订单来源", "订单编号", "订单类型", "订单类别", "进单时间", "来源方式", "抢单时间", "处理倒计时", "锁单时间", "处理时长", "处理状态", "退回时间", "退单员"};
            //excel文件名
            fileName = "退回量统计"+".xls";
        }else if (dto.getFindingType().getCode()==4){
            //excel标题
            title= new String[]{"订单属性", "外部订单号", "订单来源", "订单编号", "订单类型", "订单类别", "进单时间", "来源方式", "抢单时间", "处理倒计时", "锁单时间", "处理时长", "处理状态", "抽回时间", "抽回员"};
            //excel文件名
            fileName = "抽回量统计"+".xls";
        }
        String[][] content = new String[list.size()][];

        for (int i = 0; i <list.size() ; i++) {
            content[i] = new String[title.length];
            OrderRecordAllPageDto obj=list.get(i);
            content[i][0] = obj.getOrderProperty();
            content[i][1] = obj.getExternalOrderNo();
            content[i][2] = obj.getOrderSource();
            content[i][3] = obj.getOrderNo();
            content[i][4] = obj.getOrderType();
            content[i][5] = obj.getOrderSort();
            content[i][6] = obj.getOrderInTime();
            content[i][7] = obj.getFromAction();
            content[i][8] = obj.getCreatedAt();
            content[i][9] = obj.getDisposeCountdown();
            content[i][10] = obj.getUpdatedAt();
            content[i][11] = obj.getProcessDuration();
            content[i][12] = obj.getLastOperateStatus();
            content[i][13] = obj.getConfirmTime();
            content[i][14] = obj.getUsername();
        }

        String sheetName = "统计页";


        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelXUtils.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ApiOperation(value = "订单类别/订单来源/来源方式/出票员/订单类型统计列表", httpMethod = "POST")
    @PostMapping("/excelExportCountWayByCondition")
    @ResponseBody
    public Json<OrderRecordQueryDto> count(@RequestBody OrderRecordQueryDto dto){
        Json json=new Json();
        try {
            if (dto.getStartOrderIn()!=null&&dto.getEndOrderIn()!=null){
                //时间转换
                getDate(dto);
            }
            json.setSuccess(true);
            List<OrderRecordCountWayDto>  list = orderRecordClient.excelExportCountWayByCondition(dto);
            json.setObj(list);
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg(e.getMessage());
            log.error("list {}", e);
        }
        return json;
    }



    @ApiOperation(value = "订单类别/订单来源/来源方式/出票员/订单类型统计导出",httpMethod = "GET")
    @GetMapping("/countExport")
    public void countExport(HttpServletRequest request,HttpServletResponse response,@RequestBody OrderRecordQueryDto dto)throws Exception{
        if (dto.getStartOrderIn()!=null&&dto.getEndOrderIn()!=null){
            //时间转换
            getDate(dto);
        }
        //获取数据
        List<OrderRecordCountWayDto>  list = orderRecordClient.excelExportCountWayByCondition(dto);
        //excel标题
        String[] title={"订单类别", "总订单量", "已完成量", "未完成量", "抢单量", "派单量", "暂存量", "超时量", "退回量", "抽回量"};
        //excel文件名
        String fileName = dto.getCountWay().name()+"统计"+".xls";

        String[][] content = new String[list.size()][];

        for (int i = 0; i <list.size() ; i++) {
            content[i] = new String[title.length];
            OrderRecordCountWayDto obj = list.get(i);
            content[i][0] = String.valueOf(obj.getCountWay());
            content[i][1] = String.valueOf(obj.getTotal());
            content[i][2] = String.valueOf(obj.getAccomplish());
            content[i][3] = String.valueOf(obj.getNotAccomplish());
            content[i][4] = String.valueOf(obj.getRob());
            content[i][5] = String.valueOf(obj.getDesignate());
            content[i][6] = String.valueOf(obj.getTempDeposit());
            content[i][7] = String.valueOf(obj.getOverTime());
            content[i][8] = String.valueOf(obj.getSendBack());
            content[i][9] = String.valueOf(obj.getWithdraw());
        }

        String sheetName = "统计页";


        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelXUtils.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ApiOperation(value = "查询总订单量分页列表", httpMethod = "POST")
    @PostMapping("/selectOrdReAllPage")
    @ResponseBody
    public Json<Page<OrderRecordAllPageDto>> selectOrdReAllPage(@RequestBody OrderRecordQueryDto dto){
        Json json=new Json();
        try {
            if (dto.getStartOrderIn()!=null&&dto.getEndOrderIn()!=null){
                //时间转换
                getDate(dto);
            }
            json.setSuccess(true);
            json.setObj(orderRecordClient.selectOrdReAllPage(dto));
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg(e.getMessage());
            log.error("list {}", e);
        }
        return json;
    }


    @ApiOperation(value = "查询总订单导出",httpMethod = "GET")
    @GetMapping("/selectOrdReAllPageExport")
    public void selectOrdReAllPageExport(HttpServletRequest request,HttpServletResponse response,@RequestBody OrderRecordQueryDto dto)throws Exception{
        if (dto.getStartOrderIn()!=null&&dto.getEndOrderIn()!=null){
            //时间转换
            getDate(dto);
        }
        //获取数据
        Page<OrderRecordAllPageDto> list = orderRecordClient.selectOrdReAllPage(dto);
        List<OrderRecordAllPageDto> data= (List<OrderRecordAllPageDto>) list.getRows();
        //excel标题
        String[] title={"订单属性", "外部订单号", "订单来源", "订单编号", "订单类型", "订单类别", "进单时间", "来源方式", "抢派时间", "处理倒计时","锁单时间","处理时长","处理状态","出票时间","出票员"};
        //excel文件名
        String fileName ="总订单量"+".xls";

        String[][] content = new String[list.getRows().size()][];

        for (int i = 0; i <data.size() ; i++) {
            content[i] = new String[title.length];
            OrderRecordAllPageDto obj=data.get(i);
            content[i][0] = obj.getOrderProperty();
            content[i][1] = obj.getExternalOrderNo();
            content[i][2] = obj.getOrderSource();
            content[i][3] = obj.getOrderNo();
            content[i][4] = obj.getOrderType();
            content[i][5] = obj.getOrderSort();
            content[i][6] = obj.getOrderInTime();
            content[i][7] = obj.getFromAction();
            content[i][8] = obj.getCreatedAt();
            content[i][9] = obj.getDisposeCountdown();
            content[i][10] = obj.getUpdatedAt();
            content[i][11] = obj.getProcessDuration();
            content[i][12] = obj.getLastOperateStatus();
            content[i][13] = obj.getConfirmTime();
            content[i][14] = obj.getUsername();
        }

        String sheetName = "总订单";


        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelXUtils.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ApiOperation(value = "查询已完成订单量分页列表", httpMethod = "POST")
    @PostMapping("/selectCompletePage")
    @ResponseBody
    public Json<Page<OrderRecordAllPageDto>> selectCompletePage(@RequestBody OrderRecordQueryDto dto){
        Json json=new Json();
        try {
            if (dto.getStartOrderIn()!=null&&dto.getEndOrderIn()!=null){
                //时间转换
                getDate(dto);
            }
            json.setSuccess(true);
            json.setObj(orderRecordClient.selectCompletePage(dto));
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg(e.getMessage());
            log.error("list {}", e);
        }
        return json;
    }


    @ApiOperation(value = "查询已完成订单导出",httpMethod = "GET")
    @GetMapping("/selectCompletePageExport")
    public void selectCompletePageExport(HttpServletRequest request,HttpServletResponse response,@RequestBody OrderRecordQueryDto dto)throws Exception{
        if (dto.getStartOrderIn()!=null&&dto.getEndOrderIn()!=null){
            //时间转换
            getDate(dto);
        }
        //获取数据
        Page<OrderRecordAllPageDto> list = orderRecordClient.selectCompletePage(dto);
        List<OrderRecordAllPageDto> data= (List<OrderRecordAllPageDto>) list.getRows();
        //excel标题
        String[] title={"订单属性", "外部订单号", "订单来源", "订单编号", "订单类型", "订单类别", "进单时间", "来源方式", "抢派时间", "处理倒计时","锁单时间","处理时长","处理状态","出票时间","出票员"};
        //excel文件名
        String fileName ="已完成量"+".xls";

        String[][] content = new String[list.getRows().size()][];

        for (int i = 0; i <data.size() ; i++) {
            content[i] = new String[title.length];
            OrderRecordAllPageDto obj=data.get(i);
            content[i][0] = obj.getOrderProperty();
            content[i][1] = obj.getExternalOrderNo();
            content[i][2] = obj.getOrderSource();
            content[i][3] = obj.getOrderNo();
            content[i][4] = obj.getOrderType();
            content[i][5] = obj.getOrderSort();
            content[i][6] = obj.getOrderInTime();
            content[i][7] = obj.getFromAction();
            content[i][8] = obj.getCreatedAt();
            content[i][9] = obj.getDisposeCountdown();
            content[i][10] = obj.getUpdatedAt();
            content[i][11] = obj.getProcessDuration();
            content[i][12] = obj.getLastOperateStatus();
            content[i][13] = obj.getConfirmTime();
            content[i][14] = obj.getUsername();
        }

        String sheetName = "总订单";


        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelXUtils.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @ApiOperation(value = "查询未完成订单分页列表", httpMethod = "POST")
    @PostMapping("/selectUnfinishedOrder")
    @ResponseBody
    public Json<Page<OrderRecordAllPageDto>> selectUnfinishedOrder(@RequestBody OrderRecordQueryDto dto){
        Json json=new Json();
        try {
            if (dto.getStartOrderIn()!=null&&dto.getEndOrderIn()!=null){
                //时间转换
                getDate(dto);
            }
            json.setSuccess(true);
            json.setObj(orderRecordClient.selectUnfinishedOrder(dto));
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg(e.getMessage());
            log.error("list {}", e);
        }
        return json;
    }



    @ApiOperation(value = "查询未完成订单导出",httpMethod = "GET")
    @GetMapping("/selectUnfinishedOrderExport")
    public void selectUnfinishedOrderExport(HttpServletRequest request,HttpServletResponse response,@RequestBody OrderRecordQueryDto dto)throws Exception{
        if (dto.getStartOrderIn()!=null&&dto.getEndOrderIn()!=null){
            //时间转换
            getDate(dto);
        }
        //获取数据
        Page<OrderRecordAllPageDto> list = orderRecordClient.selectUnfinishedOrder(dto);
        List<OrderRecordAllPageDto> data= (List<OrderRecordAllPageDto>) list.getRows();
        //excel标题
        String[] title={"订单属性", "外部订单号", "订单来源", "订单编号", "订单类型", "订单类别", "进单时间", "来源方式", "抢派时间", "处理倒计时","锁单时间","处理时长","处理状态","出票时间","锁单员"};
        //excel文件名
        String fileName ="未完成量"+".xls";

        String[][] content = new String[list.getRows().size()][];

        for (int i = 0; i <data.size() ; i++) {
            content[i] = new String[title.length];
            OrderRecordAllPageDto obj=data.get(i);
            content[i][0] = obj.getOrderProperty();
            content[i][1] = obj.getExternalOrderNo();
            content[i][2] = obj.getOrderSource();
            content[i][3] = obj.getOrderNo();
            content[i][4] = obj.getOrderType();
            content[i][5] = obj.getOrderSort();
            content[i][6] = obj.getOrderInTime();
            content[i][7] = obj.getFromAction();
            content[i][8] = obj.getCreatedAt();
            content[i][9] = obj.getDisposeCountdown();
            content[i][10] = obj.getUpdatedAt();
            content[i][11] = obj.getProcessDuration();
            content[i][12] = obj.getLastOperateStatus();
            content[i][13] = obj.getConfirmTime();
            content[i][14] = obj.getUsername();
        }

        String sheetName = "未完成订单";


        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelXUtils.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ApiOperation(value = "首页调度单统计",httpMethod = "POST")
    @PostMapping("/selectOrderRecordTotalByType")
    public Json<OrderRecordAllPageDto> selectOrderRecordTotalByType(){
        Json json=new Json();
        try {
            json.setSuccess(true);
            json.setObj(orderRecordClient.selectOrderRecordTotalByType(0));
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg(e.getMessage());
            log.error("list {}", e);
        }
        return json;
    }


    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 转时间
     * @param dto
     * @throws Exception
     */
    private void getDate(OrderRecordQueryDto dto) throws Exception{
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dto.setStartDate(sd.parse(dto.getStartOrderIn()));
        dto.setEndDate(sd.parse(dto.getEndOrderIn()));
    }

}
