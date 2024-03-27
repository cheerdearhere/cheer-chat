package com.cheer.cheerchat.service;

import com.cheer.cheerchat.entity.Chat;
import com.cheer.cheerchat.entity.Message;
import com.cheer.cheerchat.entity.User;
import com.cheer.cheerchat.exception.ChatException;
import com.cheer.cheerchat.exception.MessageException;
import com.cheer.cheerchat.exception.UserException;
import com.cheer.cheerchat.repository.MessageRepository;
import com.cheer.cheerchat.request.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImp implements MessageService{
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final UserService userService;

    @Override
    public Message sendMessage(SendMessageRequest req) throws UserException, ChatException {
        User user = userService.findUserById(req.getUserId());
        Chat chat = chatService.findChatById(req.getChatId());
        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setMessageContentType(req.getMessageContentType());
        message.setTitle(req.getTitle());
        message.setContent(req.getContent());
        message.setRegId(user.getId());
        message.setRegDate(LocalDateTime.now());

        message = messageRepository.save(message);
        return message;
    }

    @Override
    public List<Message> getChatMessages(Integer chatId, User reqUser) throws ChatException{
        Chat chat = chatService.findChatById(chatId);
        if(!chat.getChatUsers().contains(reqUser))
            throw new ChatException("this user isn't member in this chat");
        List<Message> messages = messageRepository.findByChatId(chatId);
        return messages;
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if(messageOpt.isEmpty()) throw new MessageException("message not found with id: "+messageId);
        return messageOpt.get();
    }

    @Override
    public void deleteMessage(Integer messageId, User reqUser) throws MessageException {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if(messageOpt.isEmpty()) throw new MessageException("message not found with id: "+messageId);
        Message message = messageOpt.get();
        if(!message.getUser().getId().equals(reqUser.getId())) throw new MessageException("no authentication");
//        messageRepository.deleteById(messageId);
        message.setUseYn('0');
        message.setModDate(LocalDateTime.now());
        message.setModId(reqUser.getId());
    }
}
