package com.yihexinda.platformweb.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.core.dto.Json;
import com.yihexinda.data.dto.UserOnlineDto;
import com.yihexinda.platformweb.client.IndexPageStatisticsClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by wxf on 2018/11/23.
 */

@RestController
@RequestMapping("/api/IndexPageStatistics")
@Slf4j
@Api(description = "首页统计列表")
public class IndexPageStatisticsResource {

    @Autowired
    private IndexPageStatisticsClient indexPageStatisticsClient;

    /**
     * 首页用户在线统计
     * @return
     */
    @ApiOperation(value = "首页用户在线统计", httpMethod = "POST")
    @PostMapping("/userOnline")
    public Json<UserOnlineDto> userOnline(){
        Json json = new Json();
        try {

            json.setSuccess(true);
            json.setObj(indexPageStatisticsClient.UserOnline());
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg(e.getMessage());
            log.error("list {}", e);
        }
        return json;
    }
}