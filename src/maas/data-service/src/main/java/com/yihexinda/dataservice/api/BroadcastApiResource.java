package com.yihexinda.dataservice.api;

import java.text.SimpleDateFormat;
import java.util.*;

import com.yihexinda.data.dto.TBoradcastTimeRangeDto;
import com.yihexinda.dataservice.service.TBoradcastTimeRangeService;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TBroadcastDto;
import com.yihexinda.data.dto.TCarDto;
import com.yihexinda.data.dto.TLineDto;
import com.yihexinda.dataservice.service.TBroadcastService;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @author zl
 * @version 1.0
 * @date 2018/11/28 0028
 */
@RestController
@RequestMapping("/broadcast/client")
public class BroadcastApiResource {

    @Autowired
    private TBroadcastService tbroadcastService;

    /**
     * 广播运营时间服务
     */
    @Resource
    private TBoradcastTimeRangeService tBoradcastTimeRangeService;

    /**
     * 显示广播
     *
     * @return
     */
    @GetMapping("/getBroadcastList")
    public ResultVo getBroadcastList() {
        List<TBroadcastDto> list = tbroadcastService.getBroadcastList();
        if (list != null && list.size() > 0) {
            return ResultVo.success().setDataSet(list.get(0));
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, "暂无广播数据");
    }

    /**
     * 添加广播
     *
     * @param tbroadcastDto 广播信息
     * @return ResultVo
     */
    @PostMapping("/addBroadcast")
    @Transactional(rollbackFor = Exception.class)
    public ResultVo addBroadcast(@RequestBody TBroadcastDto tbroadcastDto) {
        if ("1".equals(tbroadcastDto.getStatus())) {
            int result = seach(tbroadcastDto);
            if (result == 1) {
                return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, "已有该时间的广播");
            }
        }
        if (tbroadcastService.save(tbroadcastDto)) {
            if (null != tbroadcastDto.getBoradcastTimeRangeDtoList() && tbroadcastDto.getBoradcastTimeRangeDtoList().size() > 0) {
                tbroadcastDto.getBoradcastTimeRangeDtoList().forEach(boradcastTimeRangeDto -> boradcastTimeRangeDto.setBoradcastId(tbroadcastDto.getId()));
                if (tBoradcastTimeRangeService.saveBatch(tbroadcastDto.getBoradcastTimeRangeDtoList())) {
                    return ResultVo.success();
                }
            }
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 删除广播
     *
     * @param broadcastId
     * @return
     */
    @GetMapping("/deleteBroadcast/{broadcastId}")
    public ResultVo deleteBroadcast(@PathVariable("broadcastId") String broadcastId) {
        TBroadcastDto tbroadcastDto = tbroadcastService.getById(broadcastId);
        if (tbroadcastDto != null) {
            if (tbroadcastService.removeById(broadcastId)) {
                return ResultVo.success();
            }
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 修改广播
     *
     * @param tbroadcastDto 广播信息
     * @return ResultVo
     */
    @PutMapping("/updateBroadcast")
    @Transactional(rollbackFor = Exception.class)
    public ResultVo updateBroadcast(@RequestBody TBroadcastDto tbroadcastDto) {
        //是否有修改信息
        if (StringUtil.isEmpty(tbroadcastDto.getId())) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR, "修改信息为空，请检查");
        }
        if ("1".equals(tbroadcastDto.getStatus())){
            int result = seach(tbroadcastDto);
            if (result ==1){
                return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"已有该时间的广播");
            }
        }
        tbroadcastDto.setUpdateDate(new Date());
        //修改广播信息
        if (tbroadcastService.updateById(tbroadcastDto)) {
            QueryWrapper<TBoradcastTimeRangeDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("boradcast_id", tbroadcastDto.getId());
            if (tBoradcastTimeRangeService.list(queryWrapper).size() > 0) {
                if (!tBoradcastTimeRangeService.remove(queryWrapper)) {
                    return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR, "系统错误，联系管理员");
                }
            }
            //添加广播时间
            if (null != tbroadcastDto.getBoradcastTimeRangeDtoList() && tbroadcastDto.getBoradcastTimeRangeDtoList().size() > 0) {
                tbroadcastDto.getBoradcastTimeRangeDtoList().forEach(boradcastTimeRangeDto -> boradcastTimeRangeDto.setBoradcastId(tbroadcastDto.getId()));
                if (!tBoradcastTimeRangeService.saveBatch(tbroadcastDto.getBoradcastTimeRangeDtoList())) {
                    return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR, "系统错误，请联系管理员");
                }
            }
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }


    /**
     * 查询广播
     *
     * @param condition 参数
     * @return 广播列表
     */
    @PostMapping("/broadcastList")
    public ResultVo broadcastList(@RequestBody Map<String, Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")), 1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")), 10);
        QueryWrapper<TBroadcastDto> queryWrapper = new QueryWrapper<>();
        ResultVo resultVo = new AbstractPageTemplate<TBroadcastDto>() {
            @Override
            protected List<TBroadcastDto> executeSql() {
                List<TBroadcastDto> list = tbroadcastService.list(queryWrapper);
                for (TBroadcastDto tBroadcastDto : list) {
                    QueryWrapper<TBoradcastTimeRangeDto> queryTimeWrapper = new QueryWrapper<>();
                    queryTimeWrapper.eq("boradcast_id", tBroadcastDto.getId());
                    tBroadcastDto.setBoradcastTimeRangeDtoList(tBoradcastTimeRangeService.list(queryTimeWrapper));
                }
                return list;
            }
        }.preparePageTemplate(pageIndex, pageSize);
        return resultVo;
    }

    /**
     * 查询广播详情
     *
     * @param id 参数
     * @return 广播列表
     */
    @GetMapping("/getBroadcast/{id}")
    public ResultVo getBroadcast(@PathVariable String id) {
        if (StringUtil.isEmpty(id)) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR, "广播id不能为空");
        }
        TBroadcastDto tBroadcastDto = tbroadcastService.getById(id);
        if (tBroadcastDto != null) {
            QueryWrapper<TBoradcastTimeRangeDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("boradcast_id", id);
            tBroadcastDto.setBoradcastTimeRangeDtoList(tBoradcastTimeRangeService.list(queryWrapper));
        }
        return ResultVo.success().setDataSet(tBroadcastDto);
    }

    /**
     * 修改广播状态
     *
     * @param tbroadcastDto 广播信息
     * @return ResultVo
     */
    @PostMapping("/updateBroadcastStatus")
    public ResultVo updateBroadcastStatus(@RequestBody TBroadcastDto tbroadcastDto) {
        //是否有修改信息
        if (StringUtil.isEmpty(tbroadcastDto.getId())) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR, "修改信息为空，请检查");
        }
        //2019/1/16 新增，功能为：不允许时间有重叠
        if ("1".equals(tbroadcastDto.getStatus())) {
            TBroadcastDto broadcastDto = tbroadcastService.getById(tbroadcastDto.getId());
            QueryWrapper<TBoradcastTimeRangeDto> queryWrapper =new QueryWrapper<>();
            queryWrapper.eq("boradcast_id",tbroadcastDto.getId());
            broadcastDto.setBoradcastTimeRangeDtoList(tBoradcastTimeRangeService.list(queryWrapper));
            int result = seach(broadcastDto);
            if (result==1){
                return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"已有该时间的广播");
            }
        }
        tbroadcastDto.setUpdateDate(new Date());
        //修改广播信息
        if (tbroadcastService.updateById(tbroadcastDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    private int seach(TBroadcastDto tbroadcastDto) {
        int result = 0;
        QueryWrapper<TBroadcastDto> queryDateWrapper = new QueryWrapper<>();
        queryDateWrapper.eq("status", "1");
        List<TBroadcastDto> tBroadcastDtoList = tbroadcastService.list(queryDateWrapper);
        List<String> overlapList = new ArrayList<>();
        Long startTime = tbroadcastDto.getOnlineTime().getTime();
        Long endTime = tbroadcastDto.getOfflineTime().getTime();
        if (tBroadcastDtoList.size() > 0) {
            for (TBroadcastDto tBroadcastDto : tBroadcastDtoList) {
                Long onLineTime = tBroadcastDto.getOnlineTime().getTime();
                Long offLineTime = tBroadcastDto.getOfflineTime().getTime();
                if (endTime < onLineTime || startTime > offLineTime) {
                    System.out.println("时间不存在重叠");
                } else {
                    overlapList.add(tBroadcastDto.getId());
                }
            }
        }
        if (overlapList.size() > 0) {
            QueryWrapper<TBoradcastTimeRangeDto> queryWrapper = new QueryWrapper<>();
            queryDateWrapper.in("boradcast_id", overlapList);
            List<TBoradcastTimeRangeDto> tBoradcastTimeRangeDtoList = tBoradcastTimeRangeService.list(queryWrapper);
            if (tBoradcastTimeRangeDtoList.size() > 0 && tbroadcastDto.getBoradcastTimeRangeDtoList().size() > 0) {
                if (tbroadcastDto.getBoradcastTimeRangeDtoList().size() > 0) {
                    for (TBoradcastTimeRangeDto boradcastTimeRangeDto : tbroadcastDto.getBoradcastTimeRangeDtoList()) {
                        Long startDate = boradcastTimeRangeDto.getStartDate().getTime();
                        Long endDate = boradcastTimeRangeDto.getEndDate().getTime();
                        for (TBoradcastTimeRangeDto tBoradcastTimeRangeDto : tBoradcastTimeRangeDtoList) {
                            Long onLineDate = tBoradcastTimeRangeDto.getStartDate().getTime();
                            Long offLineDate = tBoradcastTimeRangeDto.getEndDate().getTime();
                            if (endDate < onLineDate || startDate > offLineDate) {
                                System.out.println("时间不存在重叠");
                            } else {
                                return result = 1;
                            }
                        }
                    }
                }
            }
        }
        return  result;
    }

}
