package com.alumni.common.enums;


public enum DeptTypeEnum {
    SCHOOL("school", "学校"),
    YEAR("year", "届"),
    CLASS("class", "班级"),
    ;

    private final String code;
    private final String name;

    DeptTypeEnum(String code, String name) {
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