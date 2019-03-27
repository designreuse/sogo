package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TRoutePeakTimeRangeDto;
import com.yihexinda.data.dto.TStationDto;
import com.yihexinda.dataservice.service.TRoutePeakTimeRangeService;
import com.yihexinda.dataservice.service.TStationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 *
 * @author wenbn
 * @version 1.0
 * @date 2018/12/3 0003
 */
@RestController
@RequestMapping("/routePeakTimeRange/client")
@Slf4j
public class TRoutePeakTimeRangeApiResource {


    @Autowired
    private TRoutePeakTimeRangeService tRoutePeakTimeRangeService;

    /**
     * 加载站点信息
     * @return
     */
    @PostMapping(value = "/routePeakTimeRangeList")
    ResultVo routePeakTimeRangeList(@RequestBody Map<String,Object> condition){
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")),1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")),10);
        QueryWrapper<TRoutePeakTimeRangeDto> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("status" ,"1");
        ResultVo resultVo = new AbstractPageTemplate<TRoutePeakTimeRangeDto>() {
            @Override
            protected List<TRoutePeakTimeRangeDto> executeSql() {
                List<TRoutePeakTimeRangeDto> list = tRoutePeakTimeRangeService.list(queryWrapper);
                return list;
            }
        }.preparePageTemplate(pageIndex,pageSize);
        return resultVo;
    }
}
