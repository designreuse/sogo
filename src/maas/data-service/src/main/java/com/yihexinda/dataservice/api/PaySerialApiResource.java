package com.yihexinda.dataservice.api;

import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TPaySerialDto;
import com.yihexinda.dataservice.service.TPaySerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zl
 * @version 1.0
 * @date 2018/11/28 0028
 */
@RestController
@RequestMapping("/paySerial/client")
public class PaySerialApiResource {

    @Autowired
    private TPaySerialService paySerialService;

    /**
     * 支付统计 列表
     * @param data  分页参数
     * @return 统计列表
     */
    @PostMapping("/getPaySerialList")
    public ResultVo getPaySerialList(@RequestBody Map<String, Object> data){
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(data.get("pageIndex")),1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(data.get("pageSize")),10);
        if (!"".equals(StringUtil.trim(data.get("startTime")))&&!"".equals(data.get("endTime"))){
            Timestamp aStartTime = Timestamp.valueOf(StringUtil.trim(data.get("startTime")));
            String endTime = StringUtil.trim(data.get("endTime")).replace("00:00:00","23:59:59");
            Timestamp aEndTime = Timestamp.valueOf(endTime);
            data.put("aStartTime", aStartTime);
            data.put("aEndTime", aEndTime);
        }

            String payStatus = StringUtil.trim(data.get("payStatus"));
            ResultVo resultVo = new AbstractPageTemplate<TPaySerialDto>() {
            @Override
            protected List<TPaySerialDto> executeSql() {
                List<TPaySerialDto> list = paySerialService.getPaySerialList(data);
                return list;
            }
        }.preparePageTemplate(pageIndex,pageSize);
        return resultVo;
    }

    /**
     * 支付统计 列表
     * @return 统计列表
     */
    @GetMapping("/paySerialExcel")
    public List<TPaySerialDto> paySerialExcel(){
             return  paySerialService.getPaySerialList(null);
    }

}
