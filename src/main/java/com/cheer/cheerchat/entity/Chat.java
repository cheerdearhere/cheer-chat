package com.cheer.cheerchat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="CHAT_ID")
    private Integer id;
    @Column(name="CHAT_NAME")
    private String chatName;
    @Column(name = "CHAT_IMAGE")
    private String chatImage;
    @Column(name="IS_GROUP")
    private boolean isGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    private User createBy;
    @ManyToOne(fetch = FetchType.LAZY)
    private User chatHost;
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private Set<User> chatUsers = new HashSet<>();
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();


}
