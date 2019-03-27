package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TBoradcastTimeRangeDto;
import com.yihexinda.dataservice.service.TBoradcastTimeRangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zl
 * @version 1.0
 * @date 2018/11/28 0028
 */
@RestController
@RequestMapping("/broadcastTimeRange/client")
public class BoradcastTimeRangeApiResource {

    @Autowired
    private TBoradcastTimeRangeService tboradcastTimeRangeService;


    /**
     * 添加广播时间段
     * @param boradcastTimeRangeDto
     * @return
     */
    @PostMapping("/addBroadcastTimeRange")
    public ResultVo addBroadcastTimeRange(@RequestBody TBoradcastTimeRangeDto boradcastTimeRangeDto) {
        if (tboradcastTimeRangeService.save(boradcastTimeRangeDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 删除广播时间段
     * @param broadcastId 广播id
     * @return
     */
    @GetMapping("/deleteBroadcastTimeRange/{broadcastId}")
    public ResultVo deleteBroadcastTimeRange(@PathVariable("broadcastId") String broadcastId) {
        TBoradcastTimeRangeDto boradcastTimeRangeDto = tboradcastTimeRangeService.getById(broadcastId);
        if (boradcastTimeRangeDto != null) {
            if (tboradcastTimeRangeService.removeById(broadcastId)) {
                return ResultVo.success();
            }
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 修改广播时间段
     * @param boradcastTimeRangeDto
     * @return
     */
    @PutMapping("/updateBroadcastTimeRange")
    public ResultVo updateBroadcastTimeRange(@RequestBody TBoradcastTimeRangeDto boradcastTimeRangeDto) {
        if (tboradcastTimeRangeService.saveOrUpdate(boradcastTimeRangeDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 查询广播时间段
     * @param condition
     * @return
     */
    @PostMapping("/broadcastTimeRangeList")
    public ResultVo broadcastTimeRangeList(@RequestBody Map<String, Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")), 1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")), 10);
        QueryWrapper<TBoradcastTimeRangeDto> queryWrapper = new QueryWrapper<>();
        ResultVo resultVo = new AbstractPageTemplate<TBoradcastTimeRangeDto>() {
            @Override
            protected List<TBoradcastTimeRangeDto> executeSql() {
                List<TBoradcastTimeRangeDto> list = tboradcastTimeRangeService.list(queryWrapper);
                return list;
            }
        }.preparePageTemplate(pageIndex, pageSize);
        return resultVo;
    }
}
