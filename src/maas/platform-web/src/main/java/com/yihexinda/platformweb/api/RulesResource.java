package com.yihexinda.platformweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.core.dto.Json;
import com.yihexinda.core.dto.Page;
import com.yihexinda.data.dto.RulesDto;
import com.yihexinda.data.dto.RulesPageQueryDto;
import com.yihexinda.platformweb.service.RulesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author: yuanbailin
 * @create: 2018-11-02 10:08
 **/
@Api(description = "更新后的规则接口")
@RestController
@RequestMapping("/api/rules")
public class RulesResource {

    @Autowired
    private RulesService rulesService;

    @ApiOperation(value = "根据条件分页查询规则", httpMethod = "POST")
    @PostMapping("/findRulePage")
    public Json<Page<RulesDto>> findRulePage(@RequestBody RulesPageQueryDto pageQueryDto) {
        return rulesService.findRulePage(pageQueryDto);
    }

    @ApiOperation(value = "新增规则信息", httpMethod = "POST", notes = "新增规则，如果是地区规则优先级必须设置，规则值value 1-客户所在城市 2-始发地城市 3-目的地城市")
    @PostMapping("/addRule")
    public Json addRule(@RequestBody RulesDto rulesDto) {
        return rulesService.addRule(rulesDto);
    }

    @ApiOperation(value = "更新规则信息", httpMethod = "POST", notes = "更新规则不能更新规则启用停用状态,不能修改规则类型")
    @PostMapping("/updateRule")
    public Json updateRule(@RequestBody RulesDto rulesDto) {
        return rulesService.updateRule(rulesDto);
    }

    @ApiOperation(value = "修改规则状态,启用/停用状态", httpMethod = "POST", notes = "非地区规则只能启用,启用会自动禁用同类型其他规则,地区规则不限制")
    @ApiImplicitParam(name = "ruleId", value = "规则id", dataType = "Integer", paramType = "form", required = true)
    @PostMapping("/changeRuleStatus")
    public Json changeRuleStatus(@RequestParam("ruleId") Integer ruleId) {
        return rulesService.changeRuleStatus(ruleId);
    }

    @ApiOperation(value = "根据id逻辑删除规则", httpMethod = "GET", notes = "只能删除停用的规则")
    @ApiImplicitParam(name = "ruleId", value = "规则id", dataType = "Integer", paramType = "path", required = true)
    @GetMapping("/deleteRule/{ruleId}")
    public Json deleteRule(@PathVariable Integer ruleId) {
        return rulesService.deleteRule(ruleId);
    }

    @ApiOperation(value = "检查规则配置是否有问题", httpMethod = "GET")
    @GetMapping("/checkRulesProblems")
    public Json<List<String>> checkRulesProblems() {
        return rulesService.checkRulesProblems();
    }

}
