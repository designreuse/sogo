package com.yihexinda.dataservice.service.impl;

import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TRoutePeakDto;
import com.yihexinda.dataservice.api.StationApiResource;
import com.yihexinda.dataservice.dao.TRouteOffpeakDao;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.constants.SysParamConstant;
import com.yihexinda.data.dto.*;
import com.yihexinda.dataservice.dao.TRoutePeakDao;
import com.yihexinda.dataservice.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 高峰行程信息表 服务实现类
 * </p>
 * @since 2018-12-18
 */
@Service
public class TRoutePeakServiceImpl extends ServiceImpl<TRoutePeakDao, TRoutePeakDto> implements TRoutePeakService {

    /**
     *    途径站点服务
     */
    @Resource
    private TViaService tViaService;

    /**
     * 站点api
     */
    @Resource
    private StationApiResource stationApiResource;

    /**
     * 线路服务
     */
    @Resource
    private TLineService tLineService;
    @Resource
    private TRoutePeakDao routePeakDao;
    /**
     * 高峰运营时间服务
     */
    @Resource
    private TRoutePeakTimeRangeService tRoutePeakTimeRangeService;

    /**
     * 高峰司机绑定服务
     */
    @Resource
    private TLineDriverBindService tLineDriverBindService;

    /**
     * 系统配置服务
     */
    @Resource
    private  TSysParamService tSysParamService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRoutePeak(TRoutePeakDto tRoutePeakDto) {

        // 线路信息不为空，新增
        if (null!=tRoutePeakDto.getLineDto()){
            if (!tLineService.save(tRoutePeakDto.getLineDto())){
                return  false;
            }
            tRoutePeakDto.setLineId(tRoutePeakDto.getLineDto().getId());
        }

        //有站点信息则新增
        if(null!=tRoutePeakDto.getViaList()&&tRoutePeakDto.getViaList().size()>0){
            tRoutePeakDto.getViaList().forEach(routeVia -> routeVia.setLineId(tRoutePeakDto.getLineId()));
            if (!tViaService.saveBatch(tRoutePeakDto.getViaList())){
                return false;
            }
        }
        //运行时间不为空，新增
        if(null!=tRoutePeakDto.getRoutePeakTimeRangeList()&&tRoutePeakDto.getRoutePeakTimeRangeList().size()>0) {
            tRoutePeakDto.getRoutePeakTimeRangeList().forEach(routeTime -> routeTime.setLineId(tRoutePeakDto.getLineId()));
            if (!tRoutePeakTimeRangeService.saveBatch(tRoutePeakDto.getRoutePeakTimeRangeList())){
                return  false;
            }
        }
        /* //注释原因，因高峰表改变，peak变化成line ，注意时间字段未搬移,需添加
       //新增高峰行程
        if (!this.save(tRoutePeakDto)){
            return  false;
        }*/
        return true;
    }

    /**
     * 修改高峰行程信息
     *
     * @param tRoutePeakDto 高峰行程信息
     * @return boolean
     */
    @Override
    public boolean updateRoutePeak(TRoutePeakDto tRoutePeakDto) {
        //查询是否有关联站点，删除重新构建
        if(null!=tRoutePeakDto.getViaList()&&tRoutePeakDto.getViaList().size()>0){
            QueryWrapper<TViaDto> queryWrapper = new QueryWrapper<>();
            //这一块需要调整，因表变化导致获取数据问题， 高峰时间段未曾搬移，后期添加需注意
            queryWrapper.eq("line_id", tRoutePeakDto.getLineDto().getId());
            if (tViaService.list(queryWrapper).size()>0){
                tViaService.remove(queryWrapper);
            }
            tRoutePeakDto.getViaList().forEach(routeVia -> routeVia.setLineId(tRoutePeakDto.getLineDto().getId()));
            if (!tViaService.saveBatch(tRoutePeakDto.getViaList())){
                return false;
            }
        }

        //是否有关联的高峰运行时间，存在删除新增
        if(null!=tRoutePeakDto.getRoutePeakTimeRangeList()&&tRoutePeakDto.getRoutePeakTimeRangeList().size()>0) {
            QueryWrapper<TRoutePeakTimeRangeDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("line_id", tRoutePeakDto.getLineDto().getId());
            if(tRoutePeakTimeRangeService.list(queryWrapper).size()>0){
                tRoutePeakTimeRangeService.remove(queryWrapper);
            }
            tRoutePeakDto.getRoutePeakTimeRangeList().forEach(routeTime -> routeTime.setLineId(tRoutePeakDto.getLineDto().getId()));
            if (!tRoutePeakTimeRangeService.saveBatch(tRoutePeakDto.getRoutePeakTimeRangeList())){
                return  false;
            }

        }
        tLineService.updateById(tRoutePeakDto.getLineDto());
        //2019/1/14 添加 ,因要求将站点缓存，故添加。测试需注意
        stationApiResource.loadPeakLineStations(null);
          /* //注释原因，因高峰表改变，peak变化成line ，注意时间字段未搬移,需添加
        //修改高峰信息
        if (!this.updateById(tRoutePeakDto)){
            return  false;
        }*/
        return true;
    }

    /**
     * 根据id 查询高峰详情
     *
     * @param id id
     * @return 高峰详情
     */
    @Override
    public TRoutePeakDto getRoutePeakId(String id) {
         /* //注释原因，因高峰表改变，peak变化成line ，注意时间字段未搬移,需添加
        //查询信息
        TRoutePeakDto tRoutePeakDto = this.getById(id);
        //是否具有关联信息
         if (null!=tRoutePeakDto.getLineId()&&!"".equals(tRoutePeakDto.getLineId())){
             //查询关联信息
            tRoutePeakDto.setLineDto(tLineService.getById(tRoutePeakDto.getLineId()));
             //查询线路关联运营时间段
             QueryWrapper<TRoutePeakTimeRangeDto> queryLineWrapper = new QueryWrapper<>();
             queryLineWrapper.eq("line_id", tRoutePeakDto.getLineId());
             tRoutePeakDto.setRoutePeakTimeRangeList(tRoutePeakTimeRangeService.list(queryLineWrapper));

             //查询线路关联运营时间段
             QueryWrapper<TViaDto> queryWrapper = new QueryWrapper<>();
             queryWrapper.eq("line_id", tRoutePeakDto.getLineId());
             tRoutePeakDto.setViaList(tViaService.list(queryWrapper));
         }*/
        TRoutePeakDto tRoutePeakDto =new TRoutePeakDto();
        if (StringUtil.isNotEmpty(id)){
            //查询关联信息
            tRoutePeakDto.setLineDto(tLineService.getById(id));
            //查询线路关联运营时间段
            QueryWrapper<TRoutePeakTimeRangeDto> queryLineWrapper = new QueryWrapper<>();
            queryLineWrapper.eq("line_id", id);
            tRoutePeakDto.setRoutePeakTimeRangeList(tRoutePeakTimeRangeService.list(queryLineWrapper));

            //查询线路关联站点
            QueryWrapper<TViaDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("line_id", id);
            tRoutePeakDto.setViaList(tViaService.list(queryWrapper));
    }
        return  tRoutePeakDto;
    }

    /**
     * 线路批量设置司机
     *
     * @param lineId  线路id
     * @param lineDriverBindDtoList 司机id
     * @return boolean
     */
    @Override
    public boolean distribution(String lineId, List<TLineDriverBindDto> lineDriverBindDtoList) {
        QueryWrapper<TLineDriverBindDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("line_id", lineId);
        if (tLineDriverBindService.list(queryWrapper).size()>0){
            //删除线路绑定的司机
            tLineDriverBindService.remove(queryWrapper);
        }
        if (lineDriverBindDtoList==null){
            return  true;
        }
        QueryWrapper<TLineDriverBindDto>  queryDriverWrapper =new QueryWrapper<>();
        List<String> list = new ArrayList<>();
        for (TLineDriverBindDto tLineDriverBindDto : lineDriverBindDtoList) {
            list.add(tLineDriverBindDto.getDriverId());
        }
        queryDriverWrapper.in("driver_id",list);
        if (tLineDriverBindService.list(queryDriverWrapper).size()>0){
            //删除司机绑定的线路
            tLineDriverBindService.remove(queryDriverWrapper);
        }
        lineDriverBindDtoList.forEach(tLineDriverBindDto -> tLineDriverBindDto.setLineId(lineId));
             //新增司机与线路关系
        if (tLineDriverBindService.saveBatch(lineDriverBindDtoList)){
            return true;
        }
        return false;
    }

    /**
     * pc 查询高峰列表
     * @param map  查询参数
     * @return 高峰列表
     */
    @Override
    public List<TRoutePeakDto> routePeakList(Map map) {
        List<TRoutePeakDto> routePeakDtoList = routePeakDao.routePeakList(map);
        if (routePeakDtoList.size()>0){
            //查询高峰时间段
            QueryWrapper<TSysParamDto> queryTimeWrapper = new QueryWrapper<>();
            queryTimeWrapper.eq("param_name", SysParamConstant.SYS_PEAK_TIME_RANGE);
            routePeakDtoList.forEach(routePeakDto->routePeakDto.setOperateTime(tSysParamService.getOne(queryTimeWrapper).getParamValue()));
            //查询高峰价格
            QueryWrapper<TSysParamDto> queryPriceWrapper = new QueryWrapper<>();
            queryTimeWrapper.eq("param_name", SysParamConstant.SYS_PEAK_PRICE);
            routePeakDtoList.forEach(routePeakDto->routePeakDto.setPrice(new BigDecimal(tSysParamService.getOne(queryPriceWrapper).getParamValue())));
            for (TRoutePeakDto tRoutePeakDto : routePeakDtoList) {
                tRoutePeakDto.setViaList(tViaService.getViaList(tRoutePeakDto.getLineId()));
            }
        }
        return routePeakDtoList;
    }

    /**
     * 根据行程id查询高峰行程
     */
    @Override
    public TRoutePeakDto getByRouteId(String routeId) {
        return routePeakDao.getByRouteId(routeId);
    }

    /**
     * 查询高峰线路信息
     * @param condition
     * @return
     */
    @Override
    public List<Map<String, Object>> queryRouteLineList(Map<String, Object> condition) {
        return routePeakDao.queryRouteLineList(condition);
    }

    /**
     * z注：目前只知道高峰有线路信息
     * 根据车辆id 查询线路
     *
     * @param carId 车辆id
     * @return 线路信息
     */
    @Override
    public TRoutePeakDto getByCarId(String carId) {
        return this.baseMapper.getByCarId(carId);
    }
}
