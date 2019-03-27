package com.yihexinda.dataservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.data.dto.TDriverCarBindDto;
import com.yihexinda.data.dto.TRoutePeakDto;
import com.yihexinda.dataservice.dao.RouteCountDao;
import com.yihexinda.dataservice.dao.TDriverCarBindDao;
import com.yihexinda.dataservice.service.RouteCountService;
import com.yihexinda.dataservice.service.TDriverCarBindService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工时段统计
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class RouteCountServiceImpl implements RouteCountService {

@Resource
private RouteCountDao routeCountDao;

    /**
     * 历史工时段统计
     *
     * @return
     */
    @Override
    public List<Map> getListCount(Map map) {
        return routeCountDao.getListCount(map);
    }

    /**
     * 当天工时段统计
     *
     * @return
     */
    @Override
    public List<Map> getRouteStation(Map map) {
        return routeCountDao.getRouteStation(map);
    }
}
