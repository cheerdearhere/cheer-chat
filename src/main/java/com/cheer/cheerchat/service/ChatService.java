package com.cheer.cheerchat.service;

import com.cheer.cheerchat.entity.Chat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatService {
    public Chat createChat(List<Integer> chatRoomUser);


}
