package com.yihexinda.userweb.filter;

import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.utils.HttpClientUtil;
import com.yihexinda.core.utils.HttpClinetPostUtil;
import com.yihexinda.userweb.api.IndexManagerResource;
import com.yihexinda.userweb.client.RegionClient;
import com.yihexinda.userweb.client.StationClient;
import com.yihexinda.userweb.client.SysParamClient;
import com.yihexinda.userweb.interceptor.MyRequestWrapper;
import com.yihexinda.userweb.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/4 0004
 */
@WebFilter(filterName = "HttpServletRequestWrapperFilter", urlPatterns = "/api/*")
public class HttpServletRequestWrapperFilter implements Filter {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RegionClient regionClient;
    @Autowired
    private SysParamClient sysParamClient;
    @Autowired
    private StationClient stationClient;
    @Autowired
    private IndexManagerResource indexManagerResource;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        checkCache();
        if (request instanceof HttpServletRequest) {
            requestWrapper = new MyRequestWrapper((HttpServletRequest) request);
        }
        if (null == requestWrapper) {
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(requestWrapper, response);
        }
    }

    private void checkCache() {
        //地区
        if (!redisUtil.exists(RedisConstant.SYS_RESIONS_CACHE_KEY)) {
            regionClient.regionList();
        }
        //站点
        if (!redisUtil.exists(RedisConstant.SYS_STATIONS_CACHE_KEY)) {
            stationClient.loadStations(new HashMap<>());
        }
        //平高峰票价及高峰时间段信息
        if (!redisUtil.exists(RedisConstant.SYS_PEAK_PRICE)||!redisUtil.exists(RedisConstant.SYS_OFFPEAK_PRICE)||!redisUtil.exists(RedisConstant.SYS_PEAK_TIME_RANGE)) {
            sysParamClient.getTicketPrice();
        }
        //缓存预约
        if (!redisUtil.exists(RedisConstant.SYS_APPOINTMENT_TIME_KEY)) {
            indexManagerResource.loadAppoTime();
        }
        //高峰途经站点
        if (!redisUtil.exists(RedisConstant.SYS_PEAK_VIA_STATIONS_CACHE_KEY)) {
            stationClient.loadPeakViaStations(new HashMap<>());
        }


    }

    @Override
    public void destroy() {

    }
}
