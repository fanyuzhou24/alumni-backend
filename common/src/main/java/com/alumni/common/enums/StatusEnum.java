package com.alumni.common.enums;


public enum StatusEnum {
    CHECKING(0, "审批中"),
    CHECK_PASS(1, "审批通过"),
    CHECK_REFUSED(2, "审批拒绝"),
    NOT_APPLY(3, "未申请"),
    ;

    private final Integer code;
    private final String name;

    StatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}