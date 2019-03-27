package com.yihexinda.nodeweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yihexinda.data.dto.OperateLogCreateDto;
import com.yihexinda.data.dto.OperateLogDto;
import com.yihexinda.data.dto.OperateLogQueryDto;
import com.yihexinda.data.enums.OperateTypeEnum;
import com.yihexinda.nodeweb.client.DataOperateLogClient;
import com.yihexinda.nodeweb.security.SecurityHelper;

/**
 * @author Jack
 * @date 2018/10/24.
 */
@Service
public class OperateLogService {
    @Autowired
    private DataOperateLogClient operateLogClient;

    @Autowired
    private SecurityHelper securityHelper;

    public List<OperateLogDto> findByAble(String ableType, String ableId) {
        OperateLogQueryDto queryDto = new OperateLogQueryDto();
        queryDto.setAbleType(ableType);
        queryDto.setAbleId(ableId);
        queryDto.setUserId(securityHelper.getCurrentUser().getId().intValue());
        return operateLogClient.findByAbleAndUserId(queryDto);
    }

    public void addOperateLog(Long userId, String ableType, String ableId, OperateTypeEnum operateType, String remark) {
        OperateLogCreateDto createDto = new OperateLogCreateDto();
        createDto.setUserId(userId);
        createDto.setAbleType(ableType);
        createDto.setAbleId(ableId);
        createDto.setOperateType(operateType);
        createDto.setRemark(remark);
        operateLogClient.add(createDto);
    }
}
