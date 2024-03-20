package com.cheer.cheerchat.controller;

import com.cheer.cheerchat.entity.User;
import com.cheer.cheerchat.exception.UserException;
import com.cheer.cheerchat.request.UpdateUserRequest;
import com.cheer.cheerchat.response.ApiResponse;
import com.cheer.cheerchat.service.UserService;
import com.cheer.cheerchat.service.UserServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token) throws UserException {
        User user = userService.findUserByProfile(token);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUserHandler(@PathVariable String query){
        List<User> users = userService.searchUser(query);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserHandler (
            @RequestBody UpdateUserRequest request,
            @RequestHeader("Authorization") String token
    ) throws UserException {

        User user = userService.findUserByProfile(token);
        userService.updateUser(user.getId(),request);

        ApiResponse apiResponse = new ApiResponse("user updated successfully", true);

        return new ResponseEntity<>(apiResponse,HttpStatus.ACCEPTED);
    }
}
