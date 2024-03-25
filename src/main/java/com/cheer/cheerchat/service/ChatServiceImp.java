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
        Optional<User> targetUserOpt = userRepository.findById(userId);
        if(targetUserOpt.isEmpty()) throw new UserException("User not found by target user Id: "+userId);
        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        if(chatOpt.isEmpty()) throw new ChatException("Chat not found by chat Id: "+chatId);

        Chat chat = chatOpt.get();
        Set<User> userSet = chat.getChatUsers();
        User requireUser = reqUserOpt.get();
        User targetUser = targetUserOpt.get();
        //소속 확인
        if(!userSet.contains(requireUser) || !userSet.contains(targetUser))
            throw new UserException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        // 요청자가 host
        if(Objects.equals(chat.getChatHost(),requireUser)){
            //host가 나가면 방기능 off
            userSet.remove(requireUser);
            chat.setChatUsers(userSet);
            chatRepository.save(chat);
            return this.deleteChat(chatId, reqUserId);
        }
        else if(Objects.equals(requireUser,targetUser)){// 본인이 채팅방을 나가는 경우
            userSet.remove(targetUser);
            chat.setChatUsers(userSet);

            int chatUserNum = chat.getChatUsers().size();
            if(chatUserNum < 1) // 0 이하는 잘못된 접근
                throw new ChatException(HttpStatus.BAD_REQUEST.getReasonPhrase());
            else if(chatUserNum == 1) // 혼자 남은 방
                return this.deleteChat(chatId,reqUserId);
            else if( chatUserNum == 2)  {// 2명만 남은방 == no group
                chat.setGroup(false);
                updateIdAndDate(reqUserId,chat);
                return chatRepository.save(chat);
            }
            else { // 2명 이상 남은 방
                updateIdAndDate(reqUserId,chat);
                return chatRepository.save(chat);
            }
        }
        else // 호스트가 아닌 채팅 유저가 타인을 제거하는 경우
            throw new ChatException("no Auth : You Can't remove another user");
    }

    /**
     * just set unused chat, not remove data
     * @param chatId target chat sequence
     * @param reqUserId requiring user sequence
     * @return chat
     */
    @Override
    public Chat deleteChat(Integer chatId, Integer reqUserId) throws ChatException, UserException {
        Optional<User> reqUserOpt = userRepository.findById(reqUserId);
        if(reqUserOpt.isEmpty()) throw new UserException("User not found by user Id: "+reqUserId);
        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        if(chatOpt.isEmpty()) throw new ChatException("Chat not found by chat Id: "+chatId);

        Chat chat = chatOpt.get();
        if(!Objects.equals(chat.getChatHost(),reqUserOpt.get()))
            throw new ChatException("This user is not host in this chat: "+reqUserId);
        else{
            chat.setUseYn('0');
            chat.setModId(reqUserId);
            chat.setModDate(LocalDateTime.now());
            //update
            return chatRepository.save(chat);
        }
    }

    /**
     * complete remove on database
     * @param chatId target chat sequence number
     * @param hostId requiring user sequence number
     */
    @Override
    public void removeChat(Integer chatId, Integer hostId)throws ChatException, UserException {
        Optional<User> reqUserOpt = userRepository.findById(hostId);
        if (reqUserOpt.isEmpty()) throw new UserException("User not found by user Id: " + hostId);
        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        if (chatOpt.isEmpty()) throw new ChatException("Chat not found by chat Id: " + chatId);

        Chat chat = chatOpt.get();
        if(!Objects.equals(chat.getChatHost(),reqUserOpt.get()))
            throw new ChatException("This user is not host in this chat: "+hostId);
        else{
            chatRepository.deleteById(chatId);
        }
    }
// private methods
        private void updateIdAndDate(Integer modifierId, Chat chat){
        chat.setModId(modifierId);
        chat.setModDate(LocalDateTime.now());
    }

}
