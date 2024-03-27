package com.cheer.cheerchat.service;

import com.cheer.cheerchat.entity.Message;
import com.cheer.cheerchat.entity.User;
import com.cheer.cheerchat.exception.ChatException;
import com.cheer.cheerchat.exception.MessageException;
import com.cheer.cheerchat.exception.UserException;
import com.cheer.cheerchat.request.SendMessageRequest;

import java.util.List;

public interface MessageService {
    Message sendMessage(SendMessageRequest req) throws UserException, ChatException;
    List<Message> getChatMessages(Integer chatId, User reqUser) throws ChatException, MessageException;
    Message findMessageById(Integer messageId) throws MessageException;
    void deleteMessage(Integer messageId, User reqUSer) throws MessageException;
}
