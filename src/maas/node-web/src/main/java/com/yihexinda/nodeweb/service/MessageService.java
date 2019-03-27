package com.yihexinda.nodeweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yihexinda.auth.domain.User;
import com.yihexinda.core.Execption.BussException;
import com.yihexinda.core.dto.Json;
import com.yihexinda.data.dto.MessageDto;
import com.yihexinda.nodeweb.client.DataMessageClient;
import com.yihexinda.nodeweb.security.SecurityHelper;

/**
 * @author: yuanbailin
 * @create: 2018-10-27 19:33
 **/
@Service
public class MessageService {

    @Autowired
    private DataMessageClient messageClient;
    @Autowired
    private SecurityHelper securityHelper;

    public Json addMessage(MessageDto messageDto) {
        User user = securityHelper.getCurrentUser();
        messageDto.setUserId(user.getId().intValue());
        messageDto.setAbleType("Order");
        return messageClient.addMessage(messageDto);
    }

    public List<MessageDto> findMessageList() {
        //获取当前的登录用户
        User user = securityHelper.getCurrentUser();
        MessageDto messageDto = new MessageDto();
        messageDto.setUserId(user.getId().intValue());
        return messageClient.findMessageByUserId(messageDto);
    }

    public Json deleteMessageById(Integer messageId) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(messageId);
        return messageClient.deleteMessageById(messageDto);
    }

    public void rushOrderSuccessConfirm(Integer userId, String orderId, Integer code) {
        messageClient.rushOrderSuccessConfirm(userId, orderId, code);
    }

    public Json<List<MessageDto>> findMessageByUserIdOrOrderId(Integer userId, String orderId) {
        Json json = new Json();
        try {
            json.setSuccess(true);
            json.setObj(messageClient.findMessageByUserIdOrOrderId(userId, orderId));
        } catch (BussException e) {
            json.setMsg(e.getExceptionInfo());
        } catch (Exception e) {
            json.setMsg("系统内部错误");
        }

        return json;
    }

}
