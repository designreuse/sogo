package com.yihexinda.platformweb.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yihexinda.core.dto.Page;
import com.yihexinda.data.dto.OrderRecordAllPageDto;
import com.yihexinda.data.dto.OrderRecordCountWayDto;
import com.yihexinda.data.dto.OrderRecordQueryDto;
import com.yihexinda.data.dto.OrderRecordTotalByTypeDto;

/**
 * Created by wxf on 2018-11-13.
 * 订单记录数据接口
 */
@FeignClient(value = "data-service")
@RequestMapping("/orderRecord")
public interface DataOrderRecordClient {
    /**
     * 查询抢单/派单/退回/抽回/暂存统计列表订单记录数据
     * @param orderRecordQueryDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/findGrabbedByCondition")
    @ResponseBody
    Page<OrderRecordAllPageDto> findGrabbedByCondition(OrderRecordQueryDto orderRecordQueryDto);


    /**
     * 统计订单
     *
     * @param queryDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/excelExportCountWayByCondition")
    List<OrderRecordCountWayDto> excelExportCountWayByCondition(OrderRecordQueryDto queryDto);

    /**
     *查询总订单量分页列表
     * @param orderRecordQueryDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/selectOrdReAllPage")
    Page<OrderRecordAllPageDto> selectOrdReAllPage(OrderRecordQueryDto orderRecordQueryDto);


    /**
     * 查询已完成订单量分页列表
     * @param orderRecordQueryDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST ,value = "/selectCompletePage")
    Page<OrderRecordAllPageDto> selectCompletePage(OrderRecordQueryDto orderRecordQueryDto);

    /**
     * 查询未完成订单分页列表
     *
     * @param orderRecordQueryDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST ,value = "/selectUnfinishedOrder")
    Page<OrderRecordAllPageDto> selectUnfinishedOrder(OrderRecordQueryDto orderRecordQueryDto);

    /**
     * 首页调度单统计
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/selectOrderRecordTotalByType")
     List<OrderRecordTotalByTypeDto> selectOrderRecordTotalByType(Integer id);
}

