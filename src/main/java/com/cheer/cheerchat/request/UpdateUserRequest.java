package com.cheer.cheerchat.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateUserRequest {
    private String fullName;
    private String profilePicture;
    
}
