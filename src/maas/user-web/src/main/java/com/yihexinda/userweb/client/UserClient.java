package com.yihexinda.userweb.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TUserDto;

/**
 * @author zhanglei
 *
 */
@FeignClient(value = "data-service")
@RequestMapping("/user/client")
public interface UserClient {


	/**
	 * 校验用户授权
	 * @param paramter
	 * @return
	 */
	@RequestMapping(value = "/checkUserExist",method = RequestMethod.POST)
	TUserDto checkUserExist(@RequestBody  Map<String,String> paramter);
	
	
	/**
	 * 解密用户信息
	 * @param paramter
	 * @return
	 */
	@RequestMapping(value = "/decryptionUserInfo",method = RequestMethod.POST)
	ResultVo decryptionUserInfo(@RequestBody Map<String,String> paramter);


	/**
	 * 用户登录
	 * @param paramter
	 * @return
	 */
	@RequestMapping(value = "/loginUser",method = RequestMethod.POST)
	 ResultVo loginUser(@RequestBody Map<String,String> paramter);



	/**
	 * 校验token
	 * @param paramter
	 * @return
	 */
	@RequestMapping(value = "/checkToken",method = RequestMethod.POST)
	ResultVo checkToken(@RequestBody Map<String, String> paramter);

	/**
	 *  查询用户详情
	 * @param userId 用户id
	 * @return 用户详情
	 */
	@RequestMapping(value = "/getUserInfo/{userId}",method = RequestMethod.GET)
	ResultVo getUserInfo(@PathVariable("userId") String userId);
	/**
	 *  用户修改
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
	ResultVo updateUserInfo(@RequestBody Map<String,String> paramter) ;

}
