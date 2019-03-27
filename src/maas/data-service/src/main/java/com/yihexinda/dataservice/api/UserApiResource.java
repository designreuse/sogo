package com.yihexinda.dataservice.api;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.yihexinda.auth.domain.User;
import com.yihexinda.core.utils.*;
import com.yihexinda.core.vo.HeaderVo;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.dataservice.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TUserDto;
import com.yihexinda.dataservice.service.TUserService;

import javax.annotation.Resource;

/**
 * 
 * @author zhanglei
 *
 */
@RestController
@RequestMapping("/user/client")
public class UserApiResource {

	@Autowired
	private TUserService tuserService;

	@Resource
	public RedisUtil redisUtil;


	/**
	 * 解密用户信息
	 * @param paramter
	 * @return
	 */
	@PostMapping("/decryptionUserInfo")
	public ResultVo decryptionUserInfo(@RequestBody Map<String,String> paramter) {
        return addUser(paramter);
	}

    /**
     * 用户登录
     * @param paramter
     * @return
     */
	@PostMapping("/loginUser")
    public ResultVo loginUser(@RequestBody Map<String,String> paramter){
        return addUser(paramter);
    }

	/**
	 * 用户添加
	 * @param paramter
	 * @return
	 */
	private  ResultVo addUser(@RequestBody Map<String,String> paramter) {
		TUserDto user =  checkUserExist(paramter);
		if(StringUtil.isEmpty(paramter.get("openId"))){
			return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
		}else if(user != null){
			return createToken(user);
		}
        TUserDto userDto = new TUserDto();

		  userDto.setAppId(paramter.get("appId"));
		  userDto.setOpenId(paramter.get("openId"));
		  userDto.setPhone(paramter.get("phone"));
		  userDto.setNick(paramter.get("nick"));
		  userDto.setGender(paramter.get("gender"));
		  userDto.setAvatarurl(paramter.get("avatarurl"));
		  userDto.setCity( paramter.get("city"));
		  userDto.setProvince(paramter.get("province"));
		  userDto.setCountry( paramter.get("country"));
		  userDto.setLanguage(paramter.get("language"));


        userDto.setCreateDate(new Date());

		if (tuserService.save(userDto)) {
			return createToken(userDto);
		}
		return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
	}

	private ResultVo createToken(@RequestBody TUserDto userDto) {
		PayLoadVo payLoadVo = new PayLoadVo();
		HeaderVo headerVo = new HeaderVo();
		Date date = new Date();
		payLoadVo.setUserId(userDto.getId());
		payLoadVo.setExp(date);
		payLoadVo.setNick(userDto.getNick());
		payLoadVo.setPhone(userDto.getPhone());
		payLoadVo.setLoginLogo("userLogo");
		String content = EncryptionUtil.getBase64(JsonUtil.toJson(headerVo)).replaceAll("\r|\n", "") + "." + EncryptionUtil.getBase64(new Gson().toJson(payLoadVo)).replaceAll("\r|\n", "");
		String signature = EncryptionUtil.getSHA256StrJava(ResultConstant.BUS_TAG_TOKEN + content);
		redisUtil.set(userDto.getId(),signature,(long)60*60*24*7);
		return ResultVo.success().setToken(content + "." + EncryptionUtil.getBase64(signature).replaceAll("\r|\n", ""));
	}


	/**
	 * 用户查询
	 * @param condition
	 * @return
	 */
	@PostMapping("/userList")
	public ResultVo userList(@RequestBody Map<String, Object> condition) {
		int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")), 1);
		int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")), 10);
		QueryWrapper<TUserDto> queryWrapper = new QueryWrapper<>();
		//名称查询
		if (!"".equals(StringUtil.trim(condition.get("nick")))){
			queryWrapper.like("nick",StringUtil.trim(condition.get("nick")));
		}
		//性别查询
		if (!"".equals(StringUtil.trim(condition.get("gender")))){
			queryWrapper.eq("gender",StringUtil.trim(condition.get("gender")));
		}
		//电话号码查询
		if (!"".equals(StringUtil.trim(condition.get("phone")))){
			queryWrapper.like("phone",StringUtil.trim(condition.get("phone")));
		}
		//开始时间与结束时间不为空
		if (!"".equals(StringUtil.trim(condition.get("startTime")))&&!"".equals(condition.get("endTime"))){
			Timestamp aStartTime = Timestamp.valueOf(StringUtil.trim(condition.get("startTime")));
			String endTime = StringUtil.trim(condition.get("endTime")).replace("00:00:00","23:59:59");
			Timestamp aEndTime = Timestamp.valueOf(endTime);
			queryWrapper.between("create_date",aStartTime,aEndTime);
		}
		queryWrapper.orderByDesc("create_date");
		ResultVo resultVo = new AbstractPageTemplate<TUserDto>() {
			@Override
			protected List<TUserDto> executeSql() {
				List<TUserDto> list = tuserService.list(queryWrapper);
				return list;
			}
		}.preparePageTemplate(pageIndex, pageSize);
		return resultVo;
	}

	/**
	 * 校验授权
	 * @param paramter map参数
	 * @return
	 */
	@PostMapping("/checkUserExist")
	public TUserDto checkUserExist(@RequestBody  Map<String,String> paramter){
		QueryWrapper<TUserDto> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("open_id" ,StringUtil.trim(paramter.get("openId")));
		return  tuserService.getOne(queryWrapper);
	}
	/**
	 *
	 * 用户个人资料
	 * @param userId 用户id
	 * @return 个人资料
	 */
	@GetMapping("/getUserInfo/{userId}")
	public ResultVo userInfo(@PathVariable String userId) {
		TUserDto tUserDto = tuserService.getById(userId);
        if (null==tUserDto){
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,ResultConstant.SYS_REQUIRED_DATA_ERROR_VALUE);
        }
        tUserDto.setAppId("");
        tUserDto.setPassword("");
		return ResultVo.success().setDataSet(tUserDto);
	}


	@RequestMapping(value = "/checkToken",method = RequestMethod.POST)
	ResultVo checkToken(@RequestBody Map<String, String> paramter){
		QueryWrapper<TUserDto> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("open_id" ,StringUtil.trim(paramter.get("openId")));
		TUserDto one = tuserService.getOne(queryWrapper);
		if(null == one){
			return ResultVo.error(ResultConstant.SYS_REQUIRED_UNLOGIN_ERROR,ResultConstant.SYS_REQUIRED_UNLOGIN_ERROR_VALUE);
		}
		return createToken(one);
	}


	/**
	 * 用户列表导出
	 * @return 用户列表
	 */
	@GetMapping("/getUserListExcel")
	public List<TUserDto> getUserListExcel() {
		return 	tuserService.list();
	}
	/**
	 * 用户修改
	 * @param paramter
	 * @return
	 */
	@PostMapping("/updateUserInfo")
	private  ResultVo updateUserInfo(@RequestBody Map<String,String> paramter) {


		QueryWrapper<TUserDto> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("phone" ,StringUtil.trim(paramter.get("phone")));
		TUserDto one = tuserService.getOne(queryWrapper);

		TUserDto userDto = new TUserDto();
		userDto.setId(one.getId());
		userDto.setRealName(paramter.get("realName"));
		userDto.setCompName(paramter.get("compName"));
		userDto.setDeptName(paramter.get("deptName"));

		userDto.setUpdateDate(new Date());

		if(tuserService.updateById(userDto)){
			return ResultVo.success();
		}else{
			return ResultVo.error(ResultConstant.HTTP_STATUS_BAD_REQUEST,ResultConstant.HTTP_STATUS_BAD_REQUEST_VALUE);
		}



	}

}
