package com.yihexinda.userweb.api;

import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.HttpClinetPostUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TSmsLogDto;
import com.yihexinda.userweb.client.SmsLogClient;
import com.yihexinda.userweb.config.SmsConstant;
import com.yihexinda.userweb.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(description = "短信接口")
@RestController()
@RequestMapping("/api/smsLog")
@Slf4j
public class SmsLogResource {

    @Autowired
    private SmsLogClient smsLogClient;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 发送短信
     * @param smsLogDto
     * @return
     */
    @RequestMapping(value = "/addSmsLog")
    @ApiOperation(value = "发送短信", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "smsMobile", value = "手机号码", required = true, paramType = "string"),
    })
    @NoRequireLogin
    public ResultVo addSmsLog(@RequestBody TSmsLogDto smsLogDto){
        log.info("调用发送短信接口");
        //随机产生6位验证码:
        String code = RandomStringUtils.randomNumeric(6);
        smsLogDto.setSmsCode(code);
        smsLogDto.setSmsType("0");//短信类型( 0验证码  1通知 2营销 )

        //短信接口参数  模板id/手机号/验证码
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("templateId", SmsConstant.SMS_TEMPLATE_ID));
        formParams.add(new BasicNameValuePair("mobile", smsLogDto.getSmsMobile()));
        formParams.add(new BasicNameValuePair("param", code +",1"));

        //发送短信、校验短信是否发送成功 -1失败  0发送成功
        int status = HttpClinetPostUtil.post(SmsConstant.SMS_URL,formParams);
        log.info("短信接口url:"+SmsConstant.SMS_URL+"相关参数:"+formParams+"返回状态码:"+status);
        if(status == -1) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
        }
        // 将验证码、手机号存入redis中 有效期1分钟
        redisUtil.set(SmsConstant.SMS_LOGIN_USER + smsLogDto.getSmsMobile(),smsLogDto.getSmsMobile() + "-" + smsLogDto.getSmsCode(),(long)60*15);
        return smsLogClient.addSmsLog(smsLogDto);
    }



}
