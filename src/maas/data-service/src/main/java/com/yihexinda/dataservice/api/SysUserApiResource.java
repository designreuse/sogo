package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.EncryptionUtil;
import com.yihexinda.core.utils.JsonUtil;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.HeaderVo;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TLineDto;
import com.yihexinda.data.dto.TSysRoleDto;
import com.yihexinda.data.dto.TSysRoleUserDto;
import com.yihexinda.data.dto.TSysUserDto;
import com.yihexinda.dataservice.service.TSysRoleService;
import com.yihexinda.dataservice.service.TSysRoleUserService;
import com.yihexinda.dataservice.service.TSysUserService;
import com.yihexinda.dataservice.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author pengfeng
 * @version 1.0
 * @date 2018/11/30
 */
@RestController
@RequestMapping("/sysUser/client")
@Slf4j
public class SysUserApiResource {
    /**
     * 系统用户服务
     */
    @Autowired
    private TSysUserService tSysUserService;

    /**
     * 角色用户服务
     */
    @Autowired
    private TSysRoleUserService tSysRoleUserService;

    /**
     * 角色服务
     */
    @Autowired
    private TSysRoleService tSysRoleService;

    @Autowired
    private RedisUtil redisUtil;
    /**
     *  用户登录
     * @param paramter map参数
     * @return
     */
    @PostMapping("/loginUser")
    public ResultVo loginUser(@RequestBody Map<String,String> paramter){
        if(StringUtil.isEmpty( paramter.get("userName")) || StringUtil.isEmpty(paramter.get("password"))){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
        }
        //校验用户名/密码
        QueryWrapper<TSysUserDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", paramter.get("userName")).eq("password",paramter.get("password"));
        TSysUserDto sysUser = tSysUserService.getOne(queryWrapper);
        if(sysUser != null){
            return createToken(sysUser);
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, "用户名或密码错误");
    }

    /**
     * 退出登录
     * @param paramter
     * @return
     */
    @PostMapping(value = "/loginout")
    ResultVo loginout(@RequestBody Map<String, String> paramter){
        String token = paramter.get("token");
        if(StringUtil.isEmpty(token)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        PayLoadVo payLoadVo = RequestUtil.analysisToken(token);
        if(payLoadVo!= null){
            redisUtil.remove(payLoadVo.getUserId());
        }
        return ResultVo.success();
    }

    /**
     * 创建token
     * @param sysUserDto
     * @return
     */
    private ResultVo createToken(@RequestBody TSysUserDto sysUserDto) {
        PayLoadVo payLoadVo = new PayLoadVo();
        HeaderVo headerVo = new HeaderVo();
        Date date = new Date();
        payLoadVo.setUserId(sysUserDto.getId());
        payLoadVo.setExp(date);
        payLoadVo.setNick(sysUserDto.getUsername());
        payLoadVo.setPhone(sysUserDto.getPhone());
        payLoadVo.setLoginLogo("sysUserLogo");
        String content = EncryptionUtil.getBase64(JsonUtil.toJson(headerVo)).replaceAll("\r|\n", "") + "." + EncryptionUtil.getBase64(new Gson().toJson(payLoadVo)).replaceAll("\r|\n", "");
        String signature = EncryptionUtil.getSHA256StrJava(ResultConstant.BUS_TAG_TOKEN + content);
        redisUtil.set(sysUserDto.getId(),signature,(long)60*60*24*7);
        return ResultVo.success().setToken(content + "." + EncryptionUtil.getBase64(signature).replaceAll("\r|\n", ""));
    }

    /**
     * 获取用户信息列表
     * @param condition  参数
     * @return 用户信息列表
     */
    @PostMapping("/getUserList")
    public ResultVo getUserList(@RequestBody Map<String,Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")),1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")),10);
        ResultVo resultVo = new AbstractPageTemplate<TSysUserDto>() {
            @Override
            protected List<TSysUserDto> executeSql() {
                List<TSysUserDto> list = tSysUserService.list();
                if (list.size()>0) {
                    list.forEach(userDto -> userDto.setPassword(""));
                    for (TSysUserDto tSysUserDto : list) {
                        QueryWrapper<TSysRoleUserDto> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("user_id", tSysUserDto.getId());
                        List<TSysRoleUserDto> sysRoleUserDtoList = tSysRoleUserService.list(queryWrapper);
                        for (TSysRoleUserDto tSysRoleUserDto : sysRoleUserDtoList) {
                            tSysRoleUserDto.setRoleName(tSysRoleService.getById(tSysRoleUserDto.getRoleId()).getFname());
                        }
                        tSysUserDto.setRoleUserDtoList(sysRoleUserDtoList);
                    }
                }
                return list;
            }
        }.preparePageTemplate(pageIndex,pageSize);
        return resultVo;
        //return ResultVo.success().setDataSet(tSysUserService.list());
    }


    /**
     * pengfeng
     * 添加用户
     *
     * @param tSysUserDto 用户信息信息
     * @return ResultVo
     */
    @PostMapping("addUser")
    @Transactional(rollbackFor = Exception.class)
    public ResultVo addUser(@RequestBody TSysUserDto tSysUserDto) {
        if (tSysUserService.save(tSysUserDto)) {
            if (null!=tSysUserDto.getRoleUserDtoList()&&tSysUserDto.getRoleUserDtoList().size()>0){
                tSysUserDto.getRoleUserDtoList().forEach(userDto -> userDto.setUserId(tSysUserDto.getId()));
                //添加角色列表
                if (!tSysRoleUserService.saveBatch(tSysUserDto.getRoleUserDtoList())) {
                    return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"系统错误，请联系管理员");
                }

            }
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);

    }

    /**
     * 修改用户信息
     *
     * @param tSysUserDto 用户信息
     * @return ResultVo
     */
    @PutMapping("updateUser")
    @Transactional(rollbackFor = Exception.class)
    public ResultVo updateUser(@RequestBody TSysUserDto tSysUserDto) {

            if (tSysUserService.updateById(tSysUserDto)) {
                if (null!=tSysUserDto.getRoleUserDtoList()&&tSysUserDto.getRoleUserDtoList().size()>0){
                    QueryWrapper<TSysRoleUserDto> queryWrapper =new QueryWrapper<>();
                    queryWrapper.eq("user_id",tSysUserDto.getId());
                    tSysRoleUserService.remove(queryWrapper);
                    tSysUserDto.getRoleUserDtoList().forEach(userDto -> userDto.setUserId(tSysUserDto.getId()));
                    if (!tSysRoleUserService.saveBatch(tSysUserDto.getRoleUserDtoList())){
                        return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"系统错误，请联系管理员");
                    }
                }
                return ResultVo.success();
            }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 获取用户信息详情
     *
     * @return 用户信息详情
     */
    @GetMapping("/getUser/{id}")
    public ResultVo getUser(@PathVariable String id) {
        TSysUserDto tSysUserDto = tSysUserService.getById(id);
        if (null!=tSysUserDto){
            tSysUserDto.setPassword("");
            return ResultVo.success().setDataSet(tSysUserDto);
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 获取角色下用户
     *
     * @return 用户列表
     */
    @GetMapping("/getRoleUser/{roleId}")
    public ResultVo getRoleUser(@PathVariable String roleId) {
        QueryWrapper<TSysRoleUserDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        return ResultVo.success().setDataSet(tSysRoleUserService.list(queryWrapper));
    }

}
