package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.constants.SysParamConstant;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TCompanyInfoDto;
import com.yihexinda.data.dto.TSysParamDto;
import com.yihexinda.data.dto.TUserDto;
import com.yihexinda.dataservice.service.TCompanyInfoService;
import com.yihexinda.dataservice.service.TSysParamService;
import com.yihexinda.dataservice.service.TUserService;
import com.yihexinda.dataservice.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
* @author zl
* @version 1.0
* @date 2018/12/12
*/
@RestController
@RequestMapping("/sysParam/client")
@Slf4j
public class SysParamApiResource {

    @Autowired
    private TSysParamService tsysParamService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TUserService tUserService;

    @Autowired
    private TCompanyInfoService tCompanyInfoService;

    /**
     *  高峰/平峰价格
     * @return
     */
     @GetMapping("/getTicketPrice")
     public ResultVo getTicketPrice(){
         List<TSysParamDto> sysParamList = tsysParamService.list();
         Map<String,String>  paramMap = new HashMap<>();
         if (sysParamList.size()>0) {
             for (TSysParamDto tSysParamDto : sysParamList) {
                 paramMap.put(tSysParamDto.getParamName(), tSysParamDto.getParamValue());
             }
             //是否具有高峰票价，平峰票价，高峰时间段标识
             if (StringUtil.isEmpty(paramMap.get(SysParamConstant.SYS_OFFPEAK_PRICE)) || StringUtil.isEmpty(SysParamConstant.SYS_PEAK_PRICE) || StringUtil.isEmpty(SysParamConstant.SYS_PEAK_TIME_RANGE)) {
                 return ResultVo.error(ResultConstant.HTTP_STATUS_INTERNAL_SERVER_ERROR, ResultConstant.HTTP_STATUS_INTERNAL_SERVER_ERROR_VALUE);
             } else {
                 redisUtil.set(RedisConstant.SYS_OFFPEAK_PRICE, paramMap.get(SysParamConstant.SYS_OFFPEAK_PRICE), DateUtils.getDayEnd().getTime());
                 redisUtil.set(RedisConstant.SYS_PEAK_PRICE, paramMap.get(SysParamConstant.SYS_PEAK_PRICE), DateUtils.getDayEnd().getTime());
                 redisUtil.set(RedisConstant.SYS_PEAK_TIME_RANGE, paramMap.get(SysParamConstant.SYS_PEAK_TIME_RANGE), DateUtils.getDayEnd().getTime());
             }
         }else {
             return ResultVo.error(ResultConstant.HTTP_STATUS_INTERNAL_SERVER_ERROR, ResultConstant.HTTP_STATUS_INTERNAL_SERVER_ERROR_VALUE);
         }
         //高峰时间段
         String timeRange = paramMap.get(SysParamConstant.SYS_PEAK_TIME_RANGE);
         //高峰票价
         String peakPrice = paramMap.get(SysParamConstant.SYS_PEAK_PRICE);
         //平峰票价
         String offPeakPrice = paramMap.get(SysParamConstant.SYS_OFFPEAK_PRICE);
         try {
             SimpleDateFormat date = new SimpleDateFormat("HH:mm");
             String currentTimeRange = date.format(new Date());
             String[] time = timeRange.split("-");
             //当前时间
             Date currentTime = date.parse(currentTimeRange);
             //7:30高峰开始时间
             Date stratTime = date.parse(time[0]);
             //9:00高峰结束时间
             Date endTime = date.parse(time[1]);
             if(currentTime.after(stratTime) && currentTime.before(endTime)){
                 return ResultVo.success().setMessage("高峰价格").setDataSet(peakPrice);
             }else {
                 return ResultVo.success().setMessage("平峰价格").setDataSet(offPeakPrice);
             }
         } catch (ParseException e) {
             e.printStackTrace();
         }
         return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
     }


    /**
     * 修改系统配置
     * @param tSysParamDto 配置信息
     * @return ResultVo
     */
    @PostMapping("/updateAppointmentNumber")
    public ResultVo updateAppointmentNumber(@RequestBody TSysParamDto tSysParamDto){
        tSysParamDto.setUpdateDate(new Date());
        if (tsysParamService.updateById(tSysParamDto)){
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }
    /**
     * 根据key查询系统配置表
     * @param key 配置key
     * @return ResultVo
     */
    @GetMapping("/getAppointmentNumber/{key}")
    public ResultVo getAppointmentNumber(@PathVariable String key){
        if (StringUtil.isEmpty(key)){
            ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"系统key不能为空");
        }
        QueryWrapper<TSysParamDto> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("param_name",key);
        return ResultVo.success().setDataSet(tsysParamService.getOne(queryWrapper));
    }
    /**
     * 内部/外部人员价格
     *
     * @return
     */
    @PostMapping("/getTicketPriceByPerson")
    public ResultVo getTicketPriceByPerson(@RequestBody Map<String,Object> data){
        String userId=StringUtil.trim(data.get("userId"));
        if(StringUtil.isEmpty(userId)){
            ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"用户标识为空");
        }
        //查询用户电话号，根据电话号在员工表内查询是否存在
        TUserDto user=tUserService.getById(userId);
        String phone=user.getPhone();
        TCompanyInfoDto companyInfoDto=tCompanyInfoService.getOne(new QueryWrapper<TCompanyInfoDto>()
                    .select("id","user_name","dept_name","phone")
                    .eq("phone",phone)
        );
        //内部人员票价
        String companyPrice;
        //外部人员票价
        String otherPrice;
        //判断redis缓存是否为空
        if(StringUtil.isEmpty(redisUtil.get(RedisConstant.SYS_COMPANY_PRICE))||StringUtil.isEmpty(redisUtil.get(RedisConstant.SYS_OTHER_PRICE))){
            List<TSysParamDto> sysParamList = tsysParamService.list();
            Map<String,String>  paramMap = new HashMap<>();
            if (sysParamList.size()>0) {
                for (TSysParamDto tSysParamDto : sysParamList) {
                    paramMap.put(tSysParamDto.getParamName(), tSysParamDto.getParamValue());
                }
                //是否具有内部/外部人员价格
                if (StringUtil.isEmpty(paramMap.get(SysParamConstant.SYS_COMPANY_PRICE)) || StringUtil.isEmpty(SysParamConstant.SYS_OTHER_PRICE) ) {
                    return ResultVo.error(ResultConstant.HTTP_STATUS_INTERNAL_SERVER_ERROR, ResultConstant.HTTP_STATUS_INTERNAL_SERVER_ERROR_VALUE);
                } else {
                    redisUtil.set(RedisConstant.SYS_COMPANY_PRICE, paramMap.get(SysParamConstant.SYS_COMPANY_PRICE));
                    redisUtil.set(RedisConstant.SYS_OTHER_PRICE, paramMap.get(SysParamConstant.SYS_OTHER_PRICE));
                }
            }else {
                return ResultVo.error(ResultConstant.HTTP_STATUS_INTERNAL_SERVER_ERROR, ResultConstant.HTTP_STATUS_INTERNAL_SERVER_ERROR_VALUE);
            }
            companyPrice = paramMap.get(SysParamConstant.SYS_COMPANY_PRICE);
            otherPrice = paramMap.get(SysParamConstant.SYS_OTHER_PRICE);
        }else{
            companyPrice = redisUtil.get(RedisConstant.SYS_COMPANY_PRICE);
            otherPrice = redisUtil.get(RedisConstant.SYS_OTHER_PRICE);
        }
        if(companyInfoDto!=null){
            return  ResultVo.success().setMessage("内部员工价格").setDataSet(companyPrice);
            //return  ResultVo.success().setMessage("内部员工价格").setDataSet(0.01);
        }else{
            return  ResultVo.success().setMessage("外部人员价格").setDataSet(otherPrice);
            //return  ResultVo.success().setMessage("外部人员价格").setDataSet(2);
        }

    }

}
