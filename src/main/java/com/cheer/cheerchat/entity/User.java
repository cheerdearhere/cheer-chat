package com.cheer.cheerchat.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;


@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="USER_ID")
    private Integer id;
    @Column(name="FULL_NAME")
    private String fullName;
    private String email;
    @Column(name="PROFILE_PHOTO")
    private String profilePhoto;
    private String password;
    private String salt = (email == null || email.isEmpty() || email.equals(""))
            ? "nodata"
            : email.toLowerCase().substring(email.indexOf('@')+1, email.indexOf('@')+4)
    ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHAT_ID")
    private Chat chat;
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Notification> notifications = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getFullName(), user.getFullName()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getProfilePhoto(), user.getProfilePhoto()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getSalt(), user.getSalt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFullName(), getEmail(), getProfilePhoto(), getPassword(), getSalt());
    }
}
