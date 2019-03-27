package com.yihexinda.platformweb.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.core.dto.Json;
import com.yihexinda.core.dto.Page;
import com.yihexinda.data.dto.RulesDto;
import com.yihexinda.data.dto.RulesPageQueryDto;

@FeignClient("data-service")
@RequestMapping("/rules")
@RestController
public interface DataRulesClient {
    /**
     * 根据条件分页查询规则
     *
     * @param pageQueryDto
     * @return
     */
    @PostMapping("/findRulePage")
    Page<RulesDto> findRulePage(@RequestBody RulesPageQueryDto pageQueryDto);


    /**
     * 新增规则信息
     *
     * @param rulesDto
     * @return
     */
    @PostMapping("/addRule")
    Json addRule(@RequestBody RulesDto rulesDto);

    /**
     * 更新规则信息
     *
     * @param rulesDto
     * @return
     */
    @PostMapping("/updateRule")
    Json updateRule(@RequestBody RulesDto rulesDto);

    /**
     * 修改规则状态
     *
     * @param ruleId
     * @return
     */
    @PostMapping("/changeRuleStatus")
    Json changeRuleStatus(@RequestParam("ruleId") Integer ruleId, @RequestParam("updatedBy") String updatedBy);

    /**
     * 根据id逻辑删除规则
     *
     * @param ruleId
     * @return
     */
    @GetMapping("/deleteRule")
    Json deleteRule(@RequestParam("ruleId") Integer ruleId, @RequestParam("updatedBy") String updatedBy);

    /**
     * 检查规则配置是否有问题
     *
     * @return
     */
    @GetMapping("/checkRulesProblems")
    List<String> checkRulesProblems();


}
