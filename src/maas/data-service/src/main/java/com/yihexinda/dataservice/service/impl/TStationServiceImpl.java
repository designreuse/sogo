package com.yihexinda.dataservice.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TStationDto;
import com.yihexinda.dataservice.dao.TStationDao;
import com.yihexinda.dataservice.service.TStationService;
import org.springframework.stereotype.Service;

import javax.management.ServiceNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 站点信息表 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class TStationServiceImpl extends ServiceImpl<TStationDao, TStationDto> implements TStationService {

    /**
     * 根据站点ids查询站点信息
     * @param ids
     * @return
     */
    @Override
    public List<Map<String, Object>> queryStationByIds(List<String> ids) {
        //查询站点坐标
        List<Map<String, Object>> stationList = this.listMaps(new QueryWrapper<TStationDto>()
                .select("id","longitude","latitude")
                .in("id",ids)
        );
        return stationList;
    }


    /**
     * @author chenzeqi
     * @Date 2018-12-27
     * 高峰的加载所有的站点 和 线路信息
     * @return
     */
    @Override
    public ResultVo loadPeakLineStations(Map<String, Object> data) {
        if(data == null){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        List<Map<String,Object>> stationDtoList = this.baseMapper.loadPeakLineStations(data);
        ResultVo resultVo = new ResultVo();
        resultVo.setDataSet(stationDtoList);
        return resultVo;
    }

}
