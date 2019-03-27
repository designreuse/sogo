
package com.yihexinda.dataservice.api;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.Constants;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.*;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.*;
import com.yihexinda.dataservice.service.*;
import com.yihexinda.dataservice.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/11/28 0028
 */

@RestController
@RequestMapping("/car/client")
@Slf4j
public class CarApiResource {

    @Autowired
    private TCarService tCarService;
    @Autowired
    private TCarPositionService tCarPositionService;
    @Autowired
    private TDriverCarBindService tDriverCarBindService;
    @Autowired
    private TViaService viaService;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TOrderService orderService;

    @Autowired
    private TRouteOffpeakService routeOffpeakService;

    @Autowired
    private TRoutePeakService routePeakService;
    @Autowired
    private TLineDriverBindService lineDriverBindService;
    /**
     * 区域服务
     */
    @Resource
    private RegionsApiResource regionsApiResource;

    /**
     * 系统用户服务
     */
    @Resource
    private  TSysUserService tSysUserService;

    @RequestMapping(value = "/carApi/getCars" ,method = RequestMethod.GET)
    public List<TCarDto> getCars() {
        return tCarService.list();
    }

    /**
     * 添加车辆信息
     * @param car 车辆信息
     * @return ResultVo
     */
    @RequestMapping(value = "/addCar",method = RequestMethod.POST)
    public ResultVo addCar(@RequestBody TCarDto car){
        boolean save = tCarService.save(car);
        if(save){
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }


    /**
     * wenbn
     * 删除车辆
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteCar/{id}",method = RequestMethod.POST)
    public ResultVo deleteCar(@PathVariable String id){
        TCarDto carDto = tCarService.getById(id);
        if(null != carDto){
            boolean success = tCarService.removeById(id);
            if(success){
                return ResultVo.success();
            }
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * wenbn
     * 修改车辆
     * @param car
     * @return
     */
    @RequestMapping(value = "/updateCar",method = RequestMethod.POST)
    public ResultVo updateCar(@RequestBody TCarDto car){
        TCarDto oldCar = tCarService.getById(car.getId());
        if(null != oldCar){
            car.setUpdateDate(new Date());
            boolean success = tCarService.saveOrUpdate(car);
            if(success){
                return ResultVo.success();
            }
        }

        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }



    /**
     * wenbn
     * 查询车辆列表
     * @param condition 参数
     * @return  车辆列表
     */
    @PostMapping(value = "/carList")
    public ResultVo carList(@RequestBody Map<String,Object> condition){
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")),1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")),10);
        QueryWrapper<TCarDto> queryWrapper = new QueryWrapper<>();
        //车牌号
        String carNo = StringUtil.trim(condition.get("carNo"));
        if (!"".equals(carNo)){
            queryWrapper.like("car_no", carNo);
        }
        //车辆类型
        String type = StringUtil.trim(condition.get("type"));
        if (!"".equals(type)){
            queryWrapper.eq("type", type);
        }
        //车辆状态
        String status = StringUtil.trim(condition.get("status"));
        if (!"".equals(status)){
            queryWrapper.eq("status", status);
        }
        //开始时间与结束时间不为空
        Timestamp aStartTime =null;
        Timestamp aEndTime =null;


        if (!"".equals(StringUtil.trim(condition.get("startTime")))&&!"".equals(condition.get("endTime"))){
            String endTime = StringUtil.trim(condition.get("endTime")).replace("00:00:00","23:59:59");
             aStartTime = Timestamp.valueOf(StringUtil.trim(condition.get("startTime")));
             aEndTime = Timestamp.valueOf(endTime);
        }
        String dateType = StringUtil.trim(condition.get("dateType"));
        //创建时间
        if ("0".equals(dateType)){
            queryWrapper.between("create_date",aStartTime,aEndTime);
        }
        //修改时间
        if ("1".equals(dateType)){
            queryWrapper.between("update_date",aStartTime,aEndTime);
        }
        queryWrapper.orderByDesc("create_date");
        //2018/12/20注释，注释原因，id为固定1
       // queryWrapper.eq("id" ,"1");
        ResultVo resultVo = new AbstractPageTemplate<TCarDto>() {
            @Override
            protected List<TCarDto> executeSql() {
                List<TCarDto> list = tCarService.list(queryWrapper);
                for (TCarDto tCarDto : list) {
                    //添加创建人名称
                    if(StringUtil.isNotEmpty(tCarDto.getCreateId())){
                    tCarDto.setCreateName(tSysUserService.getById(tCarDto.getCreateId()).getUsername());
                    }
                    //添加车辆地址名称
                    if(StringUtil.isNotEmpty(tCarDto.getAreaPath())){
                    tCarDto.setRegionAddress(regionsApiResource.regionAddress(tCarDto.getAreaPath()));
                    }
                }
                return list;
            }
        }.preparePageTemplate(pageIndex,pageSize);
        return resultVo;
    }

    /**
     * 获取车辆位置
     * @param data 车辆号
     * @return ResultVo
     */
    @PostMapping(value = "/queryCarPosition")
    public ResultVo queryCarPosition(@RequestBody Map<String,Object> data){
        QueryWrapper<TCarDto> carDtoQueryWrapper =new QueryWrapper<>();
        carDtoQueryWrapper.eq("car_no",String.valueOf(data.get("carNo")));
        TCarDto tCarDto = tCarService.getOne(carDtoQueryWrapper);
        if (tCarDto==null){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"车辆未查询到");
        }
        QueryWrapper<TCarPositionDto> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("car_id",tCarDto.getId());
        queryWrapper.orderByDesc("create_date");
        queryWrapper.last("limit 1");
        TCarPositionDto tCarPositionDto =tCarPositionService.getOne(queryWrapper);
        if (tCarPositionDto==null){
             return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,ResultConstant.SYS_REQUIRED_DATA_ERROR_VALUE);
        }
        return ResultVo.success().setDataSet(tCarPositionDto);

    }

    /**
     * pengfeng
     * 车辆是否到站
     * @param  carPosition 当前车位置信息
     * @return ResultVo
     */
    @PostMapping(value = "/carGetDown")
    public ResultVo carGetDown(@RequestBody TCarPositionDto carPosition){
        if ("".equals(StringUtil.trim(carPosition.getCarId()))){
         return  ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR,ResultConstant.SYS_REQUIRED_PARAMETER_ERROR_VALUE);
        }
        if (!tCarService.carGetDown(carPosition)){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
        }
        return ResultVo.success();
    }


    /**
     * 添加车辆坐标信息
     * @param carPosition
     * @return
     */
    @RequestMapping(value = "/addCarPosition",method = RequestMethod.POST)
    ResultVo addCarPosition(@RequestBody TCarPositionDto carPosition){
        QueryWrapper<TCarPositionDto> queryWrapper = new QueryWrapper<TCarPositionDto>();
        queryWrapper.eq("car_id",carPosition.getCarId());
        queryWrapper.orderByDesc("create_date");
        queryWrapper.last("limit 1");
        List<TCarPositionDto> list = tCarPositionService.list(queryWrapper);
        if(list!= null && list.size()>0){
            TCarPositionDto carPositionDto = list.get(0);
            carPosition.setLastLatitude(carPositionDto.getLatitude());
            carPosition.setLastLongitude(carPositionDto.getLongitude());
        }
        boolean save = tCarPositionService.save(carPosition);
        if(save){
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }





    /**
     * pengfeng
     * 查询未绑定车辆
     * @return 未绑定车辆列表
     */
    @RequestMapping(value = "/getCarList" ,method = RequestMethod.GET)
    public ResultVo getCarList() {
        QueryWrapper<TCarDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status","1");
        queryWrapper.notInSql("id","select  car_id from t_driver_car_bind where bind_status<>'2'");
        return ResultVo.success().setDataSet(tCarService.list(queryWrapper));
    }


    /**
     * 将进行中订单修改为完成状态
     */
    @RequestMapping(value = "/updateOrderStatus",method = RequestMethod.POST)
    public Object updateOrderStatus(){
        //获取站点
        String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
        List<Map<Object,Object>> tStationDtos = JsonUtil.parseJSON2List(stations);
        //获取进行中订单 ( 0 创建订单 1待使用 2进行中 3已完成 4已失效 )
        QueryWrapper<TOrderDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_status" ,"2");
        List<TOrderDto> orderList = orderService.list(queryWrapper);
        if(orderList.isEmpty()){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
        }
        //获取车辆位置
        List<Map<String,Object>> vehicleList = tCarPositionService.selectCurCarPostion();
        List<TOrderDto> resultList = new ArrayList<TOrderDto>();
        for(TOrderDto order: orderList){
            for (Map<Object, Object> tStationDto : tStationDtos) {
                for(Map<String, Object> carLocation:  vehicleList ){
                        if(StringUtil.isNotEmpty(order.getCarId()) && StringUtil.isNotEmpty((String) carLocation.get("id"))){
                            if(carLocation.get("lng") != null && carLocation.get("lati") != null ){
                                if (order.getEndStationId().equals(tStationDto.get("id") ) && order.getCarId().equals(carLocation.get("id"))) {
                                    Double carLongitude = (Double) carLocation.get("lng"); //车辆经度
                                    Double carLatitude  =  (Double) carLocation.get("lati");//车位纬度
                                    //车辆超过下车站点50米 将订单状态修改为 3
                                    Double distance = MapUtil.GetDistance((Double)tStationDto.get("longitude"),(Double) tStationDto.get("latitude"),carLongitude,carLatitude);
                                    if(distance >= 50){
                                        order.setOrderStatus("3");
                                        resultList.add(order);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        //批量修改数据
        if(resultList.size() > 0 ){
            orderService.updateBatchById(resultList,resultList.size());
        }
        return 0;
    }

    /**
     * 平峰实时导航
     * @return
     */
    @RequestMapping(value = "/navigation" ,method = RequestMethod.POST)
    ResultVo navigation(@RequestBody Map<String,Object> condition){
        String carId = StringUtil.trim(condition.get("carId"));
        if(StringUtil.isEmpty(carId)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        Map<String, Object> map = routeOffpeakService.getMap(new QueryWrapper<TRouteOffpeakDto>()
                .select("id","midway_station","curr_station","all_position")
                .eq("car_id", carId)
                .eq("route_state", "0")
                .apply("to_char(create_date,'yyyy-MM-dd')={0}",DateUtils.formatDate(new Date(), Constants.DATE_PATTERN))
        );
        if(map==null){
            return ResultVo.error(-1,"暂无导航数据");
        }
        //途经站点
        String midway_station = StringUtil.trim(map.get("midway_station"));
        String all_position = StringUtil.trim(map.get("all_position"));
        //当前站
        String curr_station = StringUtil.trim(map.get("curr_station"),"-1");
        String[] stationSplit = midway_station.split(",");
        if(stationSplit == null || stationSplit.length==0){
            return ResultVo.error(-1,"暂无停靠站点数据");
        }
        map.clear();
        //当前站
        String cur_station = null;
        //下一站
        String next_station = null;
        if(curr_station.equals("-1")){
            next_station = stationSplit[0];
        }else{
            int size = stationSplit.length-1;
            for (int i = 0 ;i<=size ;i++) {
                String station = stationSplit[i];
                if(station.equals(curr_station)){
                    cur_station = curr_station;
                    if(i == size){
                        next_station = null;
                    }else{
                        next_station = stationSplit[i+1];
                    }
                    break;
                }
            }
        }
        //查询缓存的站点信息
        String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
        List<Map<Object, Object>> stationList = JsonUtil.parseJSON2List(stations);
        for (Map<Object, Object> station : stationList) {
            String id = StringUtil.trim(station.get("id"));
            if(id.equals(cur_station)){
                map.put("cur_station_name",StringUtil.trim(station.get("site_name")));
                map.put("cur_station_id",cur_station);
            }
            if(id.equals(next_station)){
                map.put("next_station_name",StringUtil.trim(station.get("site_name")));
                map.put("next_station_id",next_station);
            }
        }

        //统计下一站的上车人数
        int count = orderService.count(new QueryWrapper<TOrderDto>()
                .eq("is_pay", "1")
                .eq("order_status", "1")
                .eq("start_station_id", next_station)
                .eq("car_id", carId)
                .apply("to_char(create_date,'yyyy-MM-dd')={0}", DateUtils.formatDate(new Date(), Constants.DATE_PATTERN))
//                .apply("to_char(trip_time,'yyyy-MM-dd')={0}", DateUtils.formatDate(new Date(), Constants.DATE_PATTERN))
        );
        map.put("num",count);
        List<Map<Object, Object>> allPositions = JsonUtil.parseJSON2List(all_position);

        if(curr_station.equals("-1")) {
            map.put("allPosition",allPositions);
        }else{
            for (Map<Object, Object> allPosition : allPositions) {
                String id = StringUtil.trim(allPosition.get("id"));
                if(curr_station.equals(id)){
                    allPositions.remove(allPosition);
                    break;
                }
            }
            map.put("allPosition",allPositions);
        }
        return ResultVo.success().setDataSet(map);
    }

    /**
     * 高峰实时导航
     * @return
     */
    @RequestMapping(value = "/realNavigation" ,method = RequestMethod.POST)
    ResultVo realNavigation(@RequestBody Map<String, Object> condition){
        String token = StringUtil.trim(condition.get("token"));
//        String driverId = RequestUtil.analysisToken(token).getUserId();
        String driverId = StringUtil.trim(condition.get("driverId"));
        if(StringUtil.isEmpty(driverId)){
            driverId = RequestUtil.analysisToken(token).getUserId();
        }

        //查询缓存的站点信息
        String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
        List<Map<Object, Object>> stationList = JsonUtil.parseJSON2List(stations);

        //根据线路ids查询司机信息
        Map<String, Object> map = lineDriverBindService.getMap(new QueryWrapper<TLineDriverBindDto>()
                .select("driver_id","line_id")
                .eq("driver_id", driverId)
        );
        if(map == null){
            return ResultVo.error(-1,"未查询到高峰信息，请检查司机是否关联高峰线路");
        }
        String line_id = StringUtil.trim(map.get("line_id"));
        String lineStationkey = redisUtil.get("maas_peak_line_station" + line_id);
        Map<String, Object> routeMap = routePeakService.getMap(new QueryWrapper<TRoutePeakDto>()
                .select("id")
                .eq("driver_user_id", driverId)
                .eq("route_state", "0")
                .apply("to_char(create_date,'yyyy-MM-dd')={0}",DateUtils.formatDate(new Date(), Constants.DATE_PATTERN))
        );
        if(routeMap==null){
            return ResultVo.error(-1,"暂无导航数据");
        }
        ResultVo success = ResultVo.success();
        if(StringUtil.isEmpty(lineStationkey)){

            List<Map<String, Object>> lineViaList = viaService.listMaps(new QueryWrapper<TViaDto>()
                    .select("id via_id","line_id","index","station_id","type")
                    .eq("line_id",line_id)
            );
            //线路id
            lineViaList.sort((a,b) -> StringUtil.getAsInt(StringUtil.trim(a.get("index"))) - StringUtil.getAsInt(StringUtil.trim(b.get("index"))));
            //添加站点信息
            for (Map<String, Object> via : lineViaList) {
                String station_id = StringUtil.trim(via.get("station_id"));
                for (Map<Object, Object> station : stationList) {
                    String id = StringUtil.trim(station.get("id"));
                    if(station_id.equals(id)){
                        via.put("site_name",StringUtil.trim(station.get("site_name")));
                        via.put("longitude",StringUtil.trim(station.get("longitude")));
                        via.put("latitude",StringUtil.trim(station.get("latitude")));
                        via.put("type",StringUtil.trim(station.get("type")));
                    }
                }
            }
            //将车辆线路信息推送给司机端导航
            redisUtil.set("maas_peak_line_station"+line_id,JsonUtil.list2Json(lineViaList), (long) (60*60*10));
            success.setDataSet(lineViaList);
        }else{
            List<Map<Object, Object>> maps = JsonUtil.parseJSON2List(lineStationkey);
            success.setDataSet(maps);
        }
        return success;
    }

    /**
     * 查询车辆详情信息
     * @param carId 车辆id
     * @return ResultVo
     */
    @RequestMapping(value = "/getCarId/{carId}",method = RequestMethod.GET)
    public ResultVo addCar(@PathVariable String carId){
        if (StringUtil.isEmpty(carId)){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"车辆id不能为空");
        }
            return ResultVo.success().setDataSet(tCarService.getById(carId));
    }
    /**
     * 查询车辆信息
     * @return ResultVo
     */
    @RequestMapping(value = "/getCarListExcel",method = RequestMethod.GET)
    public List<TCarDto> getCarListExcel(){
        List<TCarDto> tCarDtoList = tCarService.list();
        for (TCarDto tCarDto : tCarDtoList) {
            //添加创建人名称
            if(StringUtil.isNotEmpty(tCarDto.getCreateId())){
                tCarDto.setCreateName(tSysUserService.getById(tCarDto.getCreateId()).getUsername());
            }
            //添加车辆地址名称
            if(StringUtil.isNotEmpty(tCarDto.getAreaPath())){
                tCarDto.setRegionAddress(regionsApiResource.regionAddress(tCarDto.getAreaPath()));
            }
        }
       return tCarDtoList;
    }

    /**
     * 查询车辆总座位数
     * @return ResultVo
     */
    @RequestMapping(value = "/getCarCount",method = RequestMethod.GET)
    public Integer getCarCount(){
     return   tCarService.getCarCount();
    }

    /**
     * 查询车辆状态为可用的车辆
     * @return ResultVo
     */
    @RequestMapping(value = "/getCarListStatus",method = RequestMethod.GET)
    public ResultVo getCarListStatus(){
        QueryWrapper<TCarDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status","1");
        return  ResultVo.success().setDataSet(tCarService.list(queryWrapper));
    }


    /**
     * wenbn
     * 司机确认到站
     * @param condition
     * @return
     */
    @RequestMapping(value = "/confirmArrive" ,method = RequestMethod.POST)
    ResultVo confirmArrive(@RequestBody Map<String, Object> condition){
        String carId = StringUtil.trim(condition.get("carId"));
        if(StringUtil.isEmpty(carId)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        //司机确认到站
        return tCarService.confirmArrive(condition);
    }

}
