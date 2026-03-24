package com.alumni.common.enums;


public enum UserTypeEnum {
    TOURIST("tourist", "游客"),
    ADMIN("admin", "管理员"),
    ALUMNI("alumni", "校友"),
    ;

    private final String code;
    private final String name;

    UserTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}