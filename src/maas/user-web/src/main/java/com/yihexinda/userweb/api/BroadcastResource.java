package com.yihexinda.userweb.api;

import java.util.Map;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TBroadcastDto;
import com.yihexinda.userweb.client.BroadcastClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author zl
 * @date 2018/10/12.
 */
@Api(description = "广播接口")
@RestController()
@RequestMapping("/api/broadcast")
public class BroadcastResource {

	@Autowired
	private BroadcastClient tbroadcastClient;

	/**
	 * 显示广播
	 * @return
	 */
	@ApiOperation(value = "显示广播", httpMethod = "GET")
	@RequestMapping(value = "/getBroadcastList")
	public ResultVo getBroadcastList() {
		return tbroadcastClient.getBroadcastList();
	}

}
