package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TLineDto;
import com.yihexinda.data.dto.TRoutePeakDto;
import com.yihexinda.dataservice.service.TLineService;
import com.yihexinda.dataservice.service.TRoutePeakService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 线路行程服务类
 * @author wenbn
 * @version 1.0
 * @date 2018/11/30 0030
 */
@RestController
@RequestMapping("/line/client")
@Slf4j
public class LineApiResource {

    @Autowired
    private TLineService tLineService;

    /**
     * wenbn
     * 添加线路信息
     * @param line
     * @return
     */
    @RequestMapping(value = "/addLine" ,method = RequestMethod.POST)
    public ResultVo addLine(@RequestBody TLineDto line) {
        boolean save = tLineService.save(line);
        if(save){
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }


    /**
     * wenbn
     * 删除线路信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteLine/{id}" ,method = RequestMethod.POST)
    public ResultVo deleteLine(@PathVariable("id") String id) {
        TLineDto old = tLineService.getById(id);
        if(null != old ){
            boolean remove = tLineService.removeById(id);
            if(remove){
                return ResultVo.success();
            }
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }


    /**
     * wenbn
     * 修改线路信息
     * @param line
     * @return
     */
    @RequestMapping(value = "/updateLine" ,method = RequestMethod.POST)
    public ResultVo updateLine(@RequestBody TLineDto line) {
        TLineDto oldline = tLineService.getById(line.getId());
        if(null != oldline) {
            boolean success = tLineService.saveOrUpdate(line);
            if (success) {
                return ResultVo.success();
            }
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * wenbn
     * 查询线路信息
     * @param
     * @return
     */
    @PostMapping(value = "/lineList")
    public ResultVo lineList(@RequestBody Map<String,Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")),1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")),10);
        QueryWrapper<TLineDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id" ,"1");
        ResultVo resultVo = new AbstractPageTemplate<TLineDto>() {
            @Override
            protected List<TLineDto> executeSql() {
                List<TLineDto> list = tLineService.list(queryWrapper);
                return list;
            }
        }.preparePageTemplate(pageIndex,pageSize);
        return resultVo;
    }
}
