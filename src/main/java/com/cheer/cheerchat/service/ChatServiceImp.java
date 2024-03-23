package com.cheer.cheerchat.service;

import com.cheer.cheerchat.entity.Chat;
import com.cheer.cheerchat.entity.User;
import com.cheer.cheerchat.exception.ChatException;
import com.cheer.cheerchat.exception.UserException;
import com.cheer.cheerchat.repository.ChatRepository;
import com.cheer.cheerchat.repository.UserRepository;
import com.cheer.cheerchat.request.GroupChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatServiceImp implements ChatService{
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    public Chat createChat(User reqUser, List<Integer> userIdList) throws UserException {
        if(!Objects.equals(reqUser.getId(), userIdList.get(0)))
            throw new UserException(HttpStatus.BAD_REQUEST.getReasonPhrase());

        Set<User> users = null;
        if(userIdList.size() < 2){// 2인 이하는 x
            throw new UserException("expired chat");
        }
        else if(userIdList.size()==2){ //1:1 챗
            Integer guestUserId = userIdList.get(1);
            User user = userRepository.findById(guestUserId).orElseThrow();
            Chat isChatExist = chatRepository.findSingleChatByUserIds(user,reqUser);
            if(isChatExist != null) //기존 1:1 채팅은 기존 방 return
                return isChatExist;
            users = Stream.of(reqUser, user).collect(Collectors.toSet());
        }
        else{ // 그 이상 방은 새로 방 open
            users = userIdList.stream()
                    .map(userId-> {
                        try {
                            return userRepository.findById(userId)
                                    .orElseThrow(()->new UserException("User not found with ID: " + userId));
                        } catch (UserException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
        }
        return Chat.builder()
                .createBy(reqUser)
                .chatHost(reqUser)
                .chatUsers(users)
                .chatName(users.stream().map(User::getFullName).collect(Collectors.joining(", ")))
                .isGroup(users.size()>2)
                .regDate(LocalDateTime.now())
                .regId(reqUser.getId())
                .build();
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if(chat.isEmpty()) throw new ChatException("Chat not found by chat Id: "+chatId);
        return chat.get();
    }
    @Override
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new UserException("User not found by user Id: "+userId);
        return chatRepository.findChatByUserId(userId);
    }

    @Override
    public Chat addUserToGroup(Integer reqUserId, Integer userId, Integer chatId) throws UserException, ChatException {
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()) throw new UserException("User not found by user Id: "+userId);
        if(userRepository.findById(reqUserId).isEmpty()) throw new UserException("User not found by user Id: "+reqUserId);
        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        if(chatOpt.isEmpty()) throw new ChatException("Chat not found by chat Id: "+chatId);

        Chat chat = chatOpt.get();
        Set<User> users = chat.getChatUsers();
        users.add(userOpt.get());
        chat.setChatUsers(users);
        updateIdAndDate(reqUserId,chat);

        return chatRepository.save(chat);
    }
    @Override
    public Chat renameGroup(Integer chatId, String groupName, Integer reqUserId) throws ChatException, UserException {
        if(userRepository.findById(reqUserId).isEmpty())
            throw new UserException("User not found by user Id: "+reqUserId);
        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        if(chatOpt.isEmpty()) throw new ChatException("Chat not found by chat Id: "+chatId);

        Chat chat = chatOpt.get();
        if(Objects.equals(chat.getChatName(),groupName))
            throw new ChatException("Same chat name: "+groupName);
        else if(groupName==null)
            throw new ChatException("group name is null");

        chat.setChatName(groupName);
        updateIdAndDate(reqUserId,chat);

        return chatRepository.save(chat);
    }
    public Chat updateGroupHost(Integer chatId, Integer newHostId, Integer reqUserId) throws ChatException, UserException{
        if(userRepository.findById(reqUserId).isEmpty()) throw new UserException("User not found by user Id: "+reqUserId);
        Optional<User> newHostOpt = userRepository.findById(newHostId);
        if(newHostOpt.isEmpty()) throw new UserException("User not found by new host Id: "+newHostId);
        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        if(chatOpt.isEmpty()) throw new ChatException("Chat not found by chat Id: "+chatId);

        Chat chat = chatOpt.get();
        if(!Objects.equals(chat.getChatHost().getId(), reqUserId))
            throw new UserException("User is not Host: "+reqUserId);
        if(Objects.equals(chat.getChatHost().getId(),newHostId))
            throw new UserException("User is already host: "+newHostId);

        chat.setChatHost(newHostOpt.get());
        updateIdAndDate(reqUserId,chat);
        return chatRepository.save(chat);
    }
    @Override
    public Chat updateGroupImg(Integer chatId, User reqUser, GroupChatRequest req) throws ChatException, UserException {
        if(userRepository.findById(reqUser.getId()).isEmpty()) throw new UserException("User not found by user Id: "+reqUser.getId());
        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        if(chatOpt.isEmpty()) throw new ChatException("Chat not found by chat Id: "+chatId);

        Chat chat = chatOpt.get();
        if(req.getChat_image()==null) throw new ChatException("Image data is null");

        chat.setChatImage(req.getChat_image());
        updateIdAndDate(reqUser.getId(), chat);
        return chatRepository.save(chat);
    }
    @Override
    public Chat removeFromGroup(Integer chatId, Integer userId, Integer reqUserId) throws ChatException, UserException {
        Optional<User> reqUserOpt = userRepository.findById(reqUserId);
        if(reqUserOpt.isEmpty()) throw new UserException("User not found by user Id: "+reqUserId);
        Optional<User> newHostOpt = userRepository.findById(userId);
        if(newHostOpt.isEmpty()) throw new UserException("User not found by new host Id: "+userId);
        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        if(chatOpt.isEmpty()) throw new ChatException("Chat not found by chat Id: "+chatId);



        return null;
    }

    @Override
    public Chat deleteChat(Integer chatId, Integer reqUserId) throws ChatException, UserException {
        return null;
    }

    private void updateIdAndDate(Integer modifierId, Chat chat){
        chat.setModId(modifierId);
        chat.setModDate(LocalDateTime.now());
    }

}
