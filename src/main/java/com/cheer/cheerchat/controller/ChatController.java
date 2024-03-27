package com.cheer.cheerchat.controller;

import com.cheer.cheerchat.entity.Chat;
import com.cheer.cheerchat.entity.User;
import com.cheer.cheerchat.exception.ChatException;
import com.cheer.cheerchat.exception.UserException;
import com.cheer.cheerchat.request.ChatRequest;
import com.cheer.cheerchat.service.ChatService;
import com.cheer.cheerchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController implements ControllerConstant{
    private final ChatService chatService;
    private final UserService userService;

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandler(
            @PathVariable Integer chatId,
            @RequestHeader(AUTHENTICATION_HEADER_NAME) String token) throws UserException, ChatException {
        User reqUser = userService.findUserByProfile(token);//권한 체크용
        Chat chat = chatService.findChatById(chatId);
        return new ResponseEntity<>(chat,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Chat>> findChatsHandler(@RequestHeader(AUTHENTICATION_HEADER_NAME) String token) throws UserException {
        User reqUser = userService.findUserByProfile(token);
        List<Chat> chatList = chatService.findAllChatByUserId(reqUser.getId());
        return new ResponseEntity<>(chatList,HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Chat> createChatHandler (
            @RequestBody ChatRequest chatRequest,
            @RequestHeader(AUTHENTICATION_HEADER_NAME) String token) throws UserException {
        User requestUser = userService.findUserByProfile(token);
        Chat chat = chatService.createChat(requestUser,chatRequest.getUserIdList());

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }
    @PutMapping("/add/{userId}")
    public ResponseEntity<Chat> addUserHandler(
            @RequestBody ChatRequest chatRequest,
            @PathVariable Integer userId,
            @RequestHeader(AUTHENTICATION_HEADER_NAME) String token)throws UserException, ChatException{
        User requestUser = userService.findUserByProfile(token);
        Chat chat = chatService.addUserToGroup(requestUser.getId(),userId,chatRequest.getChat_id());
        return new ResponseEntity<>(chat,HttpStatus.ACCEPTED);
    }
    @PutMapping("/group")
    public ResponseEntity<Chat> updateGroupImgAndNameHandler(
            @RequestBody ChatRequest chatRequest,
            @RequestHeader(AUTHENTICATION_HEADER_NAME) String token)throws UserException, ChatException{
        User requestUser = userService.findUserByProfile(token);
        Chat chat = chatService.updateGroupImgOrName(requestUser,chatRequest);
        return new ResponseEntity<>(chat,HttpStatus.ACCEPTED);
    }
    @PutMapping("/host")
    public ResponseEntity<Chat> updateGroupHostHandler(
            @RequestBody ChatRequest chatRequest,
            @RequestHeader(AUTHENTICATION_HEADER_NAME) String token)throws UserException, ChatException{
        User requestUser =  userService.findUserByProfile(token);
        Chat chat = chatService.updateGroupHost(chatRequest.getChat_id(),chatRequest.getChat_host().getId(), requestUser.getId());
        return new ResponseEntity<>(chat,HttpStatus.ACCEPTED);
    }
    @PatchMapping
    public ResponseEntity<Chat> removeUserFromGroupHandler(
            @RequestBody ChatRequest chatRequest,
            @RequestHeader(AUTHENTICATION_HEADER_NAME) String token)throws UserException, ChatException {
        User requestUser = userService.findUserByProfile(token);
        Chat chat = chatService.deleteChat(chatRequest.getChat_id(),requestUser.getId());
        return new ResponseEntity<>(chat, HttpStatus.ACCEPTED);
    }
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteChatHandler(
            @RequestBody ChatRequest chatRequest,
            @RequestHeader(AUTHENTICATION_HEADER_NAME) String token)throws UserException, ChatException {
        Map<String,Object> resMap = new HashMap<>();
        User requestUser = userService.findUserByProfile(token);
        chatService.removeChat(chatRequest.getChat_id(),requestUser.getId());
        resMap.put("targetChatId",chatRequest.getChat_id());
        resMap.put("reqUserId",requestUser.getId());
        resMap.put("result",true);
        return new ResponseEntity<>(resMap, HttpStatus.ACCEPTED);
    }
}
