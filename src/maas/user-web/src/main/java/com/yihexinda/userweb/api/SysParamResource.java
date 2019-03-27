package com.yihexinda.userweb.api;

import com.yihexinda.core.constants.RedisConstant;

import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;

import com.yihexinda.userweb.client.SysParamClient;
import com.yihexinda.userweb.client.UserClient;
import com.yihexinda.userweb.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * @author zl
 * @since 2018-12-12
 */
@Api(description = "票价接口")
@RestController()
@RequestMapping("/api/sysParam")
public class SysParamResource {

    @Autowired
    private SysParamClient sysParamClient;

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 平峰/高峰价格
     *
     * @return
     */
    @ApiOperation(value = "平峰/高峰价格", httpMethod = "GET")
    @RequestMapping("/getTicketPrice")
    public ResultVo getTicketPrice() {

        try {
            String peakTimeRange = redisUtil.get(RedisConstant.SYS_PEAK_TIME_RANGE);
            if (StringUtil.isEmpty(peakTimeRange)) {
                return sysParamClient.getTicketPrice();
            }

            SimpleDateFormat date = new SimpleDateFormat("HH:mm");
            String currentTimeRange = date.format(new Date());
            String[] time = peakTimeRange.split("-");

            //当前时间
            Date currentTime = date.parse(currentTimeRange);
            //7:30高峰开始时间
            Date stratTime = date.parse(time[0]);
            //9:00高峰结束时间
            Date endTime = date.parse(time[1]);
            //高峰票价
            String peakPrice = redisUtil.get(RedisConstant.SYS_PEAK_PRICE);
            //平峰价格
            String offPeakPrice = redisUtil.get(RedisConstant.SYS_OFFPEAK_PRICE);

            if (currentTime.after(stratTime) && currentTime.before(endTime) && !StringUtil.isEmpty(peakPrice)) {
                return ResultVo.success().setMessage("高峰价格").setDataSet(peakPrice);
            }
            if (!StringUtil.isEmpty(offPeakPrice)) {
                return ResultVo.success().setMessage("平峰价格").setDataSet(offPeakPrice);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sysParamClient.getTicketPrice();
    }

    /**
     * 内部/外部人员价格
     *
     * @return
     */
    @ApiOperation(value = "内部/外部人员价格", httpMethod = "POST")
    @RequestMapping("/getTicketPriceByPerson")
//    @ApiImplicitParams({
//                @ApiImplicitParam(name = "token", value = "token" , required =true,dataType = "String")
//    })
    public ResultVo getTicketPriceByPerson( @RequestBody Map<String,Object> data) {
        //获取token
        String token = StringUtil.trim(data.get("token"));
        //解析token数据
        String userId = RequestUtil.analysisToken(token).getUserId();
        data.put("userId",userId);
        return sysParamClient.getTicketPriceByPerson(data);
    }





}
