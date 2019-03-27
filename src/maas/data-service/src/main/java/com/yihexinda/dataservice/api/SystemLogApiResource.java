package com.yihexinda.dataservice.api;

import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TSystemLogDto;
import com.yihexinda.dataservice.service.TSystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2019/3/13 0013
 */
@RestController
@RequestMapping("/syslog/client")
public class SystemLogApiResource {

    @Autowired
    private TSystemLogService logService;

    /**
     *保存日志
     * @param condition
     * @return
     */
    @RequestMapping(value = "/save" ,method = RequestMethod.POST)
    ResultVo save(@RequestBody Map<String,Object> condition){
        String user_id = StringUtil.trim(condition.get("user_id"));
        String title_name = StringUtil.trim(condition.get("title_name"));
        String ip_address = StringUtil.trim(condition.get("ip_address"));
        String module = StringUtil.trim(condition.get("module"));
        String sub_module = StringUtil.trim(condition.get("sub_module"));
        String operation = StringUtil.trim(condition.get("operation"));
        String remarks = StringUtil.trim(condition.get("remarks"));
        TSystemLogDto log = new TSystemLogDto();
        log.setUserId(user_id);
        log.setTitleName(title_name);
        log.setIpAddress(ip_address);
        log.setModule(module);
        log.setSubModule(sub_module);
        log.setOperation(operation);
        log.setRemarks(remarks);

        boolean save = logService.save(log);
        if(save){
            return ResultVo.success();
        }
        return ResultVo.error(ResultVo.Status.SYS_REQUIRED_FAILURE);
    }
}
