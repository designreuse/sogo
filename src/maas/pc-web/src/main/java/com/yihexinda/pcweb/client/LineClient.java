package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TLineDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 *
 * @author wenbn
 * @version 1.0
 * @date 2018/11/30 0030
 */
@FeignClient(value = "data-service")
@RequestMapping("/line/client")
public interface LineClient {

    /**
     * wenbn
     * 添加线路信息
     * @param line
     * @return
     */
    @RequestMapping(value = "/addLine" ,method = RequestMethod.POST)
    ResultVo addLine(TLineDto line);

    /**
     * wenbn
     * 删除线路信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteLine/{id}" ,method = RequestMethod.POST)
    ResultVo deleteLine(@PathVariable("id") String id);

    /**
     * wenbn
     * 修改线路信息
     * @param line
     * @return
     */
    @RequestMapping(value = "/updateLine" ,method = RequestMethod.POST)
    ResultVo updateLine(TLineDto line);

    /**
     * wenbn
     * 查询线路信息
     * @param
     * @return
     */
    @PostMapping(value = "/lineList")
    ResultVo lineList(Map<String,Object> condition);

}
