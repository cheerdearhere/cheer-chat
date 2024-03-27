package com.cheer.cheerchat.entity;

public enum MessageContentType {
    ERROR("-2"),
    BANNED("-1"),
    NO_DATA("0"),
    TEXT("1"),
    IMG("2"),
    VIDEO("3"),
    LARGE_FILE("4");


    private final String type;

    MessageContentType(String type) {
        this.type = type;
    }

    public Integer getTypeNum(){
        return Integer.parseInt(this.type);
    }
}
