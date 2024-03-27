package com.cheer.cheerchat.controller;

import com.cheer.cheerchat.entity.Chat;
import com.cheer.cheerchat.entity.Message;
import com.cheer.cheerchat.entity.User;
import com.cheer.cheerchat.exception.ChatException;
import com.cheer.cheerchat.exception.MessageException;
import com.cheer.cheerchat.exception.UserException;
import com.cheer.cheerchat.request.SendMessageRequest;
import com.cheer.cheerchat.service.MessageService;
import com.cheer.cheerchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/{chatId}")
    public ResponseEntity<List<Message>> getChatsHandler(
            @PathVariable Integer chatId,
            @RequestHeader(ControllerConstant.AUTHENTICATION_HEADER_NAME) String token
    )throws UserException, ChatException, MessageException{
        User user = userService.findUserByProfile(token);
        List<Message> messages = messageService.getChatMessages(chatId,user);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Message> sendMessageHandler(
            @RequestBody SendMessageRequest req,
            @RequestHeader(ControllerConstant.AUTHENTICATION_HEADER_NAME) String token
    ) throws UserException, ChatException {
        User user = userService.findUserByProfile(token);
        req.setUserId(user.getId());
        Message message = messageService.sendMessage(req);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Map<String,Object>> deleteMessageHandler(
            @PathVariable Integer messageId,
            @RequestHeader(ControllerConstant.AUTHENTICATION_HEADER_NAME) String token
    ) throws UserException, MessageException {
        Map<String, Object> resObj = new HashMap<>();
        User user = userService.findUserByProfile(token);
        messageService.deleteMessage(messageId,user);
        resObj.put("message_id",messageId);
        resObj.put("request_user",user.getId());
        resObj.put("result", "delete success");
        return new ResponseEntity<>(resObj,HttpStatus.ACCEPTED);
    }

}
