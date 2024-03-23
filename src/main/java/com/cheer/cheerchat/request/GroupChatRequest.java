package com.cheer.cheerchat.request;

import com.cheer.cheerchat.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class GroupChatRequest {
    private Integer chat_id;
    private List<Integer> userIdList;
    private String chat_name;
    private User chat_host;
    private String chat_image;
}
