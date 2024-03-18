package com.cheer.cheerchat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/cheer")
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<String> homeController(){
        return new ResponseEntity<>("Welcome to cheer-api using spring boot", HttpStatus.OK);
    }
}
