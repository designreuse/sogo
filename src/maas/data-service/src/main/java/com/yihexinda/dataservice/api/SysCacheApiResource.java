package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TLineDto;
import com.yihexinda.data.dto.TSysParamDto;
import com.yihexinda.dataservice.service.TLineService;
import com.yihexinda.dataservice.service.TSysParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/3 0003
 */
@RestController
@RequestMapping("/sysCache/client")
@Slf4j
public class SysCacheApiResource {

    @Autowired
    private TSysParamService tSysParamService;

    /**
     * 系统配置
     * @return
     */
    @PostMapping(value = "/sysParamList")
    ResultVo sysParamList(){
        QueryWrapper<TSysParamDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id" ,"1");
        List<TSysParamDto> list = tSysParamService.list(queryWrapper);
        return ResultVo.success().setDataSet(list);

    }
}
