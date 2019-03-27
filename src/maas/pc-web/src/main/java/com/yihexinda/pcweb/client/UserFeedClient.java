package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TSysUserDto;
import com.yihexinda.data.dto.TUserFeedDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/12/10
 */
@FeignClient(value = "data-service")
@RequestMapping("/userFeed/client")
public interface UserFeedClient {

    /**
     * pengFeng
     * 查询用户反馈列表
     * @param  condition 参数map
     * @return 反馈信息列表
     */
    @RequestMapping(method = RequestMethod.POST, value = "/userFeedList")
    ResultVo userFeedList(@RequestBody Map<String, Object> condition);

    /**
     *  查询反馈列表list
     * @return 查询列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/userFeedExcel")
    List<TUserFeedDto> userFeedExcel();

}
