package com.yihexinda.userweb.api;


import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TUserDto;
import com.yihexinda.data.dto.TUserFeedDto;
import com.yihexinda.userweb.client.UserFeedClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/12/1
 */
@Api(description = "反馈接口")
@RestController()
@RequestMapping("api/userFeed")
@Slf4j
public class UserFeedResource {

    @Autowired
    private UserFeedClient userFeedClient;

    /**
     * 新增用户反馈信息
     * @param condition 反馈信息
     * @return ResultVo
     */
    @ApiOperation(value = "新增用户反馈信息", httpMethod = "POST")
    @RequestMapping(value = "/addUserFeed")
    public ResultVo addUserFeed(@RequestBody Map<String,Object> condition) {
        String token = StringUtil.trim(condition.get("token"));
        String feedContent = StringUtil.trim(condition.get("feedContent"));
        if ("".equals(token)||"".equals(feedContent)){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR,"参数异常，请检查参数");
        }
        PayLoadVo payLoadVo = RequestUtil.analysisToken(token);
        if (null==payLoadVo){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR,"token解析错误");
        }
        String userId = payLoadVo.getUserId();
        TUserFeedDto tUserFeedDto =new TUserFeedDto();
        tUserFeedDto.setUserId(userId);
        tUserFeedDto.setFeedContent(feedContent);
        tUserFeedDto.setUserType("0");
        return userFeedClient.addUserFeed(tUserFeedDto);
    }

}
