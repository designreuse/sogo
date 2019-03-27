package com.yihexinda.pcweb.api;

import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.JsonUtil;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.data.dto.TDriverDto;
import com.yihexinda.pcweb.client.BroadcastClient;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TBroadcastDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author zl
 * @date 2018/10/12.
 */
@Api(description = "广播接口")
@RestController()
@RequestMapping("/api/pc")
public class BroadcastResource {

	@Autowired
	private BroadcastClient broadcastClient;


	/**
	 *  添加广播信息
	 * @param data 广播实体信息
	 * @return ResultVo
	 */
	@ApiOperation(value = "添加广播", httpMethod = "GET")
	@RequestMapping(value = "/addBroadcast")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tbroadcastDto", value = "广播信息", required = true, paramType = "string"),
			@ApiImplicitParam(name = "token", value = "token", required = true, paramType = "string")
	})
	public ResultVo addBroadcast(@RequestBody Map<String,Object> data) {
		PayLoadVo payLoadVo= RequestUtil.analysisToken(StringUtil.trim(data.get("token")));
		TBroadcastDto tbroadcastDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tbroadcastDto")),TBroadcastDto.class);
		if (null!=payLoadVo){
			tbroadcastDto.setCreateId(payLoadVo.getUserId());
		}
		return broadcastClient.addBroadcast(tbroadcastDto);
	}


	/**
	 *  修改广播
	 * @param data 广播实体信息
	 * @return ResultVo
	 */
	@ApiOperation(value = "修改广播", httpMethod = "POST")
	@RequestMapping(value = "/updateBroadcast")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tbroadcastDto", value = "广播信息", required = true, paramType = "string"),
			@ApiImplicitParam(name = "token", value = "token", required = true, paramType = "string")
	})
	public ResultVo updateBroadcast(@RequestBody Map<String,Object> data) {
		TBroadcastDto tbroadcastDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tbroadcastDto")),TBroadcastDto.class);
		return broadcastClient.updateBroadcast(tbroadcastDto);
	}

	/**
	 *  查询广播
	 * @param condition 分页参数
	 * @return ResultVo
	 */
	@ApiOperation(value = "查询广播", httpMethod = "POST")
	@RequestMapping(value = "/broadcastList")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageIndex", value = "页数", required = true, paramType = "string"),
			@ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
			@ApiImplicitParam(name = "token", value = "token", required = true, paramType = "string")
	})
	public ResultVo broadcastList(@ApiIgnore @RequestBody Map<String, Object> condition) {
		return broadcastClient.broadcastList(condition);
	}

	/**
	 *  修改广播状态
	 * @param data 广播状态
	 * @return ResultVo
	 */
	@ApiOperation(value = "修改广播状态", httpMethod = "POST")
	@RequestMapping(value = "/updateBroadcastStatus")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "status", value = "状态", required = true, paramType = "string"),
			@ApiImplicitParam(name = "id", value = "广播id", required = true, paramType = "string"),
			@ApiImplicitParam(name = "token", value = "token", required = true, paramType = "string")
	})
	public ResultVo updateBroadcastStatus(@RequestBody Map<String,Object> data) {
		String status = StringUtil.trim(data.get("status"));
		String id = StringUtil.trim(data.get("id"));
		if ("".equals(status)||"".equals(id)){
			return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"缺少信息参数");
		}
		TBroadcastDto tBroadcastDto =new TBroadcastDto();
		tBroadcastDto.setId(id);
		tBroadcastDto.setStatus(status);
		return broadcastClient.updateBroadcastStatus(tBroadcastDto);
	}

	/**
	 *  查询广播详情
	 * @param data id
	 * @return ResultVo
	 */
	@ApiOperation(value = "查询广播详情", httpMethod = "POST")
	@RequestMapping(value = "/getBroadcast")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "广播id", required = true, paramType = "string"),
			@ApiImplicitParam(name = "token", value = "token", required = true, paramType = "string")
	})
	public ResultVo getBroadcast(@ApiIgnore @RequestBody Map<String, Object> data) {
		if (!"".equals(StringUtil.trim(data.get("id")))){
			return broadcastClient.getBroadcast(String.valueOf(data.get("id")));
		}

		return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"广播id不能为空");

	}


}
