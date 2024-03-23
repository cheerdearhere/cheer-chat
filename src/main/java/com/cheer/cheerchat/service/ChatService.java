package com.cheer.cheerchat.service;

import com.cheer.cheerchat.entity.Chat;
import com.cheer.cheerchat.entity.User;
import com.cheer.cheerchat.exception.ChatException;
import com.cheer.cheerchat.exception.UserException;
import com.cheer.cheerchat.request.GroupChatRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ChatService {
    public Chat createChat(User reqUser, List<Integer> userIdList) throws UserException;
    public Chat findChatById(Integer chatId) throws ChatException;
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException;
    public Chat updateGroupImg(Integer chatId, User reqUser, GroupChatRequest req) throws ChatException, UserException;
    public Chat addUserToGroup(Integer reqUserId, Integer userId, Integer chatId) throws UserException, ChatException;
    public Chat renameGroup(Integer chatId, String groupName, Integer reqUserId) throws ChatException, UserException;
    public Chat updateGroupHost(Integer chatId, Integer newHostId, Integer reqUserId) throws ChatException, UserException;
    public Chat removeFromGroup(Integer chatId, Integer userId, Integer reqUserId) throws ChatException, UserException;
    public Chat deleteChat(Integer chatId, Integer reqUserId) throws ChatException, UserException;



}
