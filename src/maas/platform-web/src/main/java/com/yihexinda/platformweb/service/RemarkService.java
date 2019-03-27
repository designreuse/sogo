package com.yihexinda.platformweb.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yihexinda.auth.domain.User;
import com.yihexinda.core.dto.Json;
import com.yihexinda.data.dto.RemarkDto;
import com.yihexinda.platformweb.client.DataRemarkClient;
import com.yihexinda.platformweb.security.SecurityHelper;

/**
 * @author: yuanbailin
 * @create: 2018-10-26 15:48
 **/
@Service
public class RemarkService {

    @Autowired
    private DataRemarkClient remarkClient;
    @Autowired
    private AuthService authService;
    @Autowired
    private SecurityHelper securityHelper;
//
//    public List<RemarkDto> findRemark(String ableType, String ableId) {
//        RemarkDto remarkDto = new RemarkDto();
//        remarkDto.setAbleType(ableType);
//        remarkDto.setAbleId(ableId);
//        return remarkClient.findRemark(remarkDto);
//    }
//
//    public Json addRemark(RemarkDto remarkDto) {
//        Json json = new Json();
//        User user = securityHelper.getCurrentUser();
//        remarkDto.setUserId(user.getId().intValue());
//        remarkDto.setAbleType("Order");
//        remarkDto.setUserName(user.getUsername());
//        remarkDto.setRemarkDate(new Date());
//        remarkClient.addRemark(remarkDto);
//        return json;
//    }

}
