package com.yihexinda.platformweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yihexinda.auth.domain.User;
import com.yihexinda.core.Execption.BussException;
import com.yihexinda.core.dto.Json;
import com.yihexinda.core.dto.Page;
import com.yihexinda.data.dto.RulesDto;
import com.yihexinda.data.dto.RulesPageQueryDto;
import com.yihexinda.platformweb.client.DataRulesClient;
import com.yihexinda.platformweb.security.SecurityHelper;

/**
 * @author: yuanbailin
 * @create: 2018-11-02 10:02
 **/
@Service
public class RulesService {

//    @Autowired
//    private DataRulesClient rulesClient;
//    @Autowired
//    private SecurityHelper securityHelper;
//
//    public Json<Page<RulesDto>> findRulePage(RulesPageQueryDto pageQueryDto) {
//        Json json = new Json();
//        try {
//            json.setSuccess(true);
//            json.setObj(rulesClient.findRulePage(pageQueryDto));
//        } catch (Exception e) {
//            json.setMsg("系统内部异常");
//        }
//        return json;
//    }
//
//
//    /**
//     * 新增规则信息
//     *
//     * @param rulesDto
//     * @return
//     */
//    public Json addRule(RulesDto rulesDto) {
//        Json json = new Json();
//        try {
//            User user = securityHelper.getCurrentUser();
//            rulesDto.setCreatedBy(user.getUsername());
//            rulesClient.addRule(rulesDto);
//            json.setSuccess(true);
//            json.setMsg("新增规则成功");
//        } catch (BussException e) {
//            //定义业务异常
//            json.setMsg(e.getMessage());
//        } catch (Exception e) {
//            //捕获系统异常
//            json.setMsg("服务器内部异常");
//        }
//        return json;
//    }
//
//    /**
//     * 更新规则信息
//     *
//     * @param rulesDto
//     * @return
//     */
//    public Json updateRule(RulesDto rulesDto) {
//        Json json = new Json();
//        try {
//            rulesDto.setUpdatedBy(securityHelper.getCurrentUser().getUsername());
//            rulesClient.updateRule(rulesDto);
//            json.setMsg("更新规则成功");
//            json.setSuccess(true);
//        } catch (BussException e) {
//            json.setMsg(e.getMessage());
//        } catch (Exception e) {
//            json.setMsg("服务器内部异常");
//        }
//        return json;
//    }
//
//    /**
//     * 修改规则状态
//     *
//     * @param ruleId
//     * @return
//     */
//    public Json changeRuleStatus(Integer ruleId) {
//        Json json = new Json();
//        try {
//            User user = securityHelper.getCurrentUser();
//            rulesClient.changeRuleStatus(ruleId, user.getUsername());
//            json.setMsg("更新规则成功");
//            json.setSuccess(true);
//        } catch (BussException e) {
//            json.setMsg(e.getMessage());
//        } catch (Exception e) {
//            json.setMsg("服务器内部异常");
//        }
//        return json;
//    }
//
//    /**
//     * 根据id逻辑删除规则
//     *
//     * @param ruleId
//     * @return
//     */
//    public Json deleteRule(Integer ruleId) {
//        User user = securityHelper.getCurrentUser();
//        return rulesClient.deleteRule(ruleId, user.getUsername());
//    }
//
//    /**
//     * 检查规则配置是否有问题
//     *
//     * @return
//     */
//    public Json<List<String>> checkRulesProblems() {
//        Json json = new Json();
//        try {
//            json.setSuccess(true);
//            json.setObj(rulesClient.checkRulesProblems());
//        } catch (Exception e) {
//            json.setMsg("系统内部错误");
//        }
//        return json;
//    }
}
