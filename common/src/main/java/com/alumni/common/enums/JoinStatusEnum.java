package com.alumni.common.enums;


public enum JoinStatusEnum {
    NOT_JOIN("not_join", "未加入"),
    APPLY_JOIN("apply_join", "已申请加入"),
    JOINED("joined", "已加入"),
    ;

    private final String code;
    private final String name;

    JoinStatusEnum(String code, String name) {
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