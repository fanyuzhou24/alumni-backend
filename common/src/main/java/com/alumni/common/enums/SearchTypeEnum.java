package com.alumni.common.enums;


public enum SearchTypeEnum {
    YEAR("year", "同届"),
    CLASS("class", "同班"),
    INDUSTRY("industry", "同行"),
    LOCATION("location", "附近"),
    ;

    private final String code;
    private final String name;

    SearchTypeEnum(String code, String name) {
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