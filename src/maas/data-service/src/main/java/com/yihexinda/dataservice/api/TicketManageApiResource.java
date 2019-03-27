package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.Condition;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.Constants;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.constants.SysParamConstant;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TLineDto;
import com.yihexinda.data.dto.TOrderDto;
import com.yihexinda.data.dto.TSysParamDto;
import com.yihexinda.dataservice.service.TOrderService;
import com.yihexinda.dataservice.service.TStationService;
import com.yihexinda.dataservice.service.TSysParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 购票管理
 * @author wenbn
 * @version 1.0
 * @date 2018/12/3 0003
 */
@RestController
@RequestMapping("/ticket/client")
@Slf4j
public class TicketManageApiResource {



    @Autowired
    private TOrderService tOrderService;

    /**
     *  系统配置服务
     */
    @Resource
    private TSysParamService tSysParamService;

    /**
     * 检查购票
     * @param condition
     * @return
     */
    @PostMapping(value = "/checkAppointment")
    ResultVo checkAppointment(@RequestBody Map<String, Object> condition) throws ParseException {
        String appoTime = StringUtil.trim(condition.get("appoTime"));
        //实时出行
        if(StringUtil.isEmpty(appoTime)){
            return ResultVo.success();
        }
        Date date = new Date();
        Date appoTimeDate = DateUtils.parseDate(appoTime, Constants.DATE_FULL_TIME_PATTERN);
        if(appoTimeDate.getTime()<=DateUtils.addMinutes(date,10).getTime()){
            return ResultVo.error(ResultConstant.SYS_APPOINTMENT_FAILURE,"预约失败，请提前10分钟预约");
        }
        //统计预约出行相同的待完成或进行中的订单
        QueryWrapper<TOrderDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(passengers_num) count");
        queryWrapper.eq("trip_time", appoTimeDate);
        queryWrapper.in("order_status", "1","2");
//        queryWrapper.apply("to_char(create_date,'yyyy-MM-dd')={0}", DateUtils.formatDate(date, Constants.DATE_PATTERN));
        //
        Map<String,Object> respMap = tOrderService.getMap(queryWrapper);
        int count = respMap == null ? 0:StringUtil.getAsInt(StringUtil.trim(respMap.get("count")),0);
        //int total = 20 ;
        QueryWrapper<TSysParamDto> queryParamWrapper = new QueryWrapper<>();
        queryParamWrapper.eq("param_name", SysParamConstant.APPOINTMENT_NUMBER);
        TSysParamDto tSysParamDto = tSysParamService.getOne(queryParamWrapper);
        if (StringUtil.isEmpty(tSysParamDto.getParamValue())){
            return  ResultVo.error(ResultConstant.SYS_APPOINTMENT_FAILURE,"未获取到配置预约人数信息");
        }
        int total = Integer.valueOf(tSysParamDto.getParamValue());
        int ticketNumber = StringUtil.getAsInt(StringUtil.trim(condition.get("ticketNumber")));
        if(count+ticketNumber<total){
            return ResultVo.success();
        }
        //预约失败
        return ResultVo.error(ResultConstant.SYS_APPOINTMENT_FAILURE,"此时段预约人数已满");
    }

    /**
     * 检测是否还有未使用的高峰票
     * @param condition
     * @return
     */
    @PostMapping(value = "/checkExitsNotTravelPeak")
    ResultVo checkExitsNotTravelPeak(@RequestBody Map<String,Object> condition){
        String userId = StringUtil.trim(condition.get("userId"));
        int count = tOrderService.count(new QueryWrapper<TOrderDto>()
                .eq("user_id", userId)
                .eq("route_type", StringUtil.trim(condition.get("routeType")))
                .eq("order_status", "1")
                .apply("to_char(create_date,'yyyy-MM-dd')={0}",DateUtils.formatDate(new Date(), Constants.DATE_PATTERN))
        );
        if(count<1){
            return ResultVo.success();
        }else{
            return ResultVo.error(ResultVo.Status.SYS_PEAK_PURCHASE_LIMITATION_ERROR);
        }
    }
}
