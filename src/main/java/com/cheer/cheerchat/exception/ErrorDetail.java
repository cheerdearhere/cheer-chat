package com.cheer.cheerchat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetail {
    private String message;
    private String description;
    private LocalDateTime now;
}
