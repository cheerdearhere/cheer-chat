package com.cheer.cheerchat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="MESSAGE_ID")
    private Integer id;
    private String content;

    private Integer regId;
    private Integer modId;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CHAT_ID")
    private Chat chat;


}
