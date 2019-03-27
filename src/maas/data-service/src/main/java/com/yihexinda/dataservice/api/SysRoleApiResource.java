package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TSysRoleDto;
import com.yihexinda.data.dto.TSysRoleUserDto;
import com.yihexinda.data.dto.TSysUserDto;
import com.yihexinda.dataservice.service.TSysRoleService;
import com.yihexinda.dataservice.service.TSysRoleUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author pengfeng
 * @version 1.0
 * @date 2018/11/30
 */
@RestController
@RequestMapping("/sysRole/client")
@Slf4j
public class SysRoleApiResource {

    @Autowired
    private TSysRoleService tSysRoleService;

    @Autowired
    private TSysRoleUserService tSysRoleUserService;

    /**
     * 获取角色信息列表
     *
     * @return 角色信息列表
     */
    @PostMapping("/getRoleList")
    public ResultVo getRoleList(@RequestBody Map<String,Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")),1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")),10);
        ResultVo resultVo = new AbstractPageTemplate<TSysRoleDto>() {
            @Override
            protected List<TSysRoleDto> executeSql() {
                List<TSysRoleDto> list = tSysRoleService.list();
                return list;
            }
        }.preparePageTemplate(pageIndex,pageSize);
        return resultVo;
       // return ResultVo.success().setDataSet(tSysRoleService.list());
    }


    /**
     * pengfeng
     * 添加角色
     *
     * @param tSysRoleDto 角色信息
     * @return ResultVo
     */
    @PostMapping("addRole")
    public ResultVo addRole(@RequestBody TSysRoleDto tSysRoleDto) {

        if (tSysRoleService.save(tSysRoleDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);

    }

    /**
     * 修改角色信息
     *
     * @param tSysRoleDto 角色信息
     * @return ResultVo
     */
    @PutMapping("updateRole")
    public ResultVo updateRole(@RequestBody TSysRoleDto tSysRoleDto) {
            if (tSysRoleService.updateById(tSysRoleDto)) {
                return ResultVo.success();
            }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 获取角色信息详情
     *
     * @return 角色详情
     */
    @GetMapping("/getRole/{id}")
    public ResultVo getRole(@PathVariable String id) {
        TSysRoleDto tSysRoleDto = tSysRoleService.getById(id);
        if (null!=tSysRoleDto){
            return ResultVo.success().setDataSet(tSysRoleDto);
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 获取用户角色信息
     *
     * @return 用户拥有角色
     */
    @GetMapping("/getUserRole/{userId}")
    public ResultVo getUserRole(@PathVariable String userId) {
        QueryWrapper<TSysRoleUserDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return ResultVo.success().setDataSet(tSysRoleUserService.list(queryWrapper));
    }

}
