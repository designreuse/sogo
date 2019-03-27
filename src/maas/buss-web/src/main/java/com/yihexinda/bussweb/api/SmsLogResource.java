package com.yihexinda.bussweb.api;

import com.yihexinda.bussweb.client.DriverClient;
import com.yihexinda.bussweb.client.SmsLogClient;
import com.yihexinda.bussweb.config.SmsConstant;
import com.yihexinda.bussweb.utils.RedisUtil;
import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.HttpClinetPostUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TSmsLogDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(description = "短信接口")
@RestController()
@RequestMapping("/api/buss")
public class SmsLogResource {

    @Autowired
    private SmsLogClient smsLogClient;

    @Autowired
    private DriverClient diverClient;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 发送短信
     *
     * @param smsLogDto
     * @return
     */
    @RequestMapping(value = "/addSmsLog")
    @ApiOperation(value = "发送短信", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "smsMobile", value = "手机号码", required = true, paramType = "string"),
    })
    @NoRequireLogin
    public ResultVo addSmsLog(@RequestBody TSmsLogDto smsLogDto) {
        //校验手机号是否存在
        String phone = StringUtil.trim(smsLogDto.getSmsMobile());
        if(StringUtil.isEmpty(phone)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        ResultVo resultVo = diverClient.checkExistsPhone(phone);
        //判断手机号是不是存在
        if(resultVo.getResult() != ResultConstant.SYS_REQUIRED_SUCCESS){
            return resultVo;
        }
        //存在则发短信

        //随机产生6位验证码:
        String code = RandomStringUtils.randomNumeric(6);
        smsLogDto.setSmsCode(code);
        //短信类型( 0验证码  1通知 2营销 )
        smsLogDto.setSmsType("0");
        //短信接口参数  模板id/手机号/验证码
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("templateId", SmsConstant.SMS_TEMPLATE_ID));
        formParams.add(new BasicNameValuePair("mobile", smsLogDto.getSmsMobile()));
        formParams.add(new BasicNameValuePair("param", code + ",1"));
        if (formParams.size()!=3){
            return ResultVo.error(999, "短信参数缺少");
        }
        //发送短信、校验短信是否发送成功 -1失败  0发送成功
        int  status = HttpClinetPostUtil.post(SmsConstant.SMS_URL,formParams);
        if(status != 0) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, "短信接口发送异常，回执码为："+status);
        }
        // 将验证码、手机号存入redis中 有效期1分钟
        redisUtil.set(SmsConstant.SMS_LOGIN_USER + smsLogDto.getSmsMobile(), smsLogDto.getSmsMobile() + "-" + smsLogDto.getSmsCode(), 60L);
        return smsLogClient.addSmsLog(smsLogDto);

    }


}
