package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TSmsLogDto;
import com.yihexinda.dataservice.service.TSmsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/smsLog/client")
public class SmsLogApiResource {

    @Autowired
    private TSmsLogService tsmsLogService;

    /**
     *  发送短信
     * @param smsLogDto
     * @return
     */
     @PostMapping("/addSmsLog")
     public ResultVo addSmsLog(@RequestBody TSmsLogDto smsLogDto){
         if (tsmsLogService.save(smsLogDto)) {
             return ResultVo.success();
         }
         return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
     }

    /**
     * 删除短信
     * @param smsLogId
     * @return
     */
    @GetMapping("/deleteSmsLog/{id}")
    public ResultVo deleteSmsLog(@RequestParam("smsLogId") int smsLogId) {
        TSmsLogDto smsLogDto = tsmsLogService.getById(smsLogId);
        if (smsLogDto != null) {
            if (tsmsLogService.removeById(smsLogId)) {
                return ResultVo.success();
            }
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 查询短信
     * @param condition
     * @return
     */
    @PostMapping("/smsLogList")
    public ResultVo smsLogList(@RequestBody Map<String, Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")), 1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")), 10);
        QueryWrapper<TSmsLogDto> queryWrapper = new QueryWrapper<>();
        ResultVo resultVo = new AbstractPageTemplate<TSmsLogDto>() {
            @Override
            protected List<TSmsLogDto> executeSql() {
                List<TSmsLogDto> list = tsmsLogService.list(queryWrapper);
                return list;
            }
        }.preparePageTemplate(pageIndex, pageSize);
        return resultVo;
    }

}
