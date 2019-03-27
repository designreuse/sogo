package com.yihexinda.platformweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.yihexinda.data.dto.OperateLogCreateDto;
//import com.yihexinda.data.dto.OperateLogDto;
//import com.yihexinda.data.dto.OperateLogQueryDto;
//import com.yihexinda.data.enums.OperateTypeEnum;
import com.yihexinda.platformweb.client.DataOperateLogClient;

/**
 * @author Jack
 * @date 2018/10/24.
 */
@Service
public class OperateLogService {
    @Autowired
private DataOperateLogClient operateLogClient;

//    public List<OperateLogDto> findByAble(String ableType, String ableId) {
//        OperateLogQueryDto queryDto = new OperateLogQueryDto();
//        queryDto.setAbleType(ableType);
//        queryDto.setAbleId(ableId);
//        return operateLogClient.findByAble(queryDto);
//    }
//
//    public void addOperateLog(Long userId, String ableType, String ableId, OperateTypeEnum operateType, String remark) {
//        OperateLogCreateDto createDto = new OperateLogCreateDto();
//        createDto.setUserId(userId);
//        createDto.setAbleType(ableType);
//        createDto.setAbleId(ableId);
//        createDto.setOperateType(operateType);
//        createDto.setRemark(remark);
//        operateLogClient.add(createDto);
//    }
}
