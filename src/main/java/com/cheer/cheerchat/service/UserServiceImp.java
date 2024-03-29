package com.cheer.cheerchat.service;

import com.cheer.cheerchat.config.TokenProvider;
import com.cheer.cheerchat.entity.User;
import com.cheer.cheerchat.exception.UserException;
import com.cheer.cheerchat.repository.UserRepository;
import com.cheer.cheerchat.request.UpdateUserRequest;
import com.cheer.cheerchat.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    @Override
    public User findUserById(Integer id) throws UserException {
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isPresent())
            return optUser.get();
        else
            throw new UserException("User not found by id : " + id);
    }

    @Override
    public User findUserByProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt);
        if(email==null)
            throw new BadCredentialsException("received invalid token");
        User user = userRepository.findByEmail(email);
        if(user==null)
            throw new UserException("user not found with email: " + email);
        return user;
    }

    @Override
    public User updateUser(Integer userid, UpdateUserRequest request) throws UserException {
        User user = findUserById(userid);
        if(request.getFullName() != null)
            user.setFullName(request.getFullName());
        if(request.getProfilePicture() != null)
            user.setProfilePhoto(request.getProfilePicture());

        updateIdAndDate(userid,user);
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String query) {
        return userRepository.searchUser(query);
    }

    private void updateIdAndDate(Integer modifierId, User user){
        user.setModId(modifierId);
        user.setModDate(LocalDateTime.now());
    }
}
