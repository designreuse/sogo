package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.dto.Json;
import com.yihexinda.core.utils.JsonUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TLineDto;
import com.yihexinda.data.dto.TRegionDto;
import com.yihexinda.data.dto.TStationDto;
import com.yihexinda.data.dto.TViaDto;
import com.yihexinda.dataservice.service.TLineService;
import com.yihexinda.dataservice.service.TRegionService;
import com.yihexinda.dataservice.service.TStationService;
import com.yihexinda.dataservice.service.TViaService;
import com.yihexinda.dataservice.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/3 0003
 */
@RestController
@RequestMapping("/station/client")
@Slf4j
public class StationApiResource {

    @Autowired
    private TStationService stationService;
    @Autowired
    private TRegionService regionService;
    @Autowired
    private TViaService viaService;
    @Autowired
    private TLineService lineService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 加载站点信息
     *
     * @return
     */
    @PostMapping(value = "/stationList")
    ResultVo loadStations(@RequestBody Map<String, Object> condition) throws ClassNotFoundException {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")));
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")));
        QueryWrapper<TStationDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "1");
        if (pageIndex == -1 || pageSize == -1) {
            queryWrapper.select("id", "link_id", "longitude", "latitude", "site_name", "region_id","type");
//            ArrayList<Object> type = Lists.newArrayList();
//            type.add("1");
//            type.add("2");
//            queryWrapper.in("type",type);
            List<Map<String, Object>> list = stationService.listMaps(queryWrapper);
            redisUtil.set(RedisConstant.SYS_STATIONS_CACHE_KEY, JsonUtil.list2Json(loadStationRegions(list)), RedisConstant.SYS_CACHE_KEY_TIME_ONE_MONTH);
            return ResultVo.success().setDataSet(list);
        }
        ResultVo resultVo = new AbstractPageTemplate<Map<String, Object>>() {
            @Override
            protected List<Map<String, Object>> executeSql() {
                List<Map<String, Object>> list = stationService.listMaps(queryWrapper);
                return list;
            }
        }.preparePageTemplate(pageIndex, pageSize);
        return resultVo;
    }


    /**
     * @author chenzeqi
     * @Date 2018-12-27
     * 高峰的加载所有的站点 和 线路信息
     * @return
     */
    @RequestMapping("loadPeakLineStations")
    public ResultVo loadPeakLineStations(@RequestBody Map<String, Object> data){
        return stationService.loadPeakLineStations(data);
    }

    /**
     *
     * @param stationList
     * @return
     */
    private List<Map<String, Object>> loadStationRegions(List<Map<String, Object>> stationList){
        List<String> regionIds = Lists.newArrayList();
        for (Map<String, Object> station : stationList) {
            regionIds.add(StringUtil.trim(station.get("region_id")));
        }
        List<Map<String, Object>> regionList = regionService.listMaps(new QueryWrapper<TRegionDto>()
                .select("id","region_name")
                .in("id",regionIds)
        );
        for (Map<String, Object> station : stationList) {
            String regionId = StringUtil.trim(station.get("region_id"));
            for (Map<String, Object> region : regionList) {
                String id = StringUtil.trim(region.get("id"));
                if(id.equals(regionId)){
                    station.put("regionName",StringUtil.trim(region.get("region_name")));
                }
            }
        }
        return stationList;
    }

    /**
     * 加载高峰途经站点
     * @param
     * @return
     */
    @PostMapping(value = "/loadPeakViaStations")
    ResultVo loadPeakViaStations(HashMap<Object, Object> condition){
        ResultVo success = ResultVo.success();

        //查询可用的线路
        List<Map<String, Object>> lineList = lineService.listMaps(new QueryWrapper<TLineDto>()
                .select("id")
                .eq("line_state", "1")
        );
        if(lineList == null || lineList.size() == 0){
            log.info("没有可用的高峰线...");
            return success;
        }
        List<String> lineIds  = Lists.newArrayList();
        lineList.forEach(line -> lineIds.add(StringUtil.trim(line.get("id"))));

        List<Map<String, Object>> maps = viaService.listMaps(new QueryWrapper<TViaDto>()
                .select("id","line_id","status","index","station_id","line_name","type")
                .in("line_id",lineIds)
                .eq("status","1")
        );
        if(maps!= null && maps.size()>0){
            redisUtil.set(RedisConstant.SYS_PEAK_VIA_STATIONS_CACHE_KEY, JsonUtil.toJson(maps),RedisConstant.SYS_CACHE_KEY_TIME_ONE_MONTH);
            success.setDataSet(maps);
        }
        return success;
    }

    /**
     *  查询站点列表
     * @return  站点列表
     */
    @GetMapping(value = "/stationList")
    ResultVo stationList()  {
        QueryWrapper<TStationDto> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("status","1");
        queryWrapper.eq("type","1");
        return ResultVo.success().setDataSet(stationService.list(queryWrapper));
    }


    /**
     * 加载高峰站点
     * @param
     * @return
     */
    @PostMapping(value = "/queryStationsByLines")
    ResultVo queryStationsByLines(@RequestBody Map<String, Object> data){
        List<String> lines = (List<String>) data.get("lines");
        List<Map<String, Object>> maps = viaService.listMaps(new QueryWrapper<TViaDto>()
                .select("id", "station_id","line_name","line_id","type")
                .in("line_id", lines)
                .eq("type", 1)
        );
        if(maps!= null && maps.size()>0){
            String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
            List<Map<Object, Object>> stationList = JsonUtil.parseJSON2List(stations);
            for (Map<String, Object> map : maps) {
                String station_id = StringUtil.trim(map.get("station_id"));
                for (Map<Object, Object> station : stationList) {
                    String stationId = StringUtil.trim(station.get("id"));
                    if(station_id.equals(stationId)){
                        map.put("site_name",station.get("site_name"));
                        break;
                    }
                }
            }
        }
        return ResultVo.success().setDataSet(maps);
    }


}
