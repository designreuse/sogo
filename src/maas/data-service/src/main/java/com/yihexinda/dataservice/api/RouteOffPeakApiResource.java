package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TRouteOffpeakDto;
import com.yihexinda.data.dto.TRoutePeakDto;
import com.yihexinda.dataservice.service.TRouteOffpeakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 平峰行程信息管理类
 * @author wenbn
 * @version 1.0
 * @date 2018/12/1 0001
 */
@RestController
@RequestMapping("/routeOffpeak/client")
@Slf4j
public class RouteOffPeakApiResource {


    @Autowired
    private TRouteOffpeakService tRouteOffpeakService;

    /**
     * wenbn
     * Buss查询平峰行程
     * @param
     * @return
     */
    @PostMapping(value = "/buss/routeOffpeakList")
    public ResultVo bussRoutePeakList(@RequestBody Map<String,Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")),1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")),10);
        QueryWrapper<TRouteOffpeakDto> queryWrapper = new QueryWrapper<>();
        ResultVo resultVo = new AbstractPageTemplate<TRouteOffpeakDto>() {
            @Override
            protected List<TRouteOffpeakDto> executeSql() {
                List<TRouteOffpeakDto> list = tRouteOffpeakService.list(queryWrapper);
                return list;
            }
        }.preparePageTemplate(pageIndex,pageSize);
        return resultVo;
    }
}
