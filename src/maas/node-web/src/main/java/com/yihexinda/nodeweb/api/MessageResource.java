package com.yihexinda.nodeweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.core.Execption.BussException;
import com.yihexinda.core.dto.Json;
import com.yihexinda.data.dto.MessageDto;
import com.yihexinda.nodeweb.service.MessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yuanbailin
 * @create: 2018-10-27 19:10
 **/
@Api(description = "消息接口")
@RestController
@RequestMapping("/api/message")
@Slf4j
public class MessageResource {

    @Autowired
    private MessageService messageService;

    @ApiOperation(value = "新增消息", httpMethod = "POST")
    @PostMapping("/addMessage")
    public Json addMessage(@RequestBody MessageDto messageDto) {
        return messageService.addMessage(messageDto);
    }

    @ApiOperation(value = "根据当前用户查询对应的消息列表", httpMethod = "GET")
    @GetMapping("/findMessageList")
    public Json<List<MessageDto>> findMessageList() {
        Json json = new Json();
        try {
            json.setSuccess(true);
            json.setObj(messageService.findMessageList());
        } catch (BussException e) {
            json.setSuccess(false);
            json.setMsg(e.getExceptionInfo());
            log.warn("findMessageList{}", e);
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg("系统内部异常");
            log.warn("findMessageList{}", e);
        }
        return json;
    }

    @ApiOperation(value = "确认消息", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "消息id", dataType = "Integer", paramType = "path", required = true)
    @GetMapping("/readMessage/{id}")
    public Json deleteMessageById(@PathVariable Integer id) {
        return messageService.deleteMessageById(id);
    }

}
