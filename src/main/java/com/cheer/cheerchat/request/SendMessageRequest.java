package com.cheer.cheerchat.request;

import com.cheer.cheerchat.entity.MessageContentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SendMessageRequest {
    private Integer userId;
    private Integer chatId;
    private Integer messageId;
    private String title;
    private String content;
    private MessageContentType messageContentType;
}
