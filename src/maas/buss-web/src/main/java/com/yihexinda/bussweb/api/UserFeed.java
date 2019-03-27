package com.yihexinda.bussweb.api;

import com.yihexinda.bussweb.client.CarClient;
import com.yihexinda.bussweb.client.UserFeedClient;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TCarDto;
import com.yihexinda.data.dto.TUserFeedDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author yhs
 * @version 1.0
 * @date 2018/12/1
 */
@Api(description = "司机反馈接口")
@RestController
@RequestMapping("/api/buss")
@Slf4j
public class UserFeed {

    @Autowired
    private UserFeedClient userFeedClient;


    /**
     * yhs
     * 提交司机用户反馈
     * @param condition
     *
     * @return
     */
    @ApiOperation(value = "提交司机用户反馈", httpMethod = "POST")
    @RequestMapping(value = "/addDriverFeed",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(value ="用户登录的标识",name = "token",required = true,dataType = "String"),
            @ApiImplicitParam(value ="反馈的内容",name = "feedContent",required = true,dataType = "String")
    })
    public ResultVo addDriverFeed(@RequestBody Map<String,Object> condition){
        String token = StringUtil.trim(condition.get("token"));
        String feedContent = StringUtil.trim(condition.get("feedContent"));
        if ("".equals(token)||"".equals(feedContent)){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR,"参数异常，请检查参数");
        }
        PayLoadVo payLoadVo =RequestUtil.analysisToken(token);
        if (null==payLoadVo){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR,"token解析错误");
        }
        String userId = payLoadVo.getUserId();
        condition.put("userId",userId);
        return userFeedClient.addDriverFeed(condition);
    }
}
