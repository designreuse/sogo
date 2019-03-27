//package com.yihexinda.shenhang.scheduling.nodeweb.service;
//
//import com.yihexinda.shenhang.scheduling.core.Execption.BussException;
//import com.yihexinda.shenhang.scheduling.core.dto.Json;
//import com.yihexinda.shenhang.scheduling.core.dto.Page;
//import com.yihexinda.shenhang.scheduling.data.dto.OrderRecordAllPageDto;
//import com.yihexinda.shenhang.scheduling.data.dto.OrderRecordCountWayDto;
//import com.yihexinda.shenhang.scheduling.data.dto.OrderRecordDto;
//import com.yihexinda.shenhang.scheduling.data.dto.OrderRecordQueryDto;
//import DataOrderRecordClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * @author: yuanbailin
// * @create: 2018-11-07 14:12
// **/
//@Service
//public class OrderRecordService {
//
//    @Autowired
//    private DataOrderRecordClient orderRecordClient;
//
//    public List<OrderRecordCountWayDto> excelExportCountWayByCondition(OrderRecordQueryDto queryDto) {
//        return orderRecordClient.excelExportCountWayByCondition(queryDto);
//    }
//
//    public Page<OrderRecordDto> findGrabbedByCondition(OrderRecordQueryDto orderRecordQueryDto) {
//        return orderRecordClient.findGrabbedByCondition(orderRecordQueryDto);
//    }
//
//    /**
//     * 总订单量/已完成订单数据分页查询
//     *
//     * @param orderRecordQueryDto
//     * @return
//     */
//    public Page<OrderRecordAllPageDto> selectOrdReAllPage(OrderRecordQueryDto orderRecordQueryDto) {
//        return orderRecordClient.selectOrdReAllPage(orderRecordQueryDto);
//    }
//
//    /**
//     * 根据条件查询所有未完成订单总量,并作分页处理
//     *
//     * @param orderRecordQueryDto
//     * @return
//     */
//    public Page<OrderRecordAllPageDto> selectUnfinishedOrder(OrderRecordQueryDto orderRecordQueryDto) {
//        return orderRecordClient.selectUnfinishedOrder(orderRecordQueryDto);
//    }
//
//}
