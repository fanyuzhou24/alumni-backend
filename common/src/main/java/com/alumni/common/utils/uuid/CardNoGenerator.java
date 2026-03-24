package com.alumni.common.utils.uuid;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

public class CardNoGenerator {

    public static String generateCardNo() {
        // yyyyMMdd
        String date = DateUtil.format(DateUtil.date(), "yyyyMMdd");

        // 10 位随机数字
        String random = RandomUtil.randomNumbers(10);

        return date + random;
    }

    public static void main(String[] args) {
        System.out.println(generateCardNo());
    }
}
