package com.yihexinda.dataservice.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.constants.SysParamConstant;
import com.yihexinda.core.utils.JsonUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.*;
import com.yihexinda.dataservice.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 高峰行程信息管理类
 * @author wenbn
 * @version 1.0
 * @date 2018/11/30 0030
 */
@RestController
@RequestMapping("/routePeak/client")
@Slf4j
public class RoutePeakApiResource {

    @Autowired
    private TRoutePeakService tRoutePeakService;

    /**
     * 区域服务
     */
    @Resource
    private  RegionsApiResource regionsApiResource;

    /**
     * 线路服务
     */
    @Resource
    private TViaService tViaService;

    /**
     * 系统配置表服务
     */
    @Resource
    private TSysParamService tSysParamService;

    /**
     *  线路服务
     */
    @Resource
    private TLineService tLineService;

    /**
     *  站点服务
     */
    @Resource
    private TStationService tStationService;

    /**
     * wenbn
     * pc 添加高峰行程
     * @param tRoutePeakDto 高峰行程信息
     * @return ResultVo
     */
    @RequestMapping(value = "/addRoutePeak" ,method = RequestMethod.POST)
    public ResultVo addRoutePeak(@RequestBody TRoutePeakDto tRoutePeakDto) {
     if(tRoutePeakService.addRoutePeak(tRoutePeakDto)){
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }


    /**
     * 2018/12/18 ，因去除运营时间，000取系统配置表
     *  批量修改运营时间
     * @param data 运营时间，高峰id
     * @return ResultVo
     */
    @RequestMapping(value = "/routePeakBatch" ,method = RequestMethod.POST)
    public ResultVo routePeakBatch(@RequestBody Map<String,Object> data) {
            List<TRoutePeakDto> routePeakDtoList = JsonUtil.json2List(String.valueOf(data.get("routePeakDtoList")), TRoutePeakDto.class);


//        if(tRoutePeakService.addRoutePeak(tRoutePeakDto)){
//            return ResultVo.success();
//        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 高峰线路分配司机
     *
     * @param data 高峰id,司机listId
     * @return ResultVo
     */
    @RequestMapping(value = "/distribution" ,method = RequestMethod.POST)
    public ResultVo distribution(@RequestBody Map<String,Object> data) {
        //是否具有司机id
        if (!"null".equals(String.valueOf(data.get("lineDriverBindDtoList")))){
            List<TLineDriverBindDto> lineDriverBindDtoList= JSONObject.parseArray(JsonUtil.toJson(data.get("lineDriverBindDtoList")),TLineDriverBindDto.class);
        //高峰id与司机id是否存在
        if (!"null".equals(String.valueOf(data.get("lineId")))){
            if (lineDriverBindDtoList.size()>0){
           if (tRoutePeakService.distribution(String.valueOf(data.get("lineId")),lineDriverBindDtoList)){
               return  ResultVo.success();
           }
            }else {
                if (tRoutePeakService.distribution(String.valueOf(data.get("lineId")),null)){
                    return  ResultVo.success();
                }
            }
        }
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR,ResultConstant.SYS_REQUIRED_PARAMETER_ERROR_VALUE);
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }


    /**
     *
     *  修改高峰行程
     * @param tRoutePeakDto 行程信息
     * @return ResultVo
     */
    @RequestMapping(value = "/updateRoutePeak" ,method = RequestMethod.POST)
    public ResultVo updateRoutePeak(@RequestBody TRoutePeakDto tRoutePeakDto) {
        if (StringUtil.isNotEmpty(tRoutePeakDto.getLineDto().getId())){
            tRoutePeakDto.getLineDto().setUpdateDate(new Date());
            if (tRoutePeakService.updateRoutePeak(tRoutePeakDto)){
                return  ResultVo.success();
            }
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }



    /**
     *
     *  查询高峰详情
     * @param id 高峰行程id
     * @return ResultVo
     */
    @GetMapping(value = "/getRoutePeakId/{id}")
    public ResultVo getRoutePeakId(@PathVariable String id) {
        if(StringUtil.isEmpty(id)){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,ResultConstant.SYS_REQUIRED_DATA_ERROR_VALUE);
        }
        TLineDto tLineDto = tLineService.getById(id);

        if (null==tLineDto){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
        }

        tLineDto.setLineStartName(tStationService.getById(tLineDto.getLineStartId()).getSiteName());
        tLineDto.setLineEndName(tStationService.getById(tLineDto.getLineEndId()).getSiteName());
        return  ResultVo.success().setDataSet(tLineDto.setViaList(tViaService.getViaList(tLineDto.getId())));
    }



    /**
     * PC 查询高峰行程
     * @param condition 参数
     * @return ResultVo
     */
    @PostMapping(value = "/routePeakList")
    public ResultVo routePeakList(@RequestBody Map<String,Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")),1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")),10);
        if (!"".equals(StringUtil.trim(condition.get("startTime")))&&!"".equals(condition.get("endTime"))){
            Timestamp aStartTime = Timestamp.valueOf(StringUtil.trim(condition.get("startTime")));
            String endTime = StringUtil.trim(condition.get("endTime")).replace("00:00:00","23:59:59");
            Timestamp aEndTime = Timestamp.valueOf(endTime);
            condition.put("aStartTime", aStartTime);
            condition.put("aEndTime", aEndTime);
        }
        ResultVo resultVo = new AbstractPageTemplate<TRoutePeakDto>() {
            @Override
            protected List<TRoutePeakDto> executeSql() {
                List<TRoutePeakDto> list = tRoutePeakService.routePeakList(condition);
                for (TRoutePeakDto tRoutePeakDto : list) {
                    if (StringUtil.isNotEmpty(tRoutePeakDto.getAreaPath())){
                        tRoutePeakDto.setRegionAddress(regionsApiResource.regionAddress(tRoutePeakDto.getAreaPath()));
                    }
                    tRoutePeakDto.setViaList(tViaService.getViaList(tRoutePeakDto.getId())) ;
                    QueryWrapper<TSysParamDto> queryWrapper =new QueryWrapper<>();
                    queryWrapper.eq("param_name", SysParamConstant.SYS_PEAK_TIME_RANGE);
                    //运营时间，如后期不取系统配置时间，自行更改
                    TSysParamDto tSysParamDto=  tSysParamService.getOne(queryWrapper);
                    if (tSysParamDto!=null){
                        tRoutePeakDto.setOperateTime(tSysParamDto.getParamValue());
                    }
                    QueryWrapper<TSysParamDto> queryPriceWrapper =new QueryWrapper<>();
                    queryPriceWrapper.eq("param_name",SysParamConstant.SYS_PEAK_PRICE);
                    //票价，如后期不取系统配置票价，自行更改
                    TSysParamDto sysParamDto =tSysParamService.getOne(queryPriceWrapper);
                    if (sysParamDto!=null){
                        tRoutePeakDto.setPrice(new BigDecimal(sysParamDto.getParamValue()));
                    }
                }
                return list;
            }
        }.preparePageTemplate(pageIndex,pageSize);
        return resultVo;
    }

    /**
     * wenbn
     * Buss查询高峰行程
     * @param
     * @return
     */
    @PostMapping(value = "/buss/routePeakList")
    public ResultVo bussRoutePeakList(@RequestBody Map<String,Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")),1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")),10);
        QueryWrapper<TRoutePeakDto> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id" ,"1");
        ResultVo resultVo = new AbstractPageTemplate<TRoutePeakDto>() {
            @Override
            protected List<TRoutePeakDto> executeSql() {
                List<TRoutePeakDto> list = tRoutePeakService.list(queryWrapper);
                return list;
            }
        }.preparePageTemplate(pageIndex,pageSize);
        return resultVo;
    }

    /**
     * PC 查询高峰行程
     * @return List<TRoutePeakDto>
     */
    @GetMapping(value = "/getRoutePeakListExcel")
    public List<TRoutePeakDto> getRoutePeakListExcel() {
        List<TRoutePeakDto> tRoutePeakDtoList = tRoutePeakService.routePeakList(null);
        for (TRoutePeakDto tRoutePeakDto : tRoutePeakDtoList) {
            if (StringUtil.isNotEmpty(tRoutePeakDto.getAreaPath())){
                tRoutePeakDto.setRegionAddress(regionsApiResource.regionAddress(tRoutePeakDto.getAreaPath()));
            }
            tRoutePeakDto.setViaList(tViaService.getViaList(tRoutePeakDto.getId())) ;
            QueryWrapper<TSysParamDto> queryWrapper =new QueryWrapper<>();
            queryWrapper.eq("param_name", SysParamConstant.SYS_PEAK_TIME_RANGE);
            //运营时间，如后期不取系统配置时间，自行更改
            TSysParamDto tSysParamDto=  tSysParamService.getOne(queryWrapper);
            if (tSysParamDto!=null){
                tRoutePeakDto.setOperateTime(tSysParamDto.getParamValue());
            }
            QueryWrapper<TSysParamDto> queryPriceWrapper =new QueryWrapper<>();
            queryPriceWrapper.eq("param_name",SysParamConstant.SYS_PEAK_PRICE);
            //票价，如后期不取系统配置票价，自行更改
            TSysParamDto sysParamDto =tSysParamService.getOne(queryPriceWrapper);
            if (sysParamDto!=null){
                tRoutePeakDto.setPrice(new BigDecimal(sysParamDto.getParamValue()));
            }
        }
        return tRoutePeakDtoList;
    }


}
