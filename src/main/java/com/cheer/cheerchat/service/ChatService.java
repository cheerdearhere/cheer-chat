package com.cheer.cheerchat.service;

import com.cheer.cheerchat.entity.Chat;
import com.cheer.cheerchat.entity.User;
import com.cheer.cheerchat.exception.ChatException;
import com.cheer.cheerchat.exception.UserException;
import com.cheer.cheerchat.request.GroupChatRequest;


import java.util.List;

public interface ChatService {
    Chat createChat(User reqUser, List<Integer> userIdList) throws UserException;
    Chat findChatById(Integer chatId) throws ChatException;
    List<Chat> findAllChatByUserId(Integer userId) throws UserException;
    Chat updateGroupImg(Integer chatId, User reqUser, GroupChatRequest req) throws ChatException, UserException;
    Chat addUserToGroup(Integer reqUserId, Integer userId, Integer chatId) throws UserException, ChatException;
    Chat renameGroup(Integer chatId, String groupName, Integer reqUserId) throws ChatException, UserException;
    Chat updateGroupHost(Integer chatId, Integer newHostId, Integer reqUserId) throws ChatException, UserException;
    Chat removeFromGroup(Integer chatId, Integer userId, Integer reqUserId) throws ChatException, UserException;
    Chat deleteChat(Integer chatId, Integer reqUserId) throws ChatException, UserException;
    void removeChat(Integer chatId, Integer hostId)throws ChatException, UserException;
}