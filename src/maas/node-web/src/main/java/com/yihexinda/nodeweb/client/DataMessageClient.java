package com.yihexinda.nodeweb.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.core.dto.Json;
import com.yihexinda.data.dto.MessageDto;

@FeignClient(value = "data-service")
@RequestMapping("/message")
@RestController
public interface DataMessageClient {

    @PostMapping("/addMessage")
    Json addMessage(@RequestBody MessageDto messageDto);

    @PostMapping("/findMessageByUserId")
    List<MessageDto> findMessageByUserId(@RequestBody MessageDto messageDto);

    @PostMapping("/deleteMessageById")
    Json deleteMessageById(@RequestBody MessageDto messageDto);

    @PostMapping("/rushOrderSuccessConfirm")
    void rushOrderSuccessConfirm(@RequestParam("userId") Integer userId, @RequestParam("orderId") String orderId, @RequestParam("code") Integer code);

    @PostMapping("/findMessageByUserIdOrOrderId")
    List<MessageDto> findMessageByUserIdOrOrderId(@RequestParam("userId") Integer userId, @RequestParam("orderId") String orderId);
}
