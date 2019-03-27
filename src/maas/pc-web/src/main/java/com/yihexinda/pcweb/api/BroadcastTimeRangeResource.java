package com.yihexinda.pcweb.api;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TBoradcastTimeRangeDto;
import com.yihexinda.data.dto.TBroadcastDto;
import com.yihexinda.pcweb.client.BoradcastTimeRangeClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author zl
 * @date 2018/10/12.
 */
@Api(description = "广播时间段接口")
@RestController()
@RequestMapping("/api/pc")
public class BroadcastTimeRangeResource {

	@Autowired
	private BoradcastTimeRangeClient boradcastTimeRangeClient;


	/**
	 *  添加广播时间段
	 * @param boradcastTimeRangeDto 广播时间段实体信息
	 * @return
	 */
	@ApiOperation(value = "添加广播时间段", httpMethod = "POST")
	@RequestMapping(value = "/addBroadcastTimeRange")
	public ResultVo addBroadcastTimeRange(@RequestBody TBoradcastTimeRangeDto boradcastTimeRangeDto) {
		return boradcastTimeRangeClient.addBroadcastTimeRange(boradcastTimeRangeDto);
	}

	/**
	 *  删除广播时间段
	 * @param broadcastId 广播id
	 * @return
	 */
	@ApiOperation(value = "删除广播时间段", httpMethod = "POST")
	@RequestMapping(value = "/deleteBroadcastTimeRange/{broadcastId}")
	public ResultVo deleteBroadcastTimeRange(@PathVariable("broadcastId") String broadcastId) {
		return boradcastTimeRangeClient.deleteBroadcastTimeRange(broadcastId);
	}

	/**
	 *  修改广播时间段
	 * @param boradcastTimeRangeDto 时间段实体信息
	 * @return
	 */
	@ApiOperation(value = "修改广播时间段", httpMethod = "POST")
	@RequestMapping(value = "/updateBroadcastTimeRange")
	public ResultVo updateBroadcastTimeRange(@RequestBody TBoradcastTimeRangeDto boradcastTimeRangeDto) {
		return boradcastTimeRangeClient.updateBroadcastTimeRange(boradcastTimeRangeDto);
	}



	/**
	 *  查询广播时间段
	 * @param condition 分页参数
	 * @return
	 */
	@ApiOperation(value = "查询广播时间段", httpMethod = "POST")
	@RequestMapping(value = "/broadcastTimeRangeList")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageIndex", value = "页数", required = true, paramType = "string"),
			@ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
	})
	public ResultVo broadcastTimeRangeList(@ApiIgnore @RequestBody Map<String, Object> condition) {
		return boradcastTimeRangeClient.broadcastTimeRangeList(condition);
	}

}
