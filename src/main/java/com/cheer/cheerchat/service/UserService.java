package com.cheer.cheerchat.service;

import com.cheer.cheerchat.entity.User;
import com.cheer.cheerchat.exception.UserException;
import com.cheer.cheerchat.request.UpdateUserRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public User findUserById(Long id);
    public User findUserByProfile(String jwt);
    public User updateUser(Long userid, UpdateUserRequest request) throws UserException;
    public List<User> searchUser(String query);
}